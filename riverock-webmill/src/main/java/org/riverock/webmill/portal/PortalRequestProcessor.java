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
package org.riverock.webmill.portal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.template.TemplateItemBaseClass;

/**
 * User: SergeMaslyukov
 * Date: 24.11.2004
 * Time: 23:53:05
 */
public final class PortalRequestProcessor {

    private final static Logger log = Logger.getLogger( PortalRequestProcessor.class );

    private static final int NUM_LINES = 100;

    /**
     *  Main method for processing pages with portlets
     */
    static void processPortalRequest( PortalRequestInstance portalRequestInstance ) throws Exception {

        if ( log.isDebugEnabled() ) {
            log.debug( "Dynamic content type: "+portalRequestInstance.getRequestContext().getDefaultPortletName() );
            log.debug( "Template name: "+portalRequestInstance.getRequestContext().getTemplateName() );
            log.debug( "Locale request:\n"+portalRequestInstance.getLocale().toString() );
            if (portalRequestInstance.template!=null) {
                log.debug( "template:\n" + portalRequestInstance.template.toString());
            }
            else {
                log.debug( "template is null ");
            }
        }

        if (!checkTemplateRole(portalRequestInstance)) {
            portalRequestInstance.byteArrayOutputStream.write(
                ("You has no sufficient roles. Need: "+portalRequestInstance.template.getRole()).getBytes()
            );
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start processing action method");
        }
        processActionSiteTemplateItems( portalRequestInstance );
        if (portalRequestInstance.getRedirectUrl()!=null) {
            if (log.isDebugEnabled()) {
                log.debug("RedirectUrl flag is true, terminate processing");
            }
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start processing render method");
        }
        render( portalRequestInstance );

        if (portalRequestInstance.getRedirectUrl()!=null) {
            if (log.isDebugEnabled()) {
                log.debug("RedirectUrl flag is true, terminate processing");
            }
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Start build result page");
        }
        buildPage( portalRequestInstance );

        if (log.isDebugEnabled()) {
            log.debug("Finish processing page");
        }
    }

    private static boolean checkTemplateRole(PortalRequestInstance portalRequestInstance) {
        if (portalRequestInstance.template==null ||
            StringUtils.isBlank( portalRequestInstance.template.getRole() ) ) {
            return true;
        }

        if (portalRequestInstance.getAuth()==null) {
            return false;
        }

        StringTokenizer st = new StringTokenizer( portalRequestInstance.template.getRole(), ", ", false);
        while (st.hasMoreTokens()) {
            String role = st.nextToken();
            if (portalRequestInstance.isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    private static void processActionSiteTemplateItems( PortalRequestInstance portalRequestInstance ) {

        if ( log.isDebugEnabled() ) {
            log.debug( "Start process action" );
        }

        for (PageElement pageElement : portalRequestInstance.getPageElementList()) {
            if (log.isDebugEnabled()) {
                if (pageElement.getPortalTemplateItem().getTypeObject().getType()==PortalTemplateItemType.PORTLET_TYPE ||
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

            // PortletParameters not null only for action portlet.
            // For all others portlet from template PortletParameters can be null
            if (pageElement.getParameters()!=null && !pageElement.getParameters().getRequestState().isActionRequest())
                continue;

            TemplateItemBaseClass.processActionTemplateItem(pageElement);

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalRequestInstance.setRedirectUrl(pageElement.getRedirectUrl());
                if (log.isDebugEnabled()) {
                    log.debug("redirectUrl: " + portalRequestInstance.getRedirectUrl());
                }
                return;
            }
        }
    }

    private static void render( PortalRequestInstance portalRequestInstance ) {

        for (PageElement pageElement : portalRequestInstance.getPageElementList()) {
            PortalTemplateItem templateItem = pageElement.getPortalTemplateItem();

            if (log.isDebugEnabled()) {
                log.debug(
                    "TemplateItem, " +
                        "type: " + (templateItem.getType() != null ? templateItem.getType() : null) + ", " +
                        "value: " + templateItem.getValue() + ", " +
                        "code: " + templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot()
                );
            }

            TemplateItemBaseClass.renderTemplateItem(pageElement);

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalRequestInstance.setRedirectUrl(pageElement.getRedirectUrl());
                if (log.isDebugEnabled()) {
                    log.debug("redirectUrl: " + portalRequestInstance.getRedirectUrl());
                }
                return;
            }
        }
    }

    private final static Object syncCtxDebug = new Object();
    private static void buildPage(PortalRequestInstance portalRequestInstance) throws Exception {

        ByteArrayOutputStream outputStream = null;
        Iterator<PageElement> iterator = portalRequestInstance.getPageElementList().iterator();

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

            SitePortletData item;
            if ( pageElement.getException()!=null || pageElement.getErrorString()!=null ) {
                String es = "";
                if (pageElement.getErrorString()!=null) {
                    es += pageElement.getErrorString();
                }
                if (pageElement.getException()!=null) {
                    es += (
                        "<br>" +
                        ExceptionTools.getStackTrace( pageElement.getException(), NUM_LINES, "<br>")
                        );
                }

                item = PortalRequestProcessor.setData( es.getBytes(), true, false );
            }
            else {
                item = pageElement.getData();
            }

            if ( log.isDebugEnabled() ) {
                log.debug( "Value of template item: "+ pageElement.getPortalTemplateItem().getValue() );
                log.debug( "portlet result data: " + item );
                log.debug( "getIsError(): " + item.getIsError()+", getIsXml() - "+item.getIsXml() );
                log.debug("#30.1-"+i);
            }

            boolean isXml = false;
            if (pageElement!=null && pageElement.getPortalTemplateItem()!=null && pageElement.getPortalTemplateItem().getParameters()!=null) {
                String isXmlString = PortletService.getString(pageElement.getPortalTemplateItem().getParameters(), "is-xml", "false");
                isXml = Boolean.parseBoolean( isXmlString );
            }
            boolean isElementXml = Boolean.TRUE.equals(item.getIsXml()) || isXml;

            if ( Boolean.TRUE.equals(item.getIsError()) || !isElementXml ) {

                // transform and output previous page elements
                if (outputStream!=null) {
                    try {
                        ByteArrayOutputStream tempBytes = new ByteArrayOutputStream(500);
                        processTransforming( outputStream, portalRequestInstance.xslt.getTransformer(), tempBytes, portalRequestInstance.getTempPath());
                        portalRequestInstance.byteArrayOutputStream.write(tempBytes.toByteArray());
                    } catch (Exception e) {
                        log.warn("Error transform page element", e);
                        portalRequestInstance.byteArrayOutputStream.write("Error transform page element".getBytes());
                    }
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

                portalRequestInstance.byteArrayOutputStream.write( item.getData() );
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


                boolean isNextXml = false;
                boolean isNextElementXml = false;
                if (pageElementNext!=null) {
                    if ( pageElementNext.getPortalTemplateItem()!=null && pageElementNext.getPortalTemplateItem().getParameters()!=null) {
                        String isXmlString = PortletService.getString(pageElementNext.getPortalTemplateItem().getParameters(), "is-xml", "false");
                        isNextXml = Boolean.parseBoolean( isXmlString );
                    }
                    isNextElementXml = pageElementNext.getIsXml() || isNextXml;
                }

                // if next portlet not xml's portlet, then transform current part and continue
                if ( pageElementNext==null || !isNextElementXml ) {
                    if ( log.isDebugEnabled() ) {
                        log.debug("#1.4 pageElementNext: " + pageElementNext);
                        if (pageElementNext!=null) {
                            log.debug( "#1.4 is xml: "+pageElementNext.getIsXml() );
                        }
                    }

                    try {
                        ByteArrayOutputStream tempBytes = new ByteArrayOutputStream(500);
                        processTransforming( outputStream, portalRequestInstance.xslt.getTransformer(), tempBytes, portalRequestInstance.getTempPath());
                        portalRequestInstance.byteArrayOutputStream.write(tempBytes.toByteArray());
                    } catch (Exception e) {
                        log.warn("Error transform page element", e);
                        portalRequestInstance.byteArrayOutputStream.write("Error transform page element".getBytes());
                    }
                    outputStream = null;
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("#30.2-"+i);
            }

            pageElement = pageElementNext;
            if (iterator.hasNext())
                pageElementNext = iterator.next();
            else
                pageElementNext = null;
        }
    }

    private static final Object syncObj = new Object();
    private static void processTransforming(final ByteArrayOutputStream xml, Transformer transformer, ByteArrayOutputStream arrayOutputStream, File tempPath) throws IOException, TransformerException {

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
                    //catch debug;
                }
            }
        }

        ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
        Source xmlSource = new StreamSource( stream );

        if (log.isDebugEnabled()) {
            log.debug("#40.2");
        }

        transformer.transform( xmlSource, new StreamResult( arrayOutputStream ) );
    }

    static SitePortletData setData(String data, boolean isError, boolean isXml) {
        return setData(data.getBytes(), isError, isXml);
    }

    public static SitePortletData setData(byte[] bytes, boolean isError, boolean isXml) {
        SitePortletData data = new SitePortletData();

        data.setData( bytes );
        data.setIsError( isError );
        data.setIsXml( isXml );

        return data;
    }
}
