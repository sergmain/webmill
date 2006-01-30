/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.StringTools;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalException;

import org.apache.log4j.Logger;

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
            log.debug( "Dynamic content type: "+portalRequestInstance.getDefaultPortletDefinition() );
            log.debug( "Template name: "+portalRequestInstance.getNameTemplate() );
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

        processActionSiteTemplateItems( portalRequestInstance );
        if (portalRequestInstance.getRedirectUrl()!=null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("#20.1");
        }

        render( portalRequestInstance );
        if (portalRequestInstance.getRedirectUrl()!=null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("#20.2");
        }

        buildPage( portalRequestInstance );

        if (log.isDebugEnabled()) {
            log.debug("#20.3");
        }
    }

    private static boolean checkTemplateRole(PortalRequestInstance portalRequestInstance) {
        if (portalRequestInstance.template==null ||
            StringTools.isEmpty( portalRequestInstance.template.getRole() ) ) {
            return true;
        }

        if (portalRequestInstance.getAuth()==null) {
            return false;
        }

        StringTokenizer st = new StringTokenizer( portalRequestInstance.template.getRole(), ", ", false);
        while (st.hasMoreTokens()) {
            String role = st.nextToken();
            if (portalRequestInstance.getAuth().isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    private static void processActionSiteTemplateItems( PortalRequestInstance portalRequestInstance ) {

        if ( log.isDebugEnabled() ) {
            log.debug( "Start process action" );
        }

        Iterator iterator = portalRequestInstance.getPageElementList().iterator();
        while( iterator.hasNext() ) {
            PageElement pageElement = (PageElement)iterator.next();
            TemplateItemBaseClass.processActionTemplateItem( pageElement );

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalRequestInstance.setRedirectUrl( pageElement.getRedirectUrl() );
                if ( log.isDebugEnabled() ) {
                    log.debug( "redirectUrl: " + portalRequestInstance.getRedirectUrl() );
                }
                return;
            }
        }
    }

    private static void render( PortalRequestInstance portalRequestInstance ) {

        Iterator iterator = portalRequestInstance.getPageElementList().iterator();
        while( iterator.hasNext() ) {
            PageElement pageElement = (PageElement)iterator.next();
            PortalTemplateItem templateItem = pageElement.getPortalTemplateItem();

            if ( log.isDebugEnabled() ) {
                log.debug(
                    "TemplateItem, " +
                    "type: "+(templateItem.getType()!=null?templateItem.getType().toString():null)+", " +
                    "value: "+templateItem.getValue()+", " +
                    "namespace: " + pageElement.getNamespace() + ", " +
                    "code: "+ templateItem.getCode() + ", xmlRoot: " + templateItem.getXmlRoot()
                );
            }

            TemplateItemBaseClass.renderTemplateItem( pageElement );

            // check if request was redirected
            if (pageElement.getIsRedirected()) {
                portalRequestInstance.setRedirectUrl( pageElement.getRedirectUrl() );
                if ( log.isDebugEnabled() ) {
                    log.debug( "redirectUrl: " + portalRequestInstance.getRedirectUrl() );
                }
                return;
            }
        }
    }

    private static Object syncCtxDebug = new Object();
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

            SitePortletData item = null;
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
                    processTransforming( outputStream, portalRequestInstance );
                    outputStream = null;
                }

                if ( log.isDebugEnabled() ) {
                    synchronized(syncCtxDebug) {
                        MainTools.writeToFile( 
                          WebmillConfig.getWebmillDebugDir()+
                          System.currentTimeMillis()+
			  "ctx-from-url-"+i+".xml", item.getData() );
                    }
                }

                portalRequestInstance.byteArrayOutputStream.write( item.getData() );
            }
            else {
                // 1st xml element
                if ( outputStream==null ) {
                    outputStream = new ByteArrayOutputStream();

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

                    processTransforming( outputStream, portalRequestInstance );
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
    private static void processTransforming( final ByteArrayOutputStream xml, final PortalRequestInstance portalRequestInstance )
        throws Exception {
        xml.write( "</SiteTemplate>".getBytes() );

        if ( log.isDebugEnabled() )
            log.debug( "string to transforming\n"+xml );

        xml.flush();
        xml.close();

        if (log.isDebugEnabled()) {
            log.debug("#40.1");
        }

        byte[] bytes = xml.toByteArray();
        synchronized( syncObj) {
            if (log.isDebugEnabled()) {
                String fileName = WebmillConfig.getWebmillTempDir() + "portlet-data.xml";
                log.debug( "write portlet result to file "+fileName );
                MainTools.writeToFile( fileName, bytes );
            }
        }
        
        ByteArrayInputStream stream = new ByteArrayInputStream( bytes );
        Source xmlSource = new StreamSource( stream );

        if (log.isDebugEnabled()) {
            log.debug("#40.2");
        }

        try {
            portalRequestInstance.xslt.getTransformer().transform( xmlSource, new StreamResult( portalRequestInstance.byteArrayOutputStream ) );
        }
        catch(javax.xml.transform.TransformerException e) {
            final String es = "TransformerException";
            log.error(es, e);
            throw new PortalException( es, e );
        }
    }

    static SitePortletData setData(String data, boolean isError, boolean isXml) {
        return setData(data.getBytes(), isError, isXml);
    }

    static SitePortletData setData(byte[] bytes, boolean isError, boolean isXml) {
        SitePortletData data = new SitePortletData();

        data.setData( bytes );
        data.setIsError( isError );
        data.setIsXml( isXml );

        return data;
    }
}
