/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.portlet.news;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

/**
 * @author SMaslyukov
 *         Date: 19.03.2005
 *         Time: 15:05:33
 *         $Id$
 */
public class NewsSubscribe implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( NewsSubscribe.class );

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


    private final static Object syncDebug = new Object();
    public byte[] getXml(String rootElement) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("start get XmlByte array, rootElement: " + rootElement );
        }


        byte b[];
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
        StringBuilder out = new StringBuilder();
        DatabaseAdapter db_ = null;
        try {

            db_ = DatabaseAdapter.getInstance();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();

            if ( userNotLogged(auth_, out ) ) {
                return prepareReturn( out );
            }

            if (!emailIsValid(auth_, out )) {
                setSubscribeStatus(renderRequest, false);
                return prepareReturn( out );
            }

            boolean isSubscribed = getSubscribeStatus( auth_ );
            printSubscribeForm( out, isSubscribed );

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

    private boolean emailIsValid(AuthSession auth_, StringBuilder out) {
        if ( StringUtils.isBlank(auth_.getUserInfo().getEmail())) {
            out.append( "You must setup your e-mail address before change of status of subscription");

            return false;
        }

        return true;
    }

    private void printSubscribeForm(StringBuilder out, boolean isSubscribed) {
        out
            .append("\n<form method=\"POST\" action=\"")
            .append(renderResponse.createActionURL().toString())
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

    private boolean getSubscribeStatus(AuthSession auth_) {
        PortalDaoProvider portalDaoProvider = (PortalDaoProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        UserMetadataItem meta = portalDaoProvider.getPortalUserMetadataDao().getMetadata( auth_.getUserLogin(), siteId, NewsSubscribePortlet.META_SUBSCRIBED_ON_NEWS);
        if (meta==null || meta.getIntValue()==null) {
            return false;
        }
        return meta.getIntValue()==1;
    }

    static void setSubscribeStatus(PortletRequest portletRequest, boolean isSubscribe) throws Exception {

        try {

            Long siteId = new Long( portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

            AuthSession auth_ = (AuthSession)portletRequest.getUserPrincipal();
            PortalDaoProvider portalDaoProvider = (PortalDaoProvider)portletRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
            portalDaoProvider.getPortalUserMetadataDao().setMetadataIntValue(auth_.getUserLogin(), siteId, NewsSubscribePortlet.META_SUBSCRIBED_ON_NEWS, isSubscribe?1L:0L);
        }
        catch (Exception e) {
            final String es = "Erorr set new status of subscription to news";
            log.error(es, e);
            throw new PortletException( es, e );
        }
    }

    private boolean userNotLogged(AuthSession auth_, StringBuilder out) {
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

    private byte[] prepareReturn(StringBuilder out) throws ConfigException, UnsupportedEncodingException {
        return out.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
    }
}
