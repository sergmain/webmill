/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.news;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.tools.XmlTools;

import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthException;

import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.schema.core.MainUserMetadataItemType;
import org.riverock.webmill.container.core.InsertMainUserMetadataItem;
import org.riverock.webmill.container.core.UpdateMainUserMetadataItem;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.tools.PortalUserMetadata;
import org.riverock.webmill.container.portal.PortalInfo;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;

/**
 * @author SMaslyukov
 *         Date: 19.03.2005
 *         Time: 15:05:33
 *         $Id$
 */
public class NewsSubscribe implements PortletResultObject, PortletResultContent {
    private final static Log log = LogFactory.getLog( NewsSubscribe.class );

    public NewsSubscribe()
    {
    }

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
//    private ResourceBundle bundle = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
//        this.bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
    }

    public PortletResultContent getInstance( final Long id )
        throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( final String portletCode_ )
        throws PortletException {

        throw new PortletException("This portlet not support method getInstanceByCode()");
    }

    public PortletResultContent getInstance() throws PortletException {
        return this;
    }


    private static Object syncDebug = new Object();
    public byte[] getXml(String rootElement) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("start get XmlByte array, rootElement: " + rootElement );
        }


        byte b[] = null;
        try {
            b = XmlTools.getXml( null, rootElement );
        }
        catch(Exception e) {
            log.error("Error persists register object to XML", e);
            try{
                log.error("info Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
            }
            catch(Exception e2){
                log.error("Error get version of xerces "+e.getMessage());
            }
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("end get XmlByte array. length of array - " + b.length);

            final String testFile = SiteUtils.getTempDir()+File.separatorChar+"regiser-item.xml";
            log.debug("Start output test data to file " + testFile );
            synchronized(syncDebug){
                MainTools.writeToFile(testFile, b);
            }
            log.debug("end output data");
        }

        return b;
    }

    public byte[] getXml() throws Exception {
        return getXml( "NewsSubscribe" );
    }

    public byte[] getPlainHTML() throws Exception {
        StringBuffer out = new StringBuffer();
        DatabaseAdapter db_ = null;
        try {

            db_ = DatabaseAdapter.getInstance();

            String indexPage = PortletService.url(ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse, "" );

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();

            if ( userNotLogged(auth_, out ) ) {
                return prepareReturn( out );
            }

            if (!emailIsValid(auth_, out )) {
                setSubscribeStatus(renderRequest, false);
                return prepareReturn( out );
            }

            boolean isSubscribed = getSubscribeStatus( db_, auth_, renderRequest.getServerName() );
            printSubscribeForm( out, isSubscribed );
            printToIndexPage( out, indexPage );

            return prepareReturn( out );
        }
        catch (Exception e) {
            final String es = "Erorr processing news subscribe page";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

    }

    private boolean emailIsValid(AuthSession auth_, StringBuffer out) {
        if ( auth_.getUserInfo().getEmail()==null ) {
            out.append( "You must setup your e-mail address before change of status of subscription");

            return false;
        }

        return true;
    }

    private void printToIndexPage(StringBuffer out, String indexPage) {
        out
            .append("<p class\"webmill.to-homepage\">\n<a href=\"")
            .append(indexPage)
            .append("\" >To homepage.</a>\n</p>\n");
    }

    private void printSubscribeForm(StringBuffer out, boolean isSubscribed) {
        out
            .append("\n<form method=\"POST\" action=\"")
            .append(PortletService.ctx(renderRequest))
            .append("\" >\n")
            .append( ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, NewsSubscribePortlet.PORTLET_NAME ) )
            .append( ServletTools.getHiddenItem( NewsSubscribePortlet.CHANGE_STATUS_PARAM, NewsSubscribePortlet.CHANGE_STATUS_PARAM  ) )
            .append("\nChange your subscribtion status on news: \n")
            .append("<input type=\"checkbox\" name=\"")
            .append(NewsSubscribePortlet.CHECKBOX_NAME)
            .append("\" ")
            .append( isSubscribed?"checked":"")
            .append(">")
            .append("<input type=\"submit\" name=\"button\" value=\"")
            .append( "Change status" )
            .append( "\">\n</form>\n" );
    }

    private boolean getSubscribeStatus(DatabaseAdapter adapter, AuthSession auth_, String serverName) throws Exception {
        MainUserMetadataItemType meta = PortalUserMetadata.getMetadata( adapter, auth_.getUserLogin(), serverName, NewsSubscribePortlet.META_SUBSCRIBED_ON_NEWS);
        if (meta==null || meta.getIntValue()==null) {
            return false;
        }
        return meta.getIntValue()==1;
    }

    static void setSubscribeStatus(PortletRequest portletRequest, boolean isSubscribe) throws Exception {

        DatabaseAdapter db_ = null;
        try {

            db_ = DatabaseAdapter.getInstance();

            PortalInfo portalInfo = (PortalInfo)portletRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
            AuthSession auth_ = (AuthSession)portletRequest.getUserPrincipal();
            MainUserMetadataItemType meta = PortalUserMetadata.getMetadata( db_, auth_.getUserLogin(), portletRequest.getServerName(), NewsSubscribePortlet.META_SUBSCRIBED_ON_NEWS);

            if (meta==null) {
                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName( "SEQ_MAIN_USER_METADATA" );
                seq.setTableName( "MAIN_USER_METADATA" );
                seq.setColumnName( "ID_MAIN_USER_METADATA" );
                Long id = db_.getSequenceNextValue( seq );

                meta = new MainUserMetadataItemType();
                meta.setIdMainUserMetadata( id );
                meta.setIdUser( auth_.getUserInfo().getUserId() );
                meta.setIdSite( portalInfo.getSiteId() );
                meta.setMeta( NewsSubscribePortlet.META_SUBSCRIBED_ON_NEWS );
                meta.setIntValue( isSubscribe?1L:0L );
                InsertMainUserMetadataItem.process(db_, meta);
            }
            else {
                meta.setIntValue( isSubscribe?1L:0L );
                UpdateMainUserMetadataItem.process(db_, meta);
            }
            db_.commit();
        }
        catch (Exception e) {
            try {
                db_.rollback();
            } catch (SQLException e1) {
            }
            final String es = "Erorr set new status of subscription to news";
            log.error(es, e);
            throw new PortletException( es, e );
        }
        finally {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

    }

    private boolean userNotLogged(AuthSession auth_, StringBuffer out) throws AuthException {
        if ( auth_==null ) {
            out.append( "You must logging before subscribe to news on this site.");
            return true;
        }

        if ( !auth_.checkAccess( renderRequest.getServerName() ) ) {
            out.append( "You have not permision on this site. You must re login to subscribe to the news on this site.");
            return true;
        }

        return false;
    }

    private byte[] prepareReturn(StringBuffer out) throws ConfigException, UnsupportedEncodingException {
        return out.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }
}
