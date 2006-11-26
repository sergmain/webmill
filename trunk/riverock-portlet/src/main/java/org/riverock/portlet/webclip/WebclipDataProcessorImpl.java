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

import org.apache.commons.lang.CharEncoding;
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

/**
 * @author Sergei Maslyukov
 *         Date: 11.09.2006
 *         Time: 20:29:27
 *         <p/>
 *         $Id$
 */
public class WebclipDataProcessorImpl implements WebclipDataProcessor {
    private final static Logger log = Logger.getLogger( WebclipDataProcessorImpl.class );

    // url producer
    private WebclipUrlProducer urlProducer = null;
    
    // document's fragment
    private Node fragmentNode = null;

    /**
     *
     * @param urlProducer UrlProducer url producer
     * @param bytes ������� ��� ����������� �����
     * @param elementType int 1- table element, 2 - div element
     * @param elementId String
     */
    public WebclipDataProcessorImpl(WebclipUrlProducer urlProducer, byte[] bytes, int elementType, String elementId) {
        this.urlProducer = urlProducer;

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
                        if (node.getNodeName().equalsIgnoreCase("table") && getIdAttr(node).equals(elementId)) {
                            return node;
                        }
                        break;
                    case WebclipConstants.DIV_NODE_TYPE:
                        if (node.getNodeName().equalsIgnoreCase("div") && getIdAttr(node).equals(elementId)) {
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
     * �������������� ���
     *
     * @param out �������� �����
     * @throws IOException

     */
    public void modify(OutputStream out) throws IOException {
        print(fragmentNode, out);
    }

    /**
     * �������� ���� � ��������
     *
     * @param node ����
     * @param out �������� �����
     * @throws IOException
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
                if (node.getNodeName().equalsIgnoreCase("A")) {
                    NamedNodeMap attrMap = node.getAttributes();
                    for (int i = 0; i < attrMap.getLength(); i++) {
                        Node tempNode = attrMap.item(i);
                        if (tempNode.getNodeName().equalsIgnoreCase("href")) {
                            urlProducer.init();
                            urlProducer.setCurrentHrefValue(tempNode.getNodeValue());
                            tempNode.setNodeValue( urlProducer.getUrl() );
                        }
                    }
                }
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
                out.write('>');
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        print(children.item(i), out);
                    }
                }
                break;
            }

            // handle entity reference nodes
            case Node.ENTITY_REFERENCE_NODE: {
                out.write('&');
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

        if (type == Node.ELEMENT_NODE) {
            out.write("</".getBytes(CharEncoding.UTF_8));
            out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
            out.write('>');
        }

        out.flush();

    }

    /**
     * ���������� ���������� ����
     *
     * @param attrs ���� � �����������
     * @return Attr[]
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