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
package org.riverock.webmill.portlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.port.PortalXslt;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.schema.site.SiteTemplate;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.AuthTools;
import org.riverock.common.html.Header;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Aug 26, 2003
 * Time: 4:40:19 PM
 *
 * $Id$
 */
public final class PortalRequestInstance {

    private static Logger log = Logger.getLogger( PortalRequestInstance.class );

    private static final int WEBPAGE_BUFFER_SIZE = 15000;

    ByteArrayOutputStream byteArrayOutputStream = null;

    PortalXslt xslt = null;
    SiteTemplate template = null;
    List portletResultOutput = null;

    int counter = 0;
    long startMills;
    DatabaseAdapter db = null;

    private ContextFactory contextFactory = null;
    private PortalInfo portalInfo = null;
    private Locale[] preferredLocale = null;
    private HttpServletRequest httpRequest = null;
    private HttpServletResponse httpResponse = null;
    private AuthSession auth = null;

    PortalRequestInstance() {
        startMills = System.currentTimeMillis();
        this.byteArrayOutputStream = new ByteArrayOutputStream( WEBPAGE_BUFFER_SIZE );
    }

    void init( HttpServletRequest request_, HttpServletResponse response_, DatabaseAdapter db )
        throws PortalException {

        this.httpRequest = request_;
        this.httpResponse = response_;
        try {

            this.auth = AuthTools.getAuthSession( httpRequest );
            this.portalInfo = PortalInfo.getInstance( db, httpRequest.getServerName() );

            this.contextFactory = ContextFactory.initTypeContext( db, httpRequest, portalInfo );
            this.preferredLocale = Header.getAcceptLanguageAsLocaleListSorted( httpRequest );
        }
        catch( Throwable e ) {
            String es = "Error create portal request instance";
            log.error( es, e );
            throw new PortalException( es, e );
        }

        // init string manager with real locale
//        this.stringManager = StringManager.getManager("mill.locale.main", contextFactory.getRealLocale());

//        this.portletRequest = new RenderRequestImpl(
//            new HashMap(),
//            httpRequest,
//            this.auth,
//            contextFactory.getRealLocale(),
//            preferredLocale
//        );
    }


    public Locale[] getPreferredLocale() {
        return preferredLocale;
    }

    public AuthSession getAuth() {
        return auth;
    }

    public String getNameTemplate() {
        if ( contextFactory == null )
            return null;

        return contextFactory.getNameTemplate();
    }

    public ContextFactory.PortletParameters getParameters(String namespace, TemplateItemTypeTypeType type ) {
        if ( contextFactory == null )
            return null;

        return contextFactory.getParameters( namespace, type );
    }

    public String getDefaultPortletType() {
        if ( contextFactory == null )
            return null;

        return contextFactory.getDefaultPortletType();
    }

    public Long getDefaultPortletId() {
        if ( contextFactory == null )
            return null;

        return contextFactory.getPortletId();
    }

    public Locale getLocale() {
        return contextFactory.getRealLocale();
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    boolean initTemplate()
        throws IOException {

        template = getPortalInfo().getTemplates().getTemplate(
            getNameTemplate(), getLocale().toString()
        );

        if ( template==null ) {
            String errorString = "Template '"+getNameTemplate()+"', locale "+getLocale().toString()+", not found";
            log.warn( errorString );
            byteArrayOutputStream.write( errorString.getBytes() );
            return false;
        }
        return true;
    }

    boolean initXslt() throws IOException {
        // prepare Xsl objects
        if ( getPortalInfo().getXsltList()==null ) {
            String errorString =
                "<html><head></head<body><p>XSL template not defined</p></body></html>";

            log.error( errorString );
            byteArrayOutputStream.write( errorString.getBytes() );
            return false;
        }

        xslt = getPortalInfo().getXsltList().getXslt( getLocale().toString() );

        if ( xslt==null ) {
            String errorString = "XSLT for locale "+getLocale().toString()+" not defined.";
            log.error( errorString );
            byteArrayOutputStream.write( errorString.getBytes() );
            return false;
        }
        return true;
    }
}
