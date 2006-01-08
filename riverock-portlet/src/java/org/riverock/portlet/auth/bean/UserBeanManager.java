package org.riverock.portlet.auth.bean;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.portlet.schema.core.WmListCompanyItemType;
import org.riverock.portlet.schema.core.WmListGroupCompanyItemType;
import org.riverock.portlet.schema.core.WmListHoldingItemType;
import org.riverock.portlet.core.GetWmListCompanyItem;
import org.riverock.portlet.core.GetWmListGroupCompanyItem;
import org.riverock.portlet.core.GetWmListHoldingItem;

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
                        userBean.setCompanyId(  RsetTools.getLong( rs1, "id_firm") );
                        userBean.setGroupCompanyId(  RsetTools.getLong( rs1, "id_service") );
                        userBean.setHoldingId(  RsetTools.getLong( rs1, "id_road") );

                        if (userBean.getCompanyId()!=null) {
                            WmListCompanyItemType companyItem = GetWmListCompanyItem.getInstance( db_, userBean.getCompanyId() ).item;
                            userBean.setCompanyName( companyItem.getShortName() );
                        }
                        if (userBean.getGroupCompanyId()!=null) {
                            WmListGroupCompanyItemType groupCompanyItem = GetWmListGroupCompanyItem.getInstance( db_, userBean.getGroupCompanyId() ).item;
                            userBean.setGroupCompanyName( groupCompanyItem.getShortNameService() );
                        }
                        if (userBean.getHoldingId()!=null) {
                            WmListHoldingItemType holdingItem = GetWmListHoldingItem.getInstance( db_, userBean.getHoldingId() ).item;
                            userBean.setHoldingName( holdingItem.getNameRoad() );
                        }
			userBean.setRoles( getRoles(db_, userBean.getAuthUserId() ) );

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

    private List<RoleBean> getRoles(DatabaseAdapter db_, Long authUserId ) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CompanyBean> list = new ArrayList<CompanyBean>();
        String userLogin = moduleUser.getUserLogin();

        try {
                ps = db_.prepareStatement( 
			"select c.ID_ACCESS_GROUP, c.NAME_ACCESS_GROUP "+
			"from   WM_AUTH_ACCESS_GROUP c, WM_AUTH_RELATE_ACCGROUP b "+
			"where  b.ID_AUTH_USER=? and " +
        	        "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP "
		);

                ps.setLong( 1, authUserId );
                rs = ps.executeQuery();

		List<RoleBean> roles = new ArrayList<RoleBean>();
                while( rs.next() ) {
			RoleBean role = new RoleBean();

			role.setName( RsetTools.getString(rs, "NAME_ACCESS_GROUP") );
			role.setRoleId( RsetTools.getLong(rs, "ID_ACCESS_GROUP") );

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

    private void processDeleteUserRoles(DatabaseAdapter db_, Long authUserId ) throws Exception {
        PreparedStatement ps = null;
        try {
                ps = db_.prepareStatement( 
			"delete from WM_AUTH_RELATE_ACCGROUP "+
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

    public void processDeleteUser( Long authUserId ) {
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        try {
            db_ = DatabaseAdapter.getInstance();
		processDeleteUserRoles(db_, authUserId );
                ps = db_.prepareStatement( 
			"delete from WM_AUTH_USER "+
			"where  ID_AUTH_USER=? "
		);

                ps.setLong( 1, authUserId );
                ps.execute();
		db_.commit();
	}
	catch(Exception e ) {
		String es = "error delete auth user data";
		log.error(es, e);
		throw new IllegalStateException(es, e);
	}
        finally {
            DatabaseManager.close( db_, ps );
            ps = null;
		db_ = null;
        }
    }

    public void procesSaveUser(UserBean userBean) {
	DatabaseAdapter db = null;
	
	try {
		db = DatabaseAdapter.getInstance();
		DeleteWmAuthRelateAccgroupWithAuthUserId.process(db, userBean.getAuthUserId() );
		Iterator<RoleBean> iterator = userBean.getRoles().iterator();
		while(iteraot.hasNext()) {
			RoleBean roleBean = iterator.next();
			WmAuthRelateAccgroupItemType role = new WmAuthAccgroupItemType();

            		CustomSequenceType seq = new CustomSequenceType();
            		seq.setSequenceName( "seq_wm_auth_relate_accgroup" );
            		seq.setTableName( "wm_auth_relate_accgroup" );
            		seq.setColumnName( "ID_RELATE_ACCGROUP" );
            		Long id = dbDyn.getSequenceNextValue( seq );

			role.setIdRelateAccgroup( id );
			role.setIdAuthUser( userBean.getAuthUserId() );
			role.setIdAccessGroup( role.getRoleId() );

			InsertWmAuthRelateAccgroup.process(db, role);
		}
/*
 wm_auth_user | CREATE TABLE `wm_auth_user` (
 `ID_AUTH_USER` decimal(10,0) NOT NULL default '0',
 `ID_USER` decimal(10,0) NOT NULL default '0',
 `ID_FIRM` decimal(10,0) default NULL,
 `USER_LOGIN` varchar(20) default NULL,
 `USER_PASSWORD` varchar(20) default NULL,
 `IS_SERVICE` decimal(10,0) NOT NULL default '0',
 `IS_ROAD` decimal(10,0) NOT NULL default '0',
 `IS_USE_CURRENT_FIRM` decimal(10,0) NOT NULL default '1',
 `ID_USER_CREATED` decimal(10,0) default NULL,
 `IS_ROOT` decimal(10,0) NOT NULL default '0',
 `ID_ROAD` decimal(10,0) default NULL,
 `ID_SERVICE` decimal(10,0) default NULL,
 PRIMARY KEY  (`ID_AUTH_USER`)
 ENGINE=InnoDB DEFAULT CHARSET=utf8 |		
*/
		WmAuthUserItemType auth = new WmAuthUserItemType();

		auth.setIdAuthUser( userBean.getAuthUserId() );
		auth.setIdUser( userBean.getUserId() );
		auth.setUserLogin( userBean.getUserLogin() );
		auth.setUserPassword( userBean.getUserPassword() );
		auth.setIsService( userBean.isGroupCompany() );
		auth.setIsRoad( userBean.isHolding() );
		auth.setIsUseCurrentFirm( userBean.isCompany() );
		auth.setIsRoot( userBean.isRoot() );
		auth.setIdUserCreated( null );
		auth.setIdFirm( userBean.getCompanyId() );
		auth.setIdService( userBean.getGroupCompanyId() );
		auth.setIdRoad( userBean.getHoldingId() );
		UpdateWmAuthUserItem.process(db, auth);


		db.commit();
	}
	catch(Exception e ) {
		String es = "error delete auth user data";
		log.error(es, e);
		throw new IllegalStateException(es, e);
	}
        finally {
            DatabaseManager.close( db_, ps );
            ps = null;
		db_ = null;
        }
    }

    public void procesAddUser(UserBean userBean) {
	DatabaseAdapter db = null;
	
	try {
		db = DatabaseAdapter.getInstance();
            		CustomSequenceType seq = new CustomSequenceType();
            		seq.setSequenceName( "seq_WM_AUTH_USER" );
            		seq.setTableName( "WM_AUTH_USER" );
            		seq.setColumnName( "ID_AUTH_USER" );
            		Long authUserId = dbDyn.getSequenceNextValue( seq );

		Iterator<RoleBean> iterator = userBean.getRoles().iterator();
		while(iteraot.hasNext()) {
			RoleBean roleBean = iterator.next();
			WmAuthRelateAccgroupItemType role = new WmAuthAccgroupItemType();

            		CustomSequenceType seq = new CustomSequenceType();
            		seq.setSequenceName( "seq_wm_auth_relate_accgroup" );
            		seq.setTableName( "wm_auth_relate_accgroup" );
            		seq.setColumnName( "ID_RELATE_ACCGROUP" );
            		Long id = dbDyn.getSequenceNextValue( seq );

			role.setIdRelateAccgroup( id );
			role.setIdAuthUser( authUserId );
			role.setIdAccessGroup( role.getRoleId() );

			InsertWmAuthRelateAccgroup.process(db, role);
		}
		WmAuthUserItemType auth = new WmAuthUserItemType();

		auth.setIdAuthUser( authUserId );
		auth.setIdUser( userBean.getUserId() );
		auth.setUserLogin( userBean.getUserLogin() );
		auth.setUserPassword( userBean.getUserPassword() );
		auth.setIsService( userBean.isGroupCompany() );
		auth.setIsRoad( userBean.isHolding() );
		auth.setIsUseCurrentFirm( userBean.isCompany() );
		auth.setIsRoot( userBean.isRoot() );
		auth.setIdUserCreated( null );
		auth.setIdFirm( userBean.getCompanyId() );
		auth.setIdService( userBean.getGroupCompanyId() );
		auth.setIdRoad( userBean.getHoldingId() );
		InsertWmAuthUserItem.process(db, auth);


		db.commit();
	}
	catch(Exception e ) {
		String es = "error delete auth user data";
		log.error(es, e);
		throw new IllegalStateException(es, e);
	}
        finally {
            DatabaseManager.close( db_, ps );
            ps = null;
		db_ = null;
        }
    }

}
