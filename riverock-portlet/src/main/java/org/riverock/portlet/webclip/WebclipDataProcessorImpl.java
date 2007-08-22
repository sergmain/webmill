/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.portlet.webclip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.ParameterParser;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.log4j.Logger;

import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;

/**
 * @author Sergei Maslyukov
 *         Date: 11.09.2006
 *         Time: 20:29:27
 *         <p/>
 *         $Id$
 */
public class WebclipDataProcessorImpl implements WebclipDataProcessor {
    private final static Logger log = Logger.getLogger( WebclipDataProcessorImpl.class );

    /**
     * URI parameter parser
     */
    public static final char SEPARATOR_HTTP_REQUEST_PARAM = '&';
    public static final char QUERY_SEPARATOR_HTTP_REQUEST_PARAM = '?';

    private static final String EDIT_ACTION_NAME = "edit";
    private static final String ACTION_NAME = "action";

    private static final String REL_ATTR = "rel";
    private static final String HREF_ATTR = "href";
    private static final String ID_ATTR = "id";
    private static final String CLASS_ATTR = "class";
    private static final String LONGDESC_ATTR = "longdesc";

    private static final String A_ELEMENT = "A";
    private static final String AREA_ELEMENT = "AREA";
    private static final String DIV_ELEMENT = "DIV";
    private static final String SPAN_ELEMENT = "SPAN";
    private static final String TABLE_ELEMENT = "TABLE";
    private static final String H3_ELEMENT = "H3";
    private static final String IMG_ELEMENT = "IMG";

    //  keep URL as is, false - leave only value of URL
    private static enum UrlStatus {
        NONE_STATUS, EXTERNAL_URI_STATUS, INTERNAL_URI_STATUS, ONLY_TEXT_URI_STATUS
    }


    // url producer
    private WebclipUrlProducer urlProducer = null;
    
    // document's fragment
    private Node fragmentNode = null;

    private PortalDaoProvider portalDaoProvider;
    private Long siteLanguageId;
    private static final String NOFOLLOW_VALUE = "nofollow";

    private WebclipUrlChecker checker;

    private final static ExcludeElement[] excludes = new ExcludeElement[]{
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox protected"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox current"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox cleanup plainlinks"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox cleanup metadata plainlink"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox cleanup metadata plainlinks"),
        new ExcludeElement(TABLE_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "infobox sisterproject"),

        new ExcludeElement(SPAN_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "noprint plainlinksneverexpand"),
        new ExcludeElement(SPAN_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "editsection"),

        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "noprint plainlinksneverexpand"),
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "magnify"), //                          
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "floatleft"), //  commons Logo
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "dablink"), //  disambiguation msg
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox cleanup metadata"), //
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox cleanup metadata plainlinks"), //
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "messagebox linkless metadata plainlinks"), //
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "boilerplate"), //
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "boilerplate metadata"), //
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.CLASS_ATTRIBUTE_TYPE, "notice metadata plainlinks"), //

        new ExcludeElement(DIV_ELEMENT, ExcludeElement.ID_ATTRIBUTE_TYPE, "administrator"), //  admin message about this page
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.ID_ATTRIBUTE_TYPE, "contentSub"), //  '(Redirected from Maybach 62S)' msg
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.ID_ATTRIBUTE_TYPE, "jump-to-nav"), //  Jump to:
        new ExcludeElement(DIV_ELEMENT, ExcludeElement.ID_ATTRIBUTE_TYPE, "siteNotice"),

        new ExcludeElement(H3_ELEMENT, ExcludeElement.ID_ATTRIBUTE_TYPE, "siteSub"), //  'From Wikipedia, the free encyclopedia' msg

        new ExcludeElement(IMG_ELEMENT, ExcludeElement.LONGDESC_ATTRIBUTE_TYPE, "/wiki/Image:Replace_this_image1.svg") // not uploaded image
    };

    /**
     *
     * @param urlProducer UrlProducer url producer
     * @param bytes content for modification
     * @param elementType int 1- table element, 2 - div element
     * @param elementId String
     * @param portalDaoProvider portal DOA provider
     * @param siteLanguageId ID of site language
     * @param checker url checker for check exist or not this url on site
     */
    public WebclipDataProcessorImpl(
        WebclipUrlProducer urlProducer,
        byte[] bytes, int elementType, String elementId, PortalDaoProvider portalDaoProvider,
        Long siteLanguageId, WebclipUrlChecker checker) {

        this.urlProducer = urlProducer;
        this.siteLanguageId = siteLanguageId;
        this.portalDaoProvider = portalDaoProvider;
        this.checker = checker;

        DOMFragmentParser parser = new DOMFragmentParser();

        HTMLDocument document = new HTMLDocumentImpl();
        DocumentFragment fragment;
        fragment = document.createDocumentFragment();

        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(bytes));
            inputSource.setEncoding(CharEncoding.UTF_8);
            parser.parse(inputSource, fragment);
            fragmentNode = searchNode(fragment, elementType, elementId);
        } catch (Exception e) {
            String es="Error parse HTML fragment.";
            log.error(es, e);
            throw new IllegalStateException(es +' ' + e.getMessage());
        }
    }

    /**
     *
     * @param node Node
     * @param elementType int 1- table element, 2 - div element
     * @param elementId String
     * @return Node
     */
    private Node searchNode(Node node, int elementType, String elementId) {
        if (node == null) {
            return null;
        }

        if ( node.getNodeType() == Node.ELEMENT_NODE) {
                switch (elementType) {
                    case WebclipConstants.TABLE_NODE_TYPE:
                        if (node.getNodeName().equalsIgnoreCase(TABLE_ELEMENT) && getIdAttr(node).equals(elementId)) {
                            return node;
                        }
                        break;
                    case WebclipConstants.DIV_NODE_TYPE:
                        if (node.getNodeName().equalsIgnoreCase(DIV_ELEMENT) && getIdAttr(node).equals(elementId)) {
                            return node;
                        }
                        break;
                }
        }

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            node = childNodes.item(i);
            Node result = searchNode(node, elementType, elementId);
            if (result!=null) {
                return result;
            }
        }
        return null;
    }

    private String getIdAttr(Node node) {
        Attr attrs[] = sortAttributes(node.getAttributes());
        for (Attr attr : attrs) {
            if (attr.getName().equalsIgnoreCase("id")) {
                return attr.getValue();
            }
        }
        return "";
    }

    /**
     * modify URL
     *
     * @param out output stream
     * @throws IOException

     */
    public void modify(OutputStream out) throws IOException {
        print(fragmentNode, out);
    }

    /**
     * recursively print node
     *
     * @param node node
     * @param out output stream
     * @throws IOException on error
     */
    private void print(Node node, OutputStream out) throws IOException {
        if (node == null) {
            return;
        }

        int type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_NODE:
                print(((Document) node).getDocumentElement(), out);
                out.flush();
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    node = childNodes.item(i);
                    print(node, out);
                    out.flush();
                }
                break;

            case Node.ELEMENT_NODE: {
                if (isSkipElement(node)) {
                    break;
                }
                boolean isNotEditAHref = !isHrefWithActionEditParam(node);
                if (isNotEditAHref) {

                    boolean isNoFollowAttr = false;
                    UrlStatus linkStatus = UrlStatus.NONE_STATUS;
                    if (node.getNodeName().equalsIgnoreCase(A_ELEMENT) ||
                        node.getNodeName().equalsIgnoreCase(AREA_ELEMENT)) {
                        NamedNodeMap attrMap = node.getAttributes();
                        for (int i = 0; i < attrMap.getLength(); i++) {
                            Node tempNode = attrMap.item(i);
                            if (tempNode.getNodeName().equalsIgnoreCase(HREF_ATTR)) {
                                linkStatus = checkUrl(tempNode.getNodeValue());
                                if (linkStatus== UrlStatus.INTERNAL_URI_STATUS) {
                                    urlProducer.init();
                                    urlProducer.setCurrentHrefValue(tempNode.getNodeValue());
                                    tempNode.setNodeValue( urlProducer.getUrl() );
                                }
                            }
                            if (tempNode.getNodeName().equalsIgnoreCase(REL_ATTR) &&
                                tempNode.getNodeValue().equalsIgnoreCase(NOFOLLOW_VALUE)) {
                                isNoFollowAttr = true;
                            }
                        }
                    }
                    if (linkStatus!= UrlStatus.ONLY_TEXT_URI_STATUS) {
                        out.write('<');
                        out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                        Attr attrs[] = sortAttributes(node.getAttributes());
                        for (Attr attr : attrs) {
                            out.write(' ');
                            out.write(attr.getNodeName().getBytes(CharEncoding.UTF_8));
                            out.write("=\"".getBytes(CharEncoding.UTF_8));
                            out.write(attr.getNodeValue().getBytes(CharEncoding.UTF_8));
                            out.write('"');
                        }
                        if (linkStatus== UrlStatus.EXTERNAL_URI_STATUS && !isNoFollowAttr) {
                            out.write( (' ' +REL_ATTR+"=\""+ NOFOLLOW_VALUE +"\" ").getBytes(CharEncoding.UTF_8));
                        }
                        out.write('>');
                    }
                    // Put text of "A HREF" element
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        int len = children.getLength();
                        for (int i = 0; i < len; i++) {
                            print(children.item(i), out);
                        }
                    }

                    if (linkStatus!=UrlStatus.ONLY_TEXT_URI_STATUS) {
                        // Put closed element
                        out.write("</".getBytes(CharEncoding.UTF_8));
                        out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                        out.write('>');
                    }
                }
                else {
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        int len = children.getLength();
                        for (int i = 0; i < len; i++) {
                            print(children.item(i), out);
                        }
                    }
                }

                break;
            }

            // handle entity reference nodes
            case Node.ENTITY_REFERENCE_NODE: {
                out.write(SEPARATOR_HTTP_REQUEST_PARAM);
                out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                out.write(';');
                break;
            }

            // print cdata sections
            case Node.CDATA_SECTION_NODE: {
                out.write("<![CDATA[".getBytes(CharEncoding.UTF_8));
                out.write(node.getNodeValue().getBytes(CharEncoding.UTF_8));
                out.write("]]>".getBytes(CharEncoding.UTF_8));
                break;
            }

            // print text
            case Node.TEXT_NODE: {
                out.write(node.getNodeValue().getBytes(CharEncoding.UTF_8));
                break;
            }

            // print processing instruction
            case Node.PROCESSING_INSTRUCTION_NODE: {
                out.write("<?".getBytes(CharEncoding.UTF_8));
                out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    out.write(' ');
                    out.write(data.getBytes(CharEncoding.UTF_8));
                }
                out.write("?>".getBytes(CharEncoding.UTF_8));
                break;
            }
        }

        out.flush();
    }

    private boolean isSkipElement(Node node) {
        for (ExcludeElement exclude : excludes) {
            if (node.getNodeName().equalsIgnoreCase(exclude.name)) {
                NamedNodeMap attrMap = node.getAttributes();
                for (int i = 0; i < attrMap.getLength(); i++) {
                    Node tempNode = attrMap.item(i);
                    switch(exclude.typeOfAttribute) {
                        case ExcludeElement.ID_ATTRIBUTE_TYPE:
                            if (tempNode.getNodeName().equalsIgnoreCase(ID_ATTR) &&
                                tempNode.getNodeValue().equals(exclude.value)) {
                                return true;
                            }
                            break;
                        case ExcludeElement.CLASS_ATTRIBUTE_TYPE:
                            if (tempNode.getNodeName().equalsIgnoreCase(CLASS_ATTR) &&
                                tempNode.getNodeValue().equals(exclude.value)) {
                                return true;
                            }
                            break;
                        case ExcludeElement.LONGDESC_ATTRIBUTE_TYPE:
                            if (tempNode.getNodeName().equalsIgnoreCase(LONGDESC_ATTR) &&
                                tempNode.getNodeValue().equals(exclude.value)) {
                                return true;
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }


    /**
     * EXTERNAL_URI_STATUS
     * INTERNAL_URI_STATUS
     * ONLY_TEXT_URI_STATUS
     * @param url URL
     * @return true - keep URL as is, false - leave only value of URL
     * @throws URIException on error
     */
    private UrlStatus checkUrl(final String url) throws URIException {
        if (StringUtils.isBlank(url)) {
            return UrlStatus.ONLY_TEXT_URI_STATUS;
        }
        if (!url.startsWith(WebclipConstants.ROOT_URI)) {
            return UrlStatus.EXTERNAL_URI_STATUS;
        }
        else {
            if (!url.startsWith(WebclipConstants.WIKI_URI)) {
                return UrlStatus.ONLY_TEXT_URI_STATUS;
            }
            String path = URIUtil.decode(url);
            path = path.substring(WebclipConstants.WIKI_URI.length()+1);
            if (StringUtils.isBlank(path)) {
                return UrlStatus.ONLY_TEXT_URI_STATUS;
            }

            if (path.indexOf('#')!=-1) {
                path = path.substring(0, path.indexOf('#'));
            }

            if (checker.isExist(siteLanguageId, path)) {
                return UrlStatus.INTERNAL_URI_STATUS;
            }
        }
        return UrlStatus.ONLY_TEXT_URI_STATUS;
    }

    private boolean isHrefWithActionEditParam(Node node) throws URIException {
        if (node==null) {
            return false;
        }

        if (node.getNodeName().equalsIgnoreCase(A_ELEMENT)) {
            NamedNodeMap attrMap = node.getAttributes();
            for (int i = 0; i < attrMap.getLength(); i++) {
                Node tempNode = attrMap.item(i);
                if (tempNode.getNodeName().equalsIgnoreCase(HREF_ATTR)) {
                    String url = tempNode.getNodeValue();

                    if (StringUtils.isBlank(url)) {
                        return false;
                    }

                    url = prepareUrl(url);
                    if (StringUtils.isBlank(url)) {
                        return false;
                    }

                    url = URIUtil.decode(url);
                    if (StringUtils.isBlank(url)) {
                        return false;
                    }

                    if (url.indexOf(QUERY_SEPARATOR_HTTP_REQUEST_PARAM)==-1) {
                        return false;
                    }
                    List<NameValuePair> params;
                    try {
                        url = StringUtils.replace(URIUtil.decode(url), "&amp;", "&");
                        //noinspection unchecked
                        params = new ParameterParser().parse(url.substring(url.indexOf(QUERY_SEPARATOR_HTTP_REQUEST_PARAM)+1), SEPARATOR_HTTP_REQUEST_PARAM);
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        log.error("Error parse url with params. URL: " + url);
                        throw e;
                    }
                    for (NameValuePair param : params) {
                        if (param.getName()==null || param.getValue()==null) {
                            return false;
                        }
                        if (param.getName().equals(ACTION_NAME) && param.getValue().equals(EDIT_ACTION_NAME)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;  
    }

    private String prepareUrl(String url) {
        if (url.startsWith(WebclipConstants.ROOT_URI)) {
            if (url.startsWith(WebclipConstants.WIKI_URI)) {
               return null;
            }
            return url;
        }
        else {
            if (url.toLowerCase().startsWith("http://en.wikipedia.org/w/")) {
                return url;
            }
            return null;
        }
    }

    /**
     * sorting of attributes of node
     *
     * @param attrs map with attributes
     * @return Attr[] sorted array of attributes
     */
    private Attr[] sortAttributes(NamedNodeMap attrs) {

        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        if (attrs==null) {
            return array;
        }
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return (array);
    }
}
