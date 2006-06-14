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
package org.riverock.portlet.member;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portlet.member.PortletGetList;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberApplicationType;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberModuleType;
import org.riverock.portlet.schema.portlet.menu_member.MenuMemberType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author Serge Maslyukov
 *
 * $Id$
 */
public final class MenuMember implements PortletResultObject, PortletGetList, PortletResultContent {
    private final static Logger log = Logger.getLogger( MenuMember.class );

    private MenuMemberType menu = new MenuMemberType();
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
    }

    protected void destroy() {
        menu = null;
        renderRequest = null;
        renderResponse = null;
    }

    public MenuMember() {
    }

    private String dynamicTemplate=null;

    public PortletResultContent getInstance() throws PortletException {
        PreparedStatement ps = null;
        ResultSet rset = null;

        if( log.isDebugEnabled() ) {
            log.debug( "Start getInstance()" );
            log.debug( "session ID status: " + renderRequest.isRequestedSessionIdValid() );
        }

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            AuthSession authSession = (AuthSession)this.renderRequest.getUserPrincipal();

            if( log.isDebugEnabled() )
                log.debug( "MenuMember authInfo: " + authSession );

            if( authSession == null )
                return this;

            PortalContext portalContext = renderRequest.getPortalContext();
            if( log.isDebugEnabled() ) {
                log.debug( "portalContext: " + portalContext );
            }
            String siteIdString = portalContext.getProperty( ContainerConstants.PORTAL_PROP_SITE_ID );
            if( log.isDebugEnabled() ) {
                log.debug( "siteId: " + siteIdString );
            }
            dynamicTemplate = getDynamicTemplate();

            String sql_ =
                "select a.user_login, f.code_arm, f.order_field, f.id_arm, f.name_arm " +
                "from   WM_AUTH_USER a, WM_AUTH_RELATE_ACCGROUP b, WM_AUTH_RELATE_RIGHT_ARM d, WM_AUTH_MODULE e, WM_AUTH_APPLICATION f " +
                "where  a.id_auth_user=b.id_auth_user and b.id_access_group = d.id_access_group and " +
                "       d.id_object_arm = e.id_object_arm and e.id_arm = f.id_arm and a.USER_LOGIN=? " +
                "union  " +
                "select a1.user_login, f1.code_arm, f1.order_field, f1.id_arm, f1.name_arm " +
                "from   WM_AUTH_USER a1, WM_AUTH_APPLICATION f1 " +
                "where  a1.is_root=1 and a1.USER_LOGIN=? ";

            ps = db_.prepareStatement( sql_ );

            ps.setString( 1, authSession.getUserLogin() );
            ps.setString( 2, authSession.getUserLogin() );

            rset = ps.executeQuery();
            int recordNumber = 0;
            ArrayList<MenuMemberApplicationType> list = new ArrayList<MenuMemberApplicationType>();
            while( rset.next() ) {
                MenuMemberApplicationType appl = getApplication( rset, recordNumber++, db_, authSession.getUserLogin() );
                if( appl != null )
                    list.add( appl );
            }
            Collections.sort( list, new MenuMemberApplicationComparator() );
            menu.setMenuMemberApplicationAsReference( list );

            if( log.isDebugEnabled() )
                log.debug( "recordNumber: " + recordNumber );
        }
        catch( Throwable e ) {
            String es = "Error get list of member application";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rset, ps );
        }
        return this;
    }

    public PortletResultContent getInstance( Long id ) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( String portletCode_ ) throws PortletException {
        return getInstance();
    }

    public byte[] getPlainHTML() {
        return null;
    }

    public byte[] getXml( String rootElement ) throws Exception {
        return XmlTools.getXml( menu, rootElement );
    }

    public byte[] getXml() throws Exception {
        return getXml( "MenuMember" );
    }

    public List<ClassQueryItem> getList( Long idSiteCtxLangCatalog, Long idContext ) {
        return null;
    }

    private final static String sql_ =
        "select distinct z.CODE_ARM,a.CODE_OBJECT_ARM,  a.NAME_OBJECT_ARM, " +
        "       a.URL, a.order_field  " +
        "from   WM_AUTH_MODULE a," +
        "(" +
        "select a.user_login, " +
        "       f.code_arm, " +
        "       d.id_object_arm, " +
        "	e.id_arm " +
        "from   WM_AUTH_USER a, " +
        "       WM_AUTH_RELATE_ACCGROUP b, " +
        "       WM_AUTH_RELATE_RIGHT_ARM d, " +
        "       WM_AUTH_MODULE e, " +
        "       WM_AUTH_APPLICATION f " +
        "where  a.id_auth_user=b.id_auth_user and " +
        "       b.id_access_group = d.id_access_group and " +
        "       d.id_object_arm = e.id_object_arm and " +
        "       e.id_arm = f.id_arm " +
        "union " +
        "select a1.user_login, f1.code_arm, d1.id_object_arm, f1.id_arm   " +
        "from   WM_AUTH_USER a1, WM_AUTH_MODULE d1,  WM_AUTH_APPLICATION f1 " +
        "where  a1.is_root=1 and d1.id_arm = f1.id_arm " +
        ") z " +
        "where  z.user_login=? and " +
        "       a.id_object_arm = z.id_object_arm and " +
        "       z.id_arm=? and a.url is not null ";

    String sql_mysql =
        "select distinct f.CODE_ARM, a1.CODE_OBJECT_ARM,  a1.NAME_OBJECT_ARM, a1.URL, a1.order_field " +
        "from   WM_AUTH_MODULE a1, WM_AUTH_USER a, WM_AUTH_RELATE_ACCGROUP b, WM_AUTH_RELATE_RIGHT_ARM d, WM_AUTH_MODULE e, WM_AUTH_APPLICATION f " +
        "where  a.id_auth_user=b.id_auth_user and b.id_access_group = d.id_access_group and d.id_object_arm = e.id_object_arm and " +
        "       e.id_arm = f.id_arm and a.user_login=? and a1.id_object_arm = d.id_object_arm and e.id_arm=? and a1.url is not null " +
        "union " +
        "select distinct f01.CODE_ARM, a1.CODE_OBJECT_ARM,  a1.NAME_OBJECT_ARM, a1.url, a1.order_field " +
        "from   WM_AUTH_MODULE a1, WM_AUTH_USER a01, WM_AUTH_MODULE d01,  WM_AUTH_APPLICATION f01 " +
        "where  a01.is_root=1 and d01.id_arm = f01.id_arm and a01.user_login=? and a1.id_object_arm = d01.id_object_arm and " +
        "       f01.id_arm=? and a1.url is not null ";


    private MenuMemberApplicationType getApplication( ResultSet rs, int recordNumber_, DatabaseAdapter db_, String userLogin )
        throws PortletException {
        if( userLogin == null )
            return null;

        PreparedStatement ps = null;
        ResultSet rset = null;

        MenuMemberApplicationType appl = new MenuMemberApplicationType();
        try {
            appl.setApplicationName( RsetTools.getString( rs, "NAME_ARM" ) );
            appl.setOrderField( RsetTools.getInt( rs, "ORDER_FIELD" ) );
            appl.setApplicationId( RsetTools.getLong( rs, "ID_ARM" ) );
            appl.setApplicationRecordNumber( recordNumber_ );

            ps = makeStatement( db_, userLogin, appl.getApplicationId() );

            rset = ps.executeQuery();
            int moduleRecordNumber = 0;
            ArrayList<MenuMemberModuleType> list = new ArrayList<MenuMemberModuleType>();
            while( rset.next() ) {
                MenuMemberModuleType mod = getModule( rset );
                if( mod != null ) {
                    mod.setModuleRecordNumber( moduleRecordNumber++ );
                    mod.setApplRecordNumber( appl.getApplicationRecordNumber() );
                    list.add( mod );
                }
            }
            Collections.sort( list, new MenuMemberModuleComparator() );
            appl.setMenuMemberModuleAsReference( list );
            return appl;
        }
        catch( Throwable e1 ) {
            final String es = "Error get member application";
            log.error( es, e1 );
            throw new PortletException( es, e1 );
        }
        finally {
            DatabaseManager.close( rset, ps );
        }

    }

    private PreparedStatement makeStatement( DatabaseAdapter db_, String userLogin, Long applicationId )
        throws SQLException {

        PreparedStatement ps;
        switch( db_.getFamaly() ) {
            case DatabaseManager.MYSQL_FAMALY:
                ps = db_.prepareStatement( sql_mysql );

                ps.setString( 1, userLogin );
                RsetTools.setLong( ps, 2, applicationId );
                ps.setString( 3, userLogin );
                RsetTools.setLong( ps, 4, applicationId );
                break;
            default:
                ps = db_.prepareStatement( sql_ );

                ps.setString( 1, userLogin );
                RsetTools.setLong( ps, 2, applicationId );
                break;
        }

        return ps;
    }

    private MenuMemberModuleType getModule( ResultSet rs )
        throws Exception {
        if( rs == null )
            return null;

        MenuMemberModuleType mod = new MenuMemberModuleType();
        try {
            mod.setModuleName( RsetTools.getString( rs, "NAME_OBJECT_ARM" ) );
            mod.setOrderField( RsetTools.getInt( rs, "ORDER_FIELD" ) );
            mod.setModuleUrl( RsetTools.getString( rs, "URL" ) );

            try {

                int idx = mod.getModuleUrl().indexOf('?');
                String moduleUrl =
                    PortletService.url( MemberConstants.CTX_TYPE_MEMBER, renderRequest, renderResponse, dynamicTemplate ) + '&' +
                        (idx!=-1?mod.getModuleUrl().substring(idx + 1):mod.getModuleUrl());

                if( log.isDebugEnabled() ) {
                    log.debug( "PortletParam  nameTemplate - " + dynamicTemplate );
                    log.debug( "Module url - " + moduleUrl );
                }
                mod.setModuleUrl( moduleUrl );
            }
            catch( Exception e ) {
                log.error( "Error getModuleUrl ", e );
                throw e;
            }

            return mod;
        }
        catch( Exception e ) {
            log.error( "Error get member module", e );
            throw e;
        }
    }

    private String getDynamicTemplate() {

        if( log.isDebugEnabled() ) {
            log.debug( "Looking template for locale " + renderRequest.getLocale().toString() );
        }
        PortalDaoProvider daoProvider = (PortalDaoProvider)renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        Long siteId = new Long(renderRequest.getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
        SiteLanguage siteLanguage = daoProvider.getPortalSiteLanguageDao().getSiteLanguage(
            siteId,
            renderRequest.getLocale().toString()
        );
        if (siteLanguage==null) {
            log.warn( "default dynamic template for Locale " + renderRequest.getLocale().toString() + " and siteId " +siteId+" not found" );
            return null;
        }
        Template template = daoProvider.getPortalTemplateDao().getDefaultDynamicTemplate(siteLanguage.getSiteLanguageId());

        if( template==null ) {
            log.warn( "default dynamic template for Locale " + renderRequest.getLocale().toString() + " and siteId " +siteId+" not found" );
            return null;
        }
        return template.getTemplateName();
    }

    private final static class MenuMemberApplicationComparator implements Comparator<MenuMemberApplicationType> {
        public int compare( MenuMemberApplicationType o1, MenuMemberApplicationType o2 ) {

            if( o1 == null && o2 == null )
                return 0;
            if( o1 == null )
                return 1;
            if( o2 == null )
                return -1;

            if( o1.getOrderField() == null && o2.getOrderField() == null )
                return 0;

            if( o1.getOrderField() != null && o2.getOrderField() == null )
                return -1;

            if( o1.getOrderField() == null && o2.getOrderField() != null )
                return 1;

            if( o1.getOrderField().equals( o2.getOrderField() ) )
                return o1.getApplicationName().compareTo( o2.getApplicationName() );
            else
                return o1.getOrderField().compareTo( o2.getOrderField() );
        }
    }

    private final static class MenuMemberModuleComparator implements Comparator<MenuMemberModuleType> {
        public int compare( final MenuMemberModuleType o1, final MenuMemberModuleType o2 ) {

            if( o1 == null && o2 == null )
                return 0;
            if( o1 == null )
                return 1;
            if( o2 == null )
                return -1;

            if( o1.getOrderField() == null && o2.getOrderField() == null )
                return 0;

            if( o1.getOrderField() == null )
                return 1;

            if( o2.getOrderField() == null )
                return -1;

            return o1.getOrderField().compareTo( o2.getOrderField() );
        }
    }
}