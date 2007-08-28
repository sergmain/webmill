/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.portal.page_element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.portal.instance.PortalInstance;
import org.riverock.webmill.portal.instance.PortalResponse;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.PortalTransformationParameters;

/**
 * User: SergeMaslyukov
 * Date: 24.11.2004
 * Time: 23:53:05
 */
public final class PortalPageController {

    private final static Logger log = Logger.getLogger( PortalPageController.class );

    /**
     *  Main method for processing pages with portlets
     * @param portalInstance portal current instance
     * @param portalRequestInstance request instance
     * @param portalResponse portal response
     * @throws Exception on any error
     */
    public static void processPortalRequest(PortalInstance portalInstance, PortalRequestInstance portalRequestInstance, PortalResponse portalResponse) throws Exception {

        List<PageElement> pageElements = InitPageElements.initPageElements(portalRequestInstance, portalInstance);

        if ( log.isDebugEnabled() ) {
            log.debug( "Dynamic content type: "+portalRequestInstance.getRequestContext().getDefaultPortletName() );
            log.debug( "Template name: "+portalRequestInstance.getRequestContext().getTemplateName() );
            log.debug( "Locale request:\n"+portalRequestInstance.getLocale().toString() );
            if (portalRequestInstance.getTemplate()!=null) {
                log.debug( "template:\n" + portalRequestInstance.getTemplate().toString());
            }
            else {
                log.debug( "template is null ");
            }
        }

        if (!checkTemplateRole(portalRequestInstance)) {
            portalResponse.getByteArrayOutputStream().write(
                ("You has no sufficient roles. Need: "+portalRequestInstance.getTemplate().getRole()).getBytes()
            );
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start processing action method");
        }
        processActionSiteTemplateItems(portalResponse, pageElements );
        if (portalResponse.getRedirectUrl()!=null) {
            if (log.isDebugEnabled()) {
                log.debug("RedirectUrl flag is true, terminate processing");
            }
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start processing render method");
        }
        render( portalRequestInstance, portalResponse, pageElements );

        if (portalResponse.getRedirectUrl()!=null) {
            if (log.isDebugEnabled()) {
                log.debug("RedirectUrl flag is true, terminate processing");
            }
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start build result page");
        }
        buildPage( portalRequestInstance, portalResponse, pageElements );

        if (log.isDebugEnabled()) {
            log.debug("Finish processing page");
        }
    }

    private static boolean checkTemplateRole(PortalRequestInstance portalRequestInstance) {
        if (portalRequestInstance.getTemplate()==null ||
            StringUtils.isBlank( portalRequestInstance.getTemplate().getRole() ) ) {
            return true;
        }

        if (portalRequestInstance.getAuth()==null) {
            return false;
        }

        StringTokenizer st = new StringTokenizer( portalRequestInstance.getTemplate().getRole(), ", ", false);
        while (st.hasMoreTokens()) {
            String role = st.nextToken();
            if (portalRequestInstance.isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    private static void processActionSiteTemplateItems(
        PortalResponse portalResponse, List<PageElement> pageElements) {

        if ( log.isDebugEnabled() ) {
            log.debug( "Start process action" );
        }

        for (PageElement pageElement : pageElements) {
/*
            if (log.isDebugEnabled()) {
                if (pageElement.getPortalTemplateItem().getTypeObject().getType()== PortalTemplateItemType.PORTLET_TYPE ||
                    pageElement.getPortalTemplateItem().getTypeObject().getType()==PortalTemplateItemType.DYNAMIC_TYPE) {

                    log.debug("PageElement: ");
                    if (pageElement.getParameters()!=null) {
                        log.debug("    isAction: "+pageElement.getParameters().getRequestState().isActionRequest());
                        log.debug("    namespace: "+pageElement.getParameters().getNamespace());
                    }
                    log.debug("    template item  type: "+pageElement.getPortalTemplateItem().getTypeObject().toString());
                    log.debug("    portlet name: "+pageElement.getFullPortletName());
                }
                else {
                    log.debug("Non-portlet page element");
                }
            }
*/

            // PortletParameters not null only for action portlet.
            // For all others portlet from template PortletParameters can be null
            if (!pageElement.isAction()) {
                continue;
            }

            pageElement.processAction();

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalResponse.setRedirectUrl(pageElement.getRedirectUrl());
                if (log.isDebugEnabled()) {
                    log.debug("redirectUrl: " + portalResponse.getRedirectUrl());
                }
                return;
            }
        }
    }

    private static void render( PortalRequestInstance portalRequestInstance, PortalResponse portalResponse, List<PageElement> pageElements ) {

        for (PageElement pageElement : pageElements) {
/*
            ElementParameter templateItem = pageElement.getPortalTemplateItem();

            if (log.isDebugEnabled()) {
                log.debug(
                    "TemplateItem, " +
                        "type: " + (templateItem.getType() != null ? templateItem.getType() : null) + ", " +
                        "value: " + templateItem.getValue() + ", " +
                        "code: " + templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot()
                );
            }
*/

            pageElement.render();

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalResponse.setRedirectUrl(pageElement.getRedirectUrl());
                if (log.isDebugEnabled()) {
                    log.debug("redirectUrl: " + portalResponse.getRedirectUrl());
                }
                return;
            }
        }
    }

    private final static Object syncCtxDebug = new Object();
    private static void buildPage(PortalRequestInstance portalRequestInstance, PortalResponse portalResponse, List<PageElement> pageElements) throws Exception {

        ByteArrayOutputStream outputStream = null;
        Iterator<PageElement> iterator = pageElements.iterator();

        if (!iterator.hasNext()) {
            return;
        }
        PageElement pageElement = iterator.next();
        PageElement pageElementNext = null;
        if (iterator.hasNext())
            pageElementNext = iterator.next();

        int i=0;
        while( pageElement!=null ) {
            ++i;
            if (log.isDebugEnabled()) {
                log.debug("#30.1-"+i);
            }

            SitePortletData item = pageElement.getData();

            if ( log.isDebugEnabled() ) {
                log.debug( "portlet result data: " + item );
                log.debug( "getIsError(): " + item.getIsError()+", getIsXml() - "+item.getIsXml() );
                log.debug("#30.1-"+i);
            }

            boolean isElementXml = Boolean.TRUE.equals(item.getIsXml());

            if ( Boolean.TRUE.equals(item.getIsError()) || !isElementXml ) {

                // transform and output previous page elements
                if (outputStream!=null) {
                    transformContent(portalRequestInstance, portalResponse, outputStream);
                    outputStream = null;
                }

                if ( log.isDebugEnabled() ) {
                    synchronized(syncCtxDebug) {
                        try {
                            MainTools.writeToFile(
                                portalRequestInstance.getTempPath().getAbsolutePath() +
                                    File.separatorChar + System.currentTimeMillis()+ "ctx-from-url-"+i+".xml",
                                item.getData()
                            );
                        } catch (Throwable e) {
                            // catch debug
                        }
                    }
                }

                portalResponse.getByteArrayOutputStream().write( item.getData() );
            }
            else {
                // 1st xml element
                if ( outputStream==null ) {
                    outputStream = new ByteArrayOutputStream(2000);

                    // format for controll of expiration of content
                    //<META HTTP-EQUIV="Expires" CONTENT="Wed, 01 Jan 1986 00:00:01 GMT">
                    //<META HTTP-EQUIV="Last-Modified" CONTENT="Wed, 17 Mar 2010 10:13:25 GMT">

                    outputStream.write( "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<SiteTemplate language=\"".getBytes() );
                    outputStream.write( portalRequestInstance.getLocale().toString().getBytes() );
                    outputStream.write( "\">".getBytes() );
                }

                outputStream.write( item.getData() );


                boolean isNextElementXml = false;
                if (pageElementNext!=null) {
                    isNextElementXml = pageElementNext.isXml();
                }

                // if next portlet not xml's portlet, then transform current part and continue
                if ( pageElementNext==null || !isNextElementXml ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug("#1.4 pageElementNext: " + pageElementNext);
                        if (pageElementNext!=null) {
                            log.debug( "#1.4 is xml: "+pageElementNext.isXml() );
                        }
                    }

                    transformContent(portalRequestInstance, portalResponse, outputStream);
                    outputStream = null;
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("#30.2-"+i);
            }

            pageElement = pageElementNext;
            if (iterator.hasNext()) {
                pageElementNext = iterator.next();
            }
            else {
                pageElementNext = null;
            }
        }
    }

    private static void transformContent(PortalRequestInstance portalRequestInstance, PortalResponse portalResponse, ByteArrayOutputStream outputStream) throws IOException {
        try {
            ByteArrayOutputStream tempBytes = new ByteArrayOutputStream(500);
            processTransforming(outputStream, portalRequestInstance.getXslt().getTransformer(), tempBytes, portalRequestInstance.getTempPath(),
                portalRequestInstance.getPortalTransformationParameters()
            );
            portalResponse.getByteArrayOutputStream().write(tempBytes.toByteArray());
        }
        catch (Exception e) {
            log.warn("Error transform page element", e);
            portalResponse.getByteArrayOutputStream().write("Error transform page element".getBytes());
        }
    }

    private static final Object syncObj = new Object();
    private static void processTransforming(
        final ByteArrayOutputStream xml, Transformer transformer, ByteArrayOutputStream arrayOutputStream, File tempPath,
        PortalTransformationParameters portalTransformationParameters
    ) throws IOException, TransformerException {

        xml.write( "</SiteTemplate>".getBytes() );

        if ( log.isDebugEnabled() )
            log.debug( "string to transforming\n"+xml );

        xml.flush();
        xml.close();

        if (log.isDebugEnabled()) {
            log.debug("#40.1");
        }

        byte[] bytes = xml.toByteArray();
        if (log.isDebugEnabled()) {
            synchronized( syncObj) {
                try {
                    String fileName =
                        tempPath.getAbsolutePath() + File.separatorChar + System.currentTimeMillis() + "portlet-data.xml";
                    log.debug( "write portlet result to file "+fileName );
                    MainTools.writeToFile( fileName, bytes );
                }
                catch(Throwable th) {
                    //catch debug error;
                }
            }
        }

        ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
        Source xmlSource = new StreamSource( stream );

        if (log.isDebugEnabled()) {
            log.debug("#40.3, title: " + portalTransformationParameters.getTitle());
            log.debug("#40.4, keyword: " + portalTransformationParameters.getKeyword());
            log.debug("#40.5, author: " + portalTransformationParameters.getAuthor());
            log.debug("#40.6, portal context path: " + portalTransformationParameters.getPortalContextPath());
        }

        transformer.setParameter("webmill-portal.portal-context-path", portalTransformationParameters.getPortalContextPath());
        if (StringUtils.isNotBlank(portalTransformationParameters.getTitle())) {
            transformer.setParameter("webmill-portal.title", portalTransformationParameters.getTitle());
        }
        if (StringUtils.isNotBlank(portalTransformationParameters.getKeyword())) {
            transformer.setParameter("webmill-portal.keyword", portalTransformationParameters.getKeyword());
        }
        if (StringUtils.isNotBlank(portalTransformationParameters.getAuthor())) {
            transformer.setParameter("webmill-portal.author", portalTransformationParameters.getAuthor());
        }
        transformer.transform( xmlSource, new StreamResult( arrayOutputStream ) );
    }

}
