package org.riverock.portlet.auth.bean;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.portlet.core.GetWmListCompanyItem;
import org.riverock.portlet.core.GetWmListGroupCompanyItem;
import org.riverock.portlet.core.GetWmListHoldingItem;
import org.riverock.portlet.schema.core.WmListCompanyItemType;
import org.riverock.portlet.schema.core.WmListGroupCompanyItemType;
import org.riverock.portlet.schema.core.WmListHoldingItemType;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.InternalAuthProviderTools;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.sso.core.GetWmAuthAccessGroupItem;
import org.riverock.sso.schema.core.WmAuthAccessGroupItemType;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:42:36
 *         $Id$
 */
public class UserBeanManager implements Serializable {
    private final static Logger log = Logger.getLogger( UserBeanManager.class );
    private static final long serialVersionUID = 2043005501L;

    private ModuleUser moduleUser = null;
    private List<CompanyBean> companyBeans = null;

    public ModuleUser getModuleUser() {
        return moduleUser;
    }

    public void setModuleUser( ModuleUser moduleUser ) {
        this.moduleUser = moduleUser;
    }

    public List<CompanyBean> getCompanyBeans() {
        if( companyBeans == null ) {
            companyBeans = initCompanyBeans();
        }
        return companyBeans;
    }

    public void reinitCompanyBeans() {
        companyBeans = initCompanyBeans();
    }

    private List<CompanyBean> initCompanyBeans() {
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CompanyBean> list = new ArrayList<CompanyBean>();
        String userLogin = moduleUser.getUserLogin();

        try {
            db_ = DatabaseAdapter.getInstance();

            String sql_ = null;
            switch( db_.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    sql_ =
                        "select c.id_firm, c.short_name " +
                        "from   WM_AUTH_USER a, WM_LIST_USER b, WM_LIST_COMPANY c " +
                        "where  a.id_user = b.id_user and " +
                        "       a.id_firm = c.id_firm and " +
                        "       b.ID_FIRM in ( " + AuthHelper.getGrantedCompanyId( db_, userLogin ) + " ) " +
                        "group by c.id_firm, c.short_name " +
                        "order by c.short_name asc, c.id_firm asc ";

                    ps = db_.prepareStatement( sql_ );
                    break;
                default:
                    sql_ =
                        "select c.id_firm, c.short_name " +
                        "from    WM_AUTH_USER a, WM_LIST_USER b, WM_LIST_COMPANY c " +
                        "where   a.id_user = b.id_user and " +
                        "        a.id_firm = c.id_firm and " +
                        "        b.ID_FIRM in " +
                        "( " +
                        "            select  a01.id_firm " +
                        "            from    WM_AUTH_USER a01 " +
                        "            where   a01.is_use_current_firm = 1 " +
                        "                    and a01.user_login = ? " +
                        "            union " +
                        "            select  d02.id_firm " +
                        "            from    WM_AUTH_USER a02, WM_LIST_R_GR_COMP_COMP d02 " +
                        "            where   a02.is_service = 1 and a02.id_service = d02.id_service " +
                        "                    and a02.user_login = ? " +
                        "            union " +
                        "            select  e03.id_firm " +
                        "            from    WM_AUTH_USER a03, WM_LIST_R_HOLDING_GR_COMPANY d03, WM_LIST_R_GR_COMP_COMP e03 " +
                        "            where   a03.is_road = 1 and a03.id_road = d03.id_road and d03.id_service = e03.id_service " +
                        "                    and a03.user_login = ? " +
                        "            union " +
                        "            select  b04.id_firm " +
                        "            from    WM_AUTH_USER a04, WM_LIST_COMPANY b04 " +
                        "            where   a04.is_root = 1 and a04.user_login = ? " +
                        ") " +
                        "group by c.id_firm, c.short_name " +
                        "order by c.short_name asc, c.id_firm asc ";

                    ps = db_.prepareStatement( sql_ );
                    ps.setString( 1, userLogin );
                    ps.setString( 2, userLogin );
                    ps.setString( 3, userLogin );
                    ps.setString( 4, userLogin );

                    break;
            }
            rs = ps.executeQuery();

            while( rs.next() ) {

                CompanyBean companyBean = new CompanyBean();
                companyBean.setCompanyId( RsetTools.getLong( rs, "ID_FIRM" ) );
                companyBean.setCompanyName( companyBean.getCompanyId() + ", " +
                    RsetTools.getString( rs, "short_name", "<not defined>" ) );

                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                String sql1 =
                    "select a.id_auth_user, a.id_user, a.user_login, a.user_password, " +
                    "       a.is_service, a.is_road, a.is_use_current_firm, a.is_root, " +
                    "       a.id_road, a.id_service, a.id_firm, " +
                    "       b.LAST_NAME, b.FIRST_NAME, b.MIDDLE_NAME " +
                    "from   WM_AUTH_USER a, WM_LIST_USER b " +
                    "where  a.id_user = b.id_user and " +
                    "       a.id_firm = ? " +
                    "order by b.LAST_NAME asc, b.FIRST_NAME asc, b.MIDDLE_NAME asc ";

                try {

                    ps1 = db_.prepareStatement( sql1 );
                    ps1.setLong( 1, companyBean.getCompanyId() );
                    rs1 = ps1.executeQuery();

                    while( rs1.next() ) {

                        UserBean userBean = new UserBean();
                        userBean.setUserName( StringTools.getUserName( RsetTools.getString( rs1, "LAST_NAME" ),
                            RsetTools.getString( rs1, "FIRST_NAME" ),
                            RsetTools.getString( rs1, "MIDDLE_NAME" ) ) );
                        userBean.setUserLogin( RsetTools.getString( rs1, "user_login", null ) );
                        userBean.setUserPassword( RsetTools.getString( rs1, "user_password", null ) );
                        userBean.setCompany( RsetTools.getInt( rs1, "is_use_current_firm", 0 ) == 1 );
                        userBean.setGroupCompany( RsetTools.getInt( rs1, "is_service", 0 ) == 1 );
                        userBean.setHolding( RsetTools.getInt( rs1, "is_road", 0 ) == 1 );
                        userBean.setRoot( RsetTools.getInt( rs1, "is_root", 0 ) == 1 );
                        userBean.setAuthUserId( RsetTools.getLong( rs1, "id_auth_user" ) );
                        userBean.setCompanyId( RsetTools.getLong( rs1, "id_firm" ) );
                        userBean.setGroupCompanyId( RsetTools.getLong( rs1, "id_service" ) );
                        userBean.setHoldingId( RsetTools.getLong( rs1, "id_road" ) );

                        if( userBean.getCompanyId() != null ) {
                            WmListCompanyItemType companyItem = GetWmListCompanyItem.getInstance( db_, userBean.getCompanyId() ).item;
                            userBean.setCompanyName( companyItem.getShortName() );
                        }
                        if( userBean.getGroupCompanyId() != null ) {
                            WmListGroupCompanyItemType groupCompanyItem = GetWmListGroupCompanyItem.getInstance( db_, userBean.getGroupCompanyId() ).item;
                            userBean.setGroupCompanyName( groupCompanyItem.getShortNameService() );
                        }
                        if( userBean.getHoldingId() != null ) {
                            WmListHoldingItemType holdingItem = GetWmListHoldingItem.getInstance( db_, userBean.getHoldingId() ).item;
                            userBean.setHoldingName( holdingItem.getNameRoad() );
                        }
                        userBean.setAllRoles( getRoles( db_, userBean.getAuthUserId() ) );

                        companyBean.addUserBeans( userBean );
                    }
                }
                finally {
                    DatabaseManager.close( rs1, ps1 );
                    rs1 = null;
                    ps1 = null;
                }
                list.add( companyBean );
            }
            return list;
        }
        catch( Exception e ) {
            String es = "Exception in BindIndex";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }

    private List<RoleBean> getRoles( DatabaseAdapter db_, Long authUserId ) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(
                "select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP " +
                "from   WM_AUTH_ACCESS_GROUP c, WM_AUTH_RELATE_ACCGROUP b " +
                "where  b.ID_AUTH_USER=? and b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP "
            );

            ps.setLong( 1, authUserId );
            rs = ps.executeQuery();

            List<RoleBean> roles = new ArrayList<RoleBean>();
            while( rs.next() ) {
                RoleBean role = new RoleBean();

                role.setName( RsetTools.getString( rs, "NAME_ACCESS_GROUP" ) );
                role.setRoleId( RsetTools.getLong( rs, "ID_ACCESS_GROUP" ) );

                roles.add( role );
            }
            return roles;
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    private void processDeleteUserRoles( DatabaseAdapter db_, Long authUserId ) throws Exception {
        PreparedStatement ps = null;
        try {
            ps = db_.prepareStatement(
                "delete from WM_AUTH_RELATE_ACCGROUP " +
                "where  ID_AUTH_USER=? "
            );

            ps.setLong( 1, authUserId );
            ps.execute();
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    private void processDeletedRoles( DatabaseAdapter db_, UserBean userBean ) throws Exception {
        PreparedStatement ps = null;
        try {
		log.info(
		"Start delete roles for authUserId: " + userBean.getAuthUserId() +
		", roles list: " + userBean.getAllRoles()
		);

            Iterator<RoleBean> iterator = userBean.getAllRoles().iterator();
            while( iterator.hasNext() ) {
                RoleBean roleBean = iterator.next();

		log.info("role: " + roleBean);

                if (!roleBean.isDelete()) {
                    continue;
                }

                ps = db_.prepareStatement(
                    "delete from WM_AUTH_RELATE_ACCGROUP " +
                    "where  ID_AUTH_USER=? and ID_ACCESS_GROUP=? "
                );

                ps.setLong( 1, userBean.getAuthUserId() );
                ps.setLong( 2, roleBean.getRoleId() );
                ps.execute();
                ps.close();
                ps = null;
            }
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    private void processNewRoles( DatabaseAdapter db, UserBean userBean ) throws Exception {
        PreparedStatement ps = null;
        try {
            Iterator<RoleBean> iterator = userBean.getAllRoles().iterator();
            while( iterator.hasNext() ) {
                RoleBean roleBean = iterator.next();

                if (!roleBean.isNew() || roleBean.isDelete()) {
                    continue;
                }

                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName( "seq_WM_AUTH_RELATE_ACCGROUP" );
                seq.setTableName( "WM_AUTH_RELATE_ACCGROUP" );
                seq.setColumnName( "ID_RELATE_ACCGROUP" );
                Long id = db.getSequenceNextValue( seq );

                ps = db.prepareStatement(
                    "insert into WM_AUTH_RELATE_ACCGROUP " +
                    "(ID_RELATE_ACCGROUP, ID_ACCESS_GROUP, ID_AUTH_USER ) " +
                    "values" +
                    "(?, ?, ? ) "
                );

                ps.setLong( 1, id );
                ps.setLong( 2, roleBean.getRoleId() );
                ps.setLong( 3, userBean.getAuthUserId() );
                ps.execute();
                ps.close();
                ps = null;
            }
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public void processDeleteUser( UserBean userBean ) {
        if( userBean == null ) {
            throw new IllegalStateException( "UserBean is null" );
        }

        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            Long authUserId = userBean.getAuthUserId();

            AuthInfo authInfo = moduleUser.getAuthInfo();
            AuthInfo authInfoUser = AuthInfo.getInstance( db, authUserId );

            boolean isRigthRelate = false;

            if ( authInfoUser != null && authInfo != null ) {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser( db,
                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID() );
            }

            if ( isRigthRelate ) {

                processDeleteUserRoles( db, authUserId );

                switch( db.getFamaly() ) {
                    case DatabaseManager.MYSQL_FAMALY:
                        ps = db.prepareStatement( "delete from WM_AUTH_USER where id_auth_user=? " +
                            "and ID_FIRM in " +
                            "(" + AuthHelper.getGrantedCompanyId( db, moduleUser.getUserLogin() ) + ")" );

                        RsetTools.setLong( ps, 1, authUserId );
                        break;
                    default:
                        ps = db.prepareStatement( "delete from WM_AUTH_USER where id_auth_user=? " +
                            "and ID_FIRM in " +
                            "(select z.ID_FIRM from v$_read_list_firm z where z.user_login = ? )" );

                        RsetTools.setLong( ps, 1, authUserId );
                        ps.setString( 2, moduleUser.getUserLogin() );

                        break;
                }

                int i1 = ps.executeUpdate();

                if ( log.isDebugEnabled() )
                    log.debug( "Count of deleted records - " + i1 );

                db.commit();
            }
        }
        catch( Exception e ) {
            try {
                if ( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error commit delete bind";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, ps );
            ps = null;
            db = null;
        }
    }

    public void processSaveUser( UserBean userBean ) {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        try {

            db = DatabaseAdapter.getInstance();

		
            Long id_auth_user = userBean.getAuthUserId();
            if( id_auth_user == null )
                throw new IllegalArgumentException( "id_auth_user not initialized" );

            AuthInfo authInfo = moduleUser.getAuthInfo();
            AuthInfo authInfoUser = AuthInfo.getInstance( db, id_auth_user );

            boolean isRigthRelate = false;

            if( authInfoUser != null && authInfo != null ) {
                isRigthRelate =
                    InternalAuthProviderTools.checkRigthOnUser( db,
                        authInfoUser.getAuthUserID(), authInfo.getAuthUserID() );
            }

            Long companyId = null;
            Long groupCompanyId = null;
            Long holdingId = null;

            if( isRigthRelate ) {

                processDeletedRoles( db, userBean );
                processNewRoles( db, userBean );

                companyId = InternalAuthProviderTools.initIdFirm( db, userBean.getCompanyId(), authInfo.getUserLogin() );
                groupCompanyId = InternalAuthProviderTools.initIdService( db, userBean.getGroupCompanyId(), authInfo.getUserLogin() );
                holdingId = InternalAuthProviderTools.initIdRoad( db, userBean.getHoldingId(), authInfo.getUserLogin() );

                if( log.isDebugEnabled() ) {
                    log.debug( "companyId " + companyId );
                    log.debug( "groupCompanyId " + groupCompanyId );
                    log.debug( "holdingId " + holdingId );

                    log.debug( "is_service " + ( authInfo.getService() == 1 ?
                        userBean.isGroupCompany() :
                        false
                        ) );
                    log.debug( "is_road " + ( authInfo.getRoad() == 1 ?
                        userBean.isHolding() :
                        false
                        ) );
                    log.debug( "is_use_current_firm " + ( authInfo.getUseCurrentFirm() == 1 ?
                        userBean.isCompany() :
                        false
                        ) );
                    log.debug( "id_auth_user " + id_auth_user );
                    log.debug( "auth_.getUserLogin() " + moduleUser.getUserLogin() );
                }

                switch( db.getFamaly() ) {
                    case DatabaseManager.MYSQL_FAMALY:
                        ps = db.prepareStatement( "UPDATE WM_AUTH_USER " +
                            "SET " +
                            "	user_login = ?, " +
                            "	user_password = ?, " +
                            "	is_service = ?, " +
                            "	is_road = ?, " +
                            "	is_use_current_firm = ?, " +
                            "	ID_FIRM = ?, " +
                            "	id_service = ?, " +
                            "	id_road = ? " +
                            "WHERE id_auth_user=? and " +
                            "ID_FIRM  in (" + AuthHelper.getGrantedCompanyId( db, moduleUser.getUserLogin() ) + ") " );

                        ps.setString( 1, userBean.getUserLogin() );
                        ps.setString( 2, userBean.getUserPassword() );

                        ps.setInt( 3,
                            authInfo.getService() == 1
                            ? userBean.isGroupCompany() ? 1 : 0
                            : 0 );
                        ps.setInt( 4,
                            authInfo.getRoad() == 1
                            ? userBean.isHolding() ? 1 : 0
                            : 0 );
                        ps.setInt( 5,
                            authInfo.getUseCurrentFirm() == 1
                            ? userBean.isCompany() ? 1 : 0
                            : 0 );

                        if( companyId != null )
                            RsetTools.setLong( ps, 6, companyId );
                        else
                            ps.setNull( 6, Types.INTEGER );

                        if( groupCompanyId != null )
                            RsetTools.setLong( ps, 7, groupCompanyId );
                        else
                            ps.setNull( 7, Types.INTEGER );

                        if( holdingId != null )
                            RsetTools.setLong( ps, 8, holdingId );
                        else
                            ps.setNull( 8, Types.INTEGER );

                        RsetTools.setLong( ps, 9, id_auth_user );

                        break;
                    default:
                        ps = db.prepareStatement( "UPDATE WM_AUTH_USER " +
                            "SET " +
                            "	user_login = ?, " +
                            "	user_password = ?, " +
                            "	is_service = ?, " +
                            "	is_road = ?, " +
                            "	is_use_current_firm = ?, " +
                            "	ID_FIRM = ?, " +
                            "	id_service = ?, " +
                            "	id_road = ? " +
                            "WHERE id_auth_user=? and ID_FIRM in " +
                            "(select z.ID_FIRM from v$_read_list_firm z where z.user_login = ? )" );

                        ps.setString( 1, userBean.getUserLogin() );
                        ps.setString( 2, userBean.getUserPassword() );

                        ps.setInt( 3,
                            authInfo.getService() == 1
                            ? userBean.isGroupCompany() ? 1 : 0
                            : 0 );
                        ps.setInt( 4,
                            authInfo.getRoad() == 1
                            ? userBean.isHolding() ? 1 : 0
                            : 0 );
                        ps.setInt( 5,
                            authInfo.getUseCurrentFirm() == 1
                            ? userBean.isCompany() ? 1 : 0
                            : 0 );

                        if( companyId != null )
                            RsetTools.setLong( ps, 6, companyId );
                        else
                            ps.setNull( 6, Types.INTEGER );

                        if( groupCompanyId != null )
                            RsetTools.setLong( ps, 7, groupCompanyId );
                        else
                            ps.setNull( 7, Types.INTEGER );

                        if( holdingId != null )
                            RsetTools.setLong( ps, 8, holdingId );
                        else
                            ps.setNull( 8, Types.INTEGER );

                        RsetTools.setLong( ps, 9, id_auth_user );
                        ps.setString( 10, moduleUser.getUserLogin() );

                        break;
                }

                int i1 = ps.executeUpdate();

                if( log.isDebugEnabled() )
                    log.debug( "Count of updated records - " + i1 );

                db.commit();
            }
        }
        catch( Exception e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e01 ) {
            }

            final String es = "Error change user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, ps );
            db = null;
            ps = null;
        }
    }

    public void processAddUser( UserBean userBean ) {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            Long id_user = userBean.getUserId();
            if( id_user == null )
                throw new IllegalArgumentException( "id_user not initialized" );

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_USER" );
            seq.setTableName( "WM_AUTH_USER" );
            seq.setColumnName( "ID_AUTH_USER" );
            Long id = db.getSequenceNextValue( seq );

            ps = db.prepareStatement( "insert into WM_AUTH_USER " +
                "( ID_AUTH_USER, ID_FIRM, ID_SERVICE, ID_ROAD, " +
                "  ID_USER, USER_LOGIN, USER_PASSWORD, " +
                "IS_USE_CURRENT_FIRM, IS_SERVICE, IS_ROAD " +
                ") values (" +
                "?, " + // PK
                "?, " + // b1.companyId, " +
                "?, " + // b2.id_service, " +
                "?, " + //b3.id_road, "+
                "?, ?, ?, ?, ?, ? " +
                ")" );

            Long companyId = null;
            Long groupCompanyId = null;
            Long holdingId = null;

            companyId = InternalAuthProviderTools.initIdFirm( db, userBean.getCompanyId(), moduleUser.getUserLogin() );
            groupCompanyId = InternalAuthProviderTools.initIdService( db, userBean.getGroupCompanyId(), moduleUser.getUserLogin() );
            holdingId = InternalAuthProviderTools.initIdRoad( db, userBean.getHoldingId(), moduleUser.getUserLogin() );

            if( log.isDebugEnabled() ) {
                log.debug( "companyId " + companyId );
                log.debug( "groupCompanyId " + groupCompanyId );
                log.debug( "holdingId " + holdingId );
            }

            RsetTools.setLong( ps, 1, id );
            if( companyId != null )
                RsetTools.setLong( ps, 2, companyId );
            else
                ps.setNull( 2, Types.INTEGER );

            if( groupCompanyId != null )
                RsetTools.setLong( ps, 3, groupCompanyId );
            else
                ps.setNull( 3, Types.INTEGER );

            if( holdingId != null )
                RsetTools.setLong( ps, 4, holdingId );
            else
                ps.setNull( 4, Types.INTEGER );


            RsetTools.setLong( ps, 5, id_user );
            ps.setString( 6, userBean.getUserLogin() );
            ps.setString( 7, userBean.getUserPassword() );

            ps.setInt( 8,
                moduleUser.isCompany()
                ? userBean.isCompany() ? 1 : 0
                : 0 );
            ps.setInt( 9,
                moduleUser.isGroupCompany()
                ? userBean.isGroupCompany() ? 1 : 0
                : 0 );
            ps.setInt( 10,
                moduleUser.isHolding()
                ? userBean.isHolding() ? 1 : 0
                : 0 );
            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

            db.commit();
            return;
        }
        catch( Exception e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
            }

            final String es = "Error add user auth";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, ps );
            ps = null;
            db = null;
        }
    }

// List's methods

    public List<RoleBean> getRoleList() {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
                    ps = db.prepareStatement( 
			"select  ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                        "from    WM_AUTH_ACCESS_GROUP "
		);

	    rs = ps.executeQuery();
            List<RoleBean> list = new ArrayList<RoleBean>();
            while( rs.next() ) {
                RoleBean bean = new RoleBean();
                Long id = RsetTools.getLong( rs, "ID_ACCESS_GROUP" );
                String name = RsetTools.getString( rs, "NAME_ACCESS_GROUP" );

                if( name != null && id != null ) {
                    bean.setName( name );
                    bean.setRoleId( id );
                    list.add( bean );
                }
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<CompanyItemBean> getCompanyList() {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:

                    String sql_ =
                        "select b.ID_FIRM, b.full_name NAME_FIRM " +
                        "from   WM_LIST_COMPANY b " +
                        "where  b.ID_FIRM in (" + AuthHelper.getGrantedCompanyId( db, moduleUser.getUserLogin() ) + ") and b.is_deleted=0 " +
                        "order  by b.ID_FIRM ASC ";

                    if( log.isDebugEnabled() )
                        log.debug( "Firm list sql:\n" + sql_ );

                    ps = db.prepareStatement( sql_ );
                    break;
                default:
                    ps = db.prepareStatement( "select b.ID_FIRM, B.FULL_NAME NAME_FIRM " +
                        "from   v$_read_list_firm a, WM_LIST_COMPANY b " +
                        "where  a.ID_FIRM = b.ID_FIRM and b.is_deleted=0 and a.user_login=? " +
                        "order  by b.ID_FIRM ASC " );
                    ps.setString( 1, moduleUser.getUserLogin() );
                    break;
            }
	    rs = ps.executeQuery();
            List<CompanyItemBean> list = new ArrayList<CompanyItemBean>();
            while( rs.next() ) {
                CompanyItemBean bean = new CompanyItemBean();
                Long id = RsetTools.getLong( rs, "ID_FIRM" );
                String name = RsetTools.getString( rs, "NAME_FIRM" );

                if( name != null && id != null ) {
                    bean.setName( name );
                    bean.setId( id );
                    list.add( bean );
                }
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<GroupCompanyItemBean> getGroupCompanyList() {
        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:

                    ps = db.prepareStatement( "select  a.id_service, full_name_service " +
                        "from    WM_LIST_GROUP_COMPANY a " +
                        "where   a.id_service in ( " + AuthHelper.getGrantedGroupCompanyId( db, moduleUser.getUserLogin() ) + ")" +
                        "order   by id_service ASC " );
                    break;
                default:
                    ps = db.prepareStatement( "select id_service, full_name_service " +
                        "from   V_AUTH_RELATE_SERVICE " +
                        "where  user_login=? " +
                        "order  by id_service ASC " );
                    ps.setString( 1, moduleUser.getUserLogin() );
                    break;
            }
	    rs = ps.executeQuery();
            List<GroupCompanyItemBean> list = new ArrayList<GroupCompanyItemBean>();
            while( rs.next() ) {
                GroupCompanyItemBean bean = new GroupCompanyItemBean();
                Long id = RsetTools.getLong( rs, "id_service" );
                String name = RsetTools.getString( rs, "full_name_service" );

                if( name != null && id != null ) {
                    bean.setName( name );
                    bean.setId( id );
                    list.add( bean );
                }
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<HoldingItemBean> getHoldingList() {

        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:

                    ps = db.prepareStatement( "select  a.id_road, full_name_road " +
                        "from    WM_LIST_HOLDING a " +
                        "where   a.id_road in ( " + AuthHelper.getGrantedHoldingId( db, moduleUser.getUserLogin() ) + ")" +
                        "order   by id_road ASC " );
                    break;
                default:
                    ps = db.prepareStatement( "select id_road, full_name_road " +
                        "from   V_AUTH_RELATE_road " +
                        "where  user_login=? " +
                        "order  by id_road ASC " );
                    ps.setString( 1, moduleUser.getUserLogin() );
                    break;
            }
	    rs = ps.executeQuery();
            List<HoldingItemBean> list = new ArrayList<HoldingItemBean>();
            while( rs.next() ) {
                HoldingItemBean bean = new HoldingItemBean();
                Long id = RsetTools.getLong( rs, "id_road" );
                String name = RsetTools.getString( rs, "full_name_road" );

                if( name != null && id != null ) {
                    bean.setName( name );
                    bean.setId( id );
                    list.add( bean );
                }
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<UserItemBean> getUserList() {

        DatabaseAdapter db = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String sql_ =
                        "select a.ID_USER, a.LAST_NAME, a.FIRST_NAME, a.MIDDLE_NAME " +
                        "from   WM_LIST_USER a " +
                        "where  a.ID_FIRM in (" + AuthHelper.getGrantedCompanyId( db, moduleUser.getUserLogin() ) + ") and " +
                        "       a.IS_DELETED=0 " +
                        // Todo uncomment when will be fixed problem with default value of timestamp
                        // Todo mysql create timestamp field with presseted value
                        // Todo read 1.8.6.2 Constraint NOT NULL and DEFAULT Values 'mysql manual'
//                                "and ((a.DATE_FIRE>"+db.getNameDateBind()+") or (a.DATE_FIRE is null)) " +
                        "order by a.LAST_NAME ASC, a.FIRST_NAME ASC, a.MIDDLE_NAME ASC ";

                    ps = db.prepareStatement( sql_ );
                    log.debug( "User list SQL:\n" + sql_ );

                    break;
                default:
                    ps = db.prepareStatement( "select a.ID_USER, a.LAST_NAME, a.FIRST_NAME, a.MIDDLE_NAME " +
                        "from   WM_LIST_USER a, V$_READ_LIST_FIRM b " +
                        "where  a.ID_FIRM=b.ID_FIRM and b.USER_LOGIN=? and " +
                        "       IS_DELETED=0 and ((a.DATE_FIRE>" + db.getNameDateBind() + ") or (DATE_FIRE is null)) " +
                        "order by LAST_NAME ASC, FIRST_NAME ASC, MIDDLE_NAME ASC " );
                    ps.setString( 1, moduleUser.getUserLogin() );
                    db.bindDate( ps, 2, DateTools.getCurrentTime() );
                    break;
            }

            rs = ps.executeQuery();

            List<UserItemBean> list = new ArrayList<UserItemBean>();
            while( rs.next() ) {
                UserItemBean user = new UserItemBean();
                user.setUserId( RsetTools.getLong( rs, "ID_USER" ) );
                user.setUserName( StringTools.getUserName( RsetTools.getString( rs, "FIRST_NAME" ),
                    RsetTools.getString( rs, "MIDDLE_NAME" ),
                    RsetTools.getString( rs, "LAST_NAME" ) ) );
                list.add( user );
            }

            return list;

        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public RoleBean getRole( Long roleId ) {

        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            WmAuthAccessGroupItemType item = GetWmAuthAccessGroupItem.getInstance( db, roleId ).item;

            if (item==null) {
                return null;
            }
            RoleBean roleBean = new RoleBean();
            roleBean.setName( item.getNameAccessGroup() );
            roleBean.setRoleId( item.getIdAccessGroup() );
            roleBean.setNew( true );
            return roleBean;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }
}
