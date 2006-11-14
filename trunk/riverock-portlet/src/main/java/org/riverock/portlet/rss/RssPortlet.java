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
package org.riverock.portlet.rss;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Portlet that transforms an RSS newsfeed
 *
 * @author SergeMaslyukov
 *         Date: 09.07.2006
 *         Time: 16:18:19
 *         $Id$
 */
public class RssPortlet extends GenericPortlet {
    private final static Logger log = Logger.getLogger( RssPortlet.class );
    /**
     * xsl file for rss 1.0
     */
    private static final String RSS10XSL = "/WEB-INF/xsl/Rss10.xsl";

    /**
     * xsl file for rss 2.0
     */
    private static final String RSS20XSL = "/WEB-INF/xsl/Rss20.xsl";

    /**
     * DOCUMENT ME!
     */
    private static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

    /**
     * compiled xsl file for 1.0
     */
    private Templates m_translet10;

    /**
     * compiled xsl file for 2.0
     */
    private Templates m_translet20;

    /**
     * Gets the xsl file and puts it into application scope
     *
     * @see javax.portlet.Portlet#init(javax.portlet.PortletConfig)
     */
    public void init() throws PortletException {
        try {
            InputStream xslstream = this.getPortletContext().getResourceAsStream(RssPortlet.RSS10XSL);
            StreamSource source = new StreamSource(xslstream);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            this.m_translet10 = tFactory.newTemplates(source);

            xslstream = this.getPortletContext().getResourceAsStream(RssPortlet.RSS20XSL);
            source = new StreamSource(xslstream);
            tFactory = TransformerFactory.newInstance();
            this.m_translet20 = tFactory.newTemplates(source);
        }
        catch (Exception e) {
            String es = "Error init translets";
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    /**
     * @see javax.portlet.Portlet#processAction(javax.portlet.ActionRequest,
        *      javax.portlet.ActionResponse)
     */
    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException {
        String errorMessage = null;

        //checkbox prefs
        //see how the prefs come over- can we just do a single checkbox?
        String[] checkPrefs = request.getParameterValues("checkPref");

        if (checkPrefs!=null) {
            PortletPreferences prefs = request.getPreferences();
            TreeSet<String> treePrefs = new TreeSet<String>(Arrays.asList(checkPrefs));
            prefs.setValues("RssXml", treePrefs.toArray(new String[0]));
            prefs.store();
        }

        //input url
        //check if the url is a valid url
        //if not, pass in a render param for error
        String url = request.getParameter("inputXml");

        if (url!=null && url.startsWith("http")) {
            try {
                //see if the url exists
                new URL(url).openStream();

                //add this as a pref
                PortletPreferences prefs = request.getPreferences();

                //add to the existing values
                TreeSet<String> existingPrefs = new TreeSet<String>(
                    Arrays.asList(
                        prefs.getValues("RssXml", new String[]{"http://www.theserverside.com/rss/theserverside-0.9.rdf"})
                    )
                );

                existingPrefs.add(url);
                prefs.setValues("RssXml", (String[]) existingPrefs.toArray(new String[0]));
                prefs.store();

                //store as the selected xml so that it shows up selected as displays
                response.setRenderParameter("selectXml", url);
                response.setRenderParameter("inputXml", url);

                //set the portlet mode back to view
                response.setPortletMode(PortletMode.VIEW);
            }
            catch (Exception e) {
                //just get the error message to pass to render
                errorMessage = e.getMessage();
            }

            if (errorMessage!=null) {
                response.setRenderParameter("errorMessage", errorMessage);
            }
        }

        //selectXml from drop-down
        String selectXml = request.getParameter("selectXml");

        if (selectXml!=null) {
            response.setRenderParameter("selectXml", selectXml);
        }
    }

    /**
     * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    protected void doEdit(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
        response.setContentType(CONTENT_TYPE_HTML);

        PrintWriter out = response.getWriter();
        PortletURL actionURL = response.createActionURL();

        out.println("<table>");
        out.println("<form name=\"inputForm\" target=\"_self\" method=\"POST\" action=\"" + actionURL.toString() + "\">");

        /*
         * display checkboxes for all the prefs
         */

        //get the preferences for the option
        PortletPreferences prefs = request.getPreferences();
        String[] rssPrefs = prefs.getValues("RssXml", new String[]{"http://www.theserverside.com/rss/theserverside-0.9.rdf"});

        for (int i = 0, len = rssPrefs.length; i < len; i++) {
            String pref = rssPrefs[i];
            out.println("<tr>");
            out.println("<td>");

            out.println("<input type=\"checkbox\" name=\"checkPref\" value=\"" + pref + "\" CHECKED >");
            out.println(pref);

            out.println("</td>");
            out.println("</tr>");
        }

        /*textbox for url*/
        out.println("<tr>");
        out.println("<td>");
        out.println("Additional RSS Feed:");
        out.println("<input type=\"text\" name=\"inputXml\" value=\"\">");
        out.println("<input name=\"inputSubmit\" type=\"submit\" value=\"submit\">");

        out.println("</td>");
        out.println("</tr>");
        out.println("</form>");
        out.println("</table>");
    }

    /**
     * @see javax.portlet.GenericPortlet#doHelp(javax.portlet.RenderRequest,javax.portlet.RenderResponse)
     */
    protected void doHelp(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
        response.setContentType(CONTENT_TYPE_HTML);

        PrintWriter out = response.getWriter();
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>");

        PortletURL renderURL = response.createRenderURL();
        renderURL.setPortletMode(PortletMode.VIEW);
        out.println(
            "The RSS Portlet transforms newsfeed using a default stylesheet<BR/>" +
            "Note that not all newsfeeds will transform correctly, but none should throw an error. <BR/>" +
            "To add or remove newfeeds, change the portlet preferences.<BR/>" +
            "<a href=\"" + renderURL.toString() + "\">back</a>"
        );
        out.println("</td>");
        out.println("</tr>");
        out.println("</form>");
        out.println("</table>");
    }

    /**
     * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,javax.portlet.RenderResponse)
     */
    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
        response.setProperty("expiration-cache", "130");

        InputStream xmlstream = null;
        InputStream xslstream = null;

        //try to get the selected value, if it exists
        String selectedXml = null;

        try {
            selectedXml = request.getParameter("selectXml");

            response.setContentType(CONTENT_TYPE_HTML);

            PrintWriter out = response.getWriter();

            /*error message from action, if any*/
            String errorMessage = request.getParameter("errorMessage");

            if (null != errorMessage) {
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>");

                out.println("<font color=\"red\">" + errorMessage + "</font>");
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
            }

            PortletURL actionURL = response.createActionURL();

            /*Select*/
            out.println("<table>");

            //get the action url
            out.print("<form name=\"selectForm\" method=\"POST\" action=\""+actionURL.toString()+"\" target=\"_self\">");
            out.println("<tr>");
            out.println("<td>");

            PortletURL renderLink = response.createRenderURL();
            renderLink.setPortletMode(PortletMode.HELP);
            out.println("<a href=\"" + renderLink + "\">help</a>");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>");
            out.println("&nbsp;");
            out.println("</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>");
            out.println("<select name=\"selectXml\">");

            //get the preferences for the option
            PortletPreferences prefs = request.getPreferences();
            String[] rssPrefs = prefs.getValues(
                "RssXml", new String[]{"http://www.theserverside.com/rss/theserverside-0.9.rdf"}
            );

            for (int i = 0, len = rssPrefs.length; i < len; i++) {
                out.print("<option value=\"");
                out.print(rssPrefs[i]);
                out.print("\"");

                if ((null != selectedXml) && rssPrefs[i].equals(selectedXml)) {
                    out.print(" SELECTED ");
                }

                out.print(">");
                out.print(rssPrefs[i]);
                out.println("</option>");
            }

            out.println("</select>");
            out.println("</td>");
            out.println("<td>");
            out.println("<input name=\"selectSubmit\" type=\"submit\" value=\"go\">");
            out.println("</td>");
            out.println("<td>");
            out.println("</td>");
            out.println("</tr>");
            out.println("</form>");
            out.println("</table>");

            /* XML */
            if (selectedXml==null) {
                selectedXml = request.getPreferences().getValue("RssXml",
                    "http://www.theserverside.com/rss/theserverside-0.9.rdf");
            }

            xmlstream = new URL(selectedXml).openStream();

            if (xmlstream==null) {
                throw new PortletException("No XML");
            }

            //make a dom from the stream, so we can tell if this is 1.0 or 2.0
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Source source = null;
            Transformer transformer = this.m_translet10.newTransformer();

            //see if we can parse the document to determine if this is 2.0  If not, go with the stream source
            try {
                Document document = builder.parse(xmlstream);
                Element element = document.getDocumentElement();

                //if we have a version attribute, then use 2.0
                if (element.hasAttribute("version") && element.getAttribute("version").equals("2.0")) {
                    transformer = this.m_translet20.newTransformer();
                }

                source = new DOMSource(document);
            }
            catch (Exception e) {
                e.printStackTrace();
                source = new StreamSource(xmlstream);
            }

            /* Transform */
            StreamResult output = new StreamResult(out);
            transformer.transform(source, output);
        }
        catch (Exception e) {
            String es = "Error transform feed";
            log.error(es, e);
            throw new PortletException(e);
        }

        finally {
            if (xmlstream != null) {
                xmlstream.close();
            }

            if (xslstream != null) {
                xslstream.close();
            }
        }
    }
}
