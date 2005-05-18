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
package org.riverock.webmill.a3.audit;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.riverock.common.html.Header;
import org.riverock.webmill.core.GetSiteAccessUrlFullList;
import org.riverock.webmill.core.GetSiteAccessUrlItem;
import org.riverock.webmill.core.GetSiteAccessUserAgentFullList;
import org.riverock.webmill.core.GetSiteAccessUserAgentItem;
import org.riverock.webmill.core.InsertSiteAccessStatItem;
import org.riverock.webmill.core.InsertSiteAccessUrlItem;
import org.riverock.webmill.core.InsertSiteAccessUserAgentItem;
import org.riverock.webmill.schema.core.SiteAccessStatItemType;
import org.riverock.webmill.schema.core.SiteAccessUrlItemType;
import org.riverock.webmill.schema.core.SiteAccessUrlListType;
import org.riverock.webmill.schema.core.SiteAccessUserAgentItemType;
import org.riverock.webmill.schema.core.SiteAccessUserAgentListType;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.site.SiteListSite;

import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

/**
 * User: Admin
 * Date: Mar 19, 2003
 * Time: 10:42:41 PM
 *
 * $Id$
 */
public final class RequestStatisticFilter implements Filter {

    private final static Logger log = Logger.getLogger( RequestStatisticFilter.class );

    // ----------------------------------------------------- Instance Variables


    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;

    private static Map userAgent = null;
    private static Map url = null;

    private static Object userAgentSync = new Object();
    private static Object urlSync = new Object();

    // --------------------------------------------------------- Public Methods


    /**
     * Take this filter out of service.
     */
    public void destroy()
    {
        this.filterConfig = null;
    }

    public void doFilter( ServletRequest request, ServletResponse response,
        FilterChain chain )
        throws IOException, ServletException
    {

        String startInfo = null;
        //Todo uncomment for production
        if ( log.isDebugEnabled() ) {
            log.debug( "enter into filter" );
            startInfo = getDebugInfo( request, response );
        }

//        if ( filterConfig == null ) {
//            try {
//                chain.doFilter( request, response );
//                return;
//            }
//            catch( Throwable th ) {
//                log.fatal( "General error call chain of filter", th );
//                throw new ServletException( th );
//            }
//        }


        DatabaseAdapter db_ = null;
        boolean isOutputData = false;
        boolean isPrintData = false;
        try {
            try {
                db_ = DatabaseAdapter.getInstance();
            }
            catch( Exception e ) {
                log.error( "Error get db connect", e );
                DatabaseAdapter.close( db_ );
                db_ = null;
            }

            if ( db_ != null ) {
                try {
                    CustomSequenceType seq = new CustomSequenceType();
                    SiteAccessStatItemType stat = new SiteAccessStatItemType();

                    if ( log.isDebugEnabled() )
                        log.debug( "Start save userAgent info" );

                    Long idUserAgent = null;
                    {
                        String userAgentString = Header.getUserAgent( request );
                        if ( userAgentString == null )
                            userAgentString = "UserAgent unknown";
                        else if ( userAgentString.length()<5 )
                            userAgentString = "UserAgent too small";
                        else
                            userAgentString = StringTools.truncateString( userAgentString, 150 );

                        boolean isLoop = true;
                        while( isLoop ) {
                            boolean isNeedInsertUserAgent = false;
                            isLoop = false;

                            if ( log.isDebugEnabled() )
                                log.debug( "Start userAgent loop" );

                            if ( userAgent == null ) {
                                synchronized (userAgentSync) {
                                    if ( userAgent == null ) {
                                        if ( log.isDebugEnabled() )
                                            log.debug( "userAgent is null" );

                                        SiteAccessUserAgentListType userAgentList =
                                            GetSiteAccessUserAgentFullList.getInstance( db_, 0 ).item;

                                        if ( log.isDebugEnabled() )
                                            log.debug( "count of userAgent " + userAgentList.getSiteAccessUserAgentCount() );

                                        userAgent = new HashMap( userAgentList.getSiteAccessUserAgentCount() + 10, 1.2f );

                                        for( int i = 0; i<userAgentList.getSiteAccessUserAgentCount(); i++ ) {
                                            SiteAccessUserAgentItemType userAgentItem =
                                                userAgentList.getSiteAccessUserAgent( i );
                                            userAgent.put( userAgentItem.getUserAgent(),
                                                userAgentItem.getIdSiteUserAgent() );
                                        }
                                    }

                                    idUserAgent = (Long)userAgent.get( userAgentString );

                                    if ( log.isDebugEnabled() )
                                        log.debug( "userAgent - " + idUserAgent );

                                    if ( idUserAgent == null ) {
                                        isNeedInsertUserAgent = true;
                                        seq.setSequenceName( "SEQ_SITE_ACCESS_USER_AGENT" );
                                        seq.setTableName( "SITE_ACCESS_USER_AGENT" );
                                        seq.setColumnName( "ID_SITE_USER_AGENT" );
                                        idUserAgent = new Long( db_.getSequenceNextValue( seq ) );
                                        userAgent.put( userAgentString, idUserAgent );
                                    }
                                }
                            }

                            if ( isNeedInsertUserAgent ) {
                                if ( log.isDebugEnabled() )
                                    log.debug( "save new userAgent, id " + idUserAgent );

                                SiteAccessUserAgentItemType item =
                                    new SiteAccessUserAgentItemType();
                                item.setIdSiteUserAgent( idUserAgent );
                                item.setUserAgent( userAgentString );
                                try {
                                    if ( log.isDebugEnabled() )
                                        log.debug( "Call InsertSiteAccessUserAgentItem.processData(db_, item)" );

                                    InsertSiteAccessUserAgentItem.process( db_, item );
                                    db_.commit();

                                }
                                catch( Exception e ) {
                                    if ( !( e instanceof SQLException ) || !db_.testExceptionIndexUniqueKey( e ) )
                                        throw e;

                                    synchronized (userAgentSync) {
                                        userAgent.clear();
                                        userAgent = null;
                                    }
                                }
                                synchronized (userAgentSync) {
                                    if ( log.isDebugEnabled() )
                                        log.debug( "reinit classes" );

                                    GetSiteAccessUserAgentItem.reinit();
                                    GetSiteAccessUserAgentFullList.reinit();
                                    isLoop = true;
                                }
                            }
                        }
                    }

                    if ( log.isDebugEnabled() )
                        log.debug( "Start save url info" );

                    Long idUrl = null;
                    {
                        String urlString = ( (HttpServletRequest)request ).getRequestURI();
                        boolean isLoop = true;
                        while( isLoop ) {
                            boolean isNeedInsertUrl = false;
                            isLoop = false;
                            if ( url == null ) {
                                synchronized (urlSync) {
                                    if ( log.isDebugEnabled() )
                                        log.debug( "Start url loop" );

                                    if ( url == null ) {
                                        SiteAccessUrlListType urlList =
                                            GetSiteAccessUrlFullList.getInstance( db_, 0 ).item;
                                        url = new HashMap( urlList.getSiteAccessUrlCount() + 10, 1.2f );
                                        for( int i = 0; i<urlList.getSiteAccessUrlCount(); i++ ) {
                                            SiteAccessUrlItemType urlItem = urlList.getSiteAccessUrl( i );
                                            url.put( urlItem.getUrl(),
                                                urlItem.getIdSiteAccessUrl() );
                                        }
                                    }
                                    idUrl = (Long)url.get( urlString );
                                    if ( idUrl == null ) {
                                        isNeedInsertUrl = true;
                                        seq.setSequenceName( "SEQ_SITE_ACCESS_URL" );
                                        seq.setTableName( "SITE_ACCESS_URL" );
                                        seq.setColumnName( "ID_SITE_ACCESS_URL" );
                                        idUrl = new Long( db_.getSequenceNextValue( seq ) );
                                        url.put( urlString, idUrl );
                                    }
                                }
                            }

                            if ( isNeedInsertUrl ) {
                                SiteAccessUrlItemType item = new SiteAccessUrlItemType();
                                item.setIdSiteAccessUrl( idUrl );
                                item.setUrl( urlString );
                                try {
                                    InsertSiteAccessUrlItem.processData( db_, item );
                                    db_.commit();
                                }
                                catch( Exception e ) {
                                    if ( !( e instanceof SQLException ) || !db_.testExceptionIndexUniqueKey( e ) )
                                        throw e;

                                    synchronized (urlSync) {
                                        url.clear();
                                        url = null;
                                    }
                                }
                                synchronized (urlSync) {
                                    GetSiteAccessUrlItem.reinit();
                                    GetSiteAccessUrlFullList.reinit();
                                    isLoop = true;
                                }
                            }
                        }
                    }
                    stat.setIdSiteAccessUserAgent( idUserAgent );
                    stat.setIdSiteAccessUrl( idUrl );

                    stat.setAccessDate( new Timestamp( System.currentTimeMillis() ) );
                    Long idSite = SiteListSite.getIdSite( request.getServerName() );

                    if ( idSite == null )
                        isPrintData = true;

                    stat.setIdSite( idSite );

                    seq.setSequenceName( "SEQ_SITE_ACCESS_STAT" );
                    seq.setTableName( "SITE_ACCESS_STAT" );
                    seq.setColumnName( "ID_SITE_ACCESS_STAT" );
                    stat.setIdSiteAccessStat( new Long( db_.getSequenceNextValue( seq ) ) );
                    stat.setIp( request.getRemoteAddr() );

                    {
                        final int SIZE_REFER = 200;
                        String referer = Header.getReferer( request );
                        if ( referer == null )
                            referer = "";
                        int lenRefer = StringTools.lengthUTF( referer );
                        if ( lenRefer>SIZE_REFER ) {
                            lenRefer = SIZE_REFER;
                            stat.setIsReferTooBig( Boolean.TRUE );
                        } else
                            stat.setIsReferTooBig( Boolean.FALSE );

                        stat.setRefer( new String( StringTools.getBytesUTF( referer ), 0, lenRefer ) );
                    }
                    {
                        final int SIZE_PARAMETERS = 200;
                        String param = ( (HttpServletRequest)request ).getQueryString();
                        int lenParams = StringTools.lengthUTF( param );
                        if ( lenParams>SIZE_PARAMETERS ) {
                            lenParams = SIZE_PARAMETERS;
                            stat.setIsParamTooBig( Boolean.TRUE );
                        } else
                            stat.setIsParamTooBig( Boolean.FALSE );

                        stat.setParameters( new String( StringTools.getBytesUTF( param ), 0, lenParams ) );
                    }

                    InsertSiteAccessStatItem.processData( db_, stat );
                    db_.commit();
                }
                catch( Exception e ) {
                    log.error( "Error save request statistic", e );
                    isOutputData = true;
                }
            }
        }
        finally {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

        String s_ = "";
        if ( isPrintData || isOutputData || log.isDebugEnabled() ) {
            s_ = getDebugInfo( request, response );
        }
        if ( isOutputData || log.isDebugEnabled() ) {
            log.debug( s_ );
        } else if ( isPrintData ) {
            log.error( s_ );
        }

        try {
            // Pass control on to the next filter
            // !!!!!!!!!!!!!!!!! DO NOT COMMENT THIS LINE !!!!!!!!!!!!!!!!!!!!!!!!
            chain.doFilter( request, response );
            return;
        }
        catch( Throwable exc ) {
            try {
                log.error( "startInfo:\n"+startInfo );
                log.error( "endInfo:\n"+getDebugInfo( request, response ) );
            }
            catch( Exception e ) {
                log.error( "Nested Exception in getDebugInfo()", e );
            }
            final String es = "Exception call chain of filter";
            log.fatal( es, exc );
            throw new ServletException( es, exc );
        }
    }

    private static String getDebugInfo( ServletRequest request, ServletResponse response ) {
        String s_ = "";
        s_ += "\nRequest Received at "+
            ( new Timestamp( System.currentTimeMillis() ) )+"\n";
        s_ += " characterEncoding="+request.getCharacterEncoding()+"\n";
        s_ += "     contentLength="+request.getContentLength()+"\n";
        s_ += "       contentType="+request.getContentType()+"\n";
        s_ += "            locale="+request.getLocale()+"\n";

        String s = ( "           locales=" );
        Enumeration locales = request.getLocales();
        boolean first = true;
        while ( locales.hasMoreElements() )
        {
            Locale locale = (Locale)locales.nextElement();
            if ( first )
                first = false;
            else
                s += ( ", " );
            s += ( locale.toString() );
        }
        s_ += s+"\n";
        Enumeration names = request.getParameterNames();
        s = "";
        while ( names.hasMoreElements() )
        {
            String name = (String)names.nextElement();
            s = ( "         parameter="+name+"=" );
            String values[] = request.getParameterValues( name );
            for ( int i = 0; i<values.length; i++ )
            {
                if ( i>0 )
                    s += ( ", " );
                s += ( values[i] );
            }
            s_ += s+"\n";
        }
        s_ += "          protocol="+request.getProtocol()+"\n";
        s_ += "        remoteAddr="+request.getRemoteAddr()+"\n";
        s_ += "        remoteHost="+request.getRemoteHost()+"\n";
        s_ += "            scheme="+request.getScheme()+"\n";
        s_ += "        serverName="+request.getServerName()+"\n";
        s_ += "        serverPort="+request.getServerPort()+"\n";
        s_ += "          isSecure="+request.isSecure()+"\n";
        s_ += "\n";
        s_ += "     request class="+request.getClass().getName()+"\n";
        s_ += "    response class="+response.getClass().getName()+"\n";
        s_ += "\n";

        // Render the HTTP servlet request properties
        if ( request instanceof HttpServletRequest )
        {
            s_ += "---------------------------------------------"+"\n";
            HttpServletRequest hrequest = (HttpServletRequest)request;
            s_ += "       contextPath="+hrequest.getContextPath()+"\n";
            Cookie cookies[] = hrequest.getCookies();
            if ( cookies==null )
                cookies = new Cookie[0];
            for ( int i = 0; i<cookies.length; i++ )
            {
                s_ += "            cookie="+cookies[i].getName()+
                    "="+cookies[i].getValue()+"\n";
            }
            names = hrequest.getHeaderNames();
            while ( names.hasMoreElements() )
            {
                String name = (String)names.nextElement();
                String value = hrequest.getHeader( name );
                s_ += "            header="+name+"="+value+"\n";
            }
            names = hrequest.getAttributeNames();
            while ( names.hasMoreElements() )
            {
                String name = (String)names.nextElement();
                Object obj = hrequest.getAttribute( name );
                String value = obj.toString();
                s_ += "         attribute="+name+"="+value+", class: "+obj.getClass().getName()+"\n";
            }
            s_ += "            method="+hrequest.getMethod()+"\n";
            s_ += "          pathInfo="+hrequest.getPathInfo()+"\n";
            s_ += "       queryString="+hrequest.getQueryString()+"\n";
            s_ += "        remoteUser="+hrequest.getRemoteUser()+"\n";
            s_ += "requestedSessionId="+hrequest.getRequestedSessionId()+"\n";
            s_ += "        requestURI="+hrequest.getRequestURI()+"\n";
            s_ += "       servletPath="+hrequest.getServletPath()+"\n";
        }
        s_ += "============================================="+"\n";
        return s_;
    }


    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        this.filterConfig = filterConfig;
    }


    /**
     * Return a String representation of this object.
     */
    public String toString()
    {
        if ( filterConfig==null )
            return ( "RequestDumperFilter()" );
        StringBuffer sb = new StringBuffer( "RequestDumperFilter(" );
        sb.append( filterConfig );
        sb.append( ")" );
        return ( sb.toString() );
    }
}
