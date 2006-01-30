package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.StringTools;
import org.riverock.sso.schema.core.WmAuthRelateAccgroupItemType;
import org.riverock.sso.schema.core.WmAuthUserItemType;
import org.riverock.sso.core.InsertWmAuthRelateAccgroupItem;
import org.riverock.sso.core.GetWmAuthUserItem;
import org.riverock.sso.a3.AuthInfoImpl;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.webmill.portal.utils.SiteList;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 19:33:35
 *         $Id$
 */
public class InternalAuthDaoImpl implements InternalAuthDao {
    private static Logger log = Logger.getLogger(InternalAuthDaoImpl.class);


    public String getGrantedUserId(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedUserId( db, username );
        }
        catch (Exception e) {
            String es = "Error get grnted userd id";
            log.error(es,e);
            throw new IllegalStateException(es,e);
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    private String getGrantedUserId(DatabaseAdapter db, String username) {
        return listToString( getGrantedUserIdList(db, username) );
    }

    private List<Long> getGrantedUserIdList(DatabaseAdapter adapter, String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ = "select ID_USER from WM_AUTH_USER where USER_LOGIN=? ";

            ps = adapter.prepareStatement(sql_);
            ps.setString(1, username);

            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while(rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_USER" );
                if (id==null)
                    continue;
                list.add( id );
            }
            return list;
        }
        catch (Exception e) {
            final String es = "Exception get granted group company id list";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public List<Long> getGrantedUserIdList(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedUserIdList(db, username);
        }
        catch(Exception e) {
            final String es = "Exception get userID list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    public String getGrantedCompanyId(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedCompanyId(db, username);
        }
        catch (Exception e) {
            String es = "Error get grnted userd id";
            log.error(es,e);
            throw new IllegalStateException(es,e);
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    public String getGrantedCompanyId(DatabaseAdapter db, String username) {
        return listToString( getGrantedCompanyIdList(db, username) );
    }

    public List<Long> getGrantedCompanyIdList(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedCompanyIdList(db, username);
        }
        catch(Exception e) {
            final String es = "Exception get granted company id list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    private List<Long> getGrantedCompanyIdList(DatabaseAdapter adapter, String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select  a01.id_firm " +
                "from    WM_AUTH_USER a01 " +
                "where   a01.is_use_current_firm = 1 and a01.user_login=? " +
                "union " +
                "select  d02.id_firm " +
                "from    WM_AUTH_USER a02, WM_LIST_R_GR_COMP_COMP d02 " +
                "where   a02.is_service = 1 and a02.id_service = d02.id_service and a02.user_login=? " +
                "union " +
                "select  e03.id_firm " +
                "from    WM_AUTH_USER a03, WM_LIST_R_HOLDING_GR_COMPANY d03, WM_LIST_R_GR_COMP_COMP e03 " +
                "where   a03.is_road = 1 and a03.id_road = d03.id_road and d03.id_service = e03.id_service and " +
                "        a03.user_login=? " +
                "union " +
                "select  b04.id_firm " +
                "from    WM_AUTH_USER a04, WM_LIST_COMPANY b04 " +
                "where   a04.is_root = 1 and a04.user_login=? ";

            ps = adapter.prepareStatement(sql_);
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);
            ps.setString(4, username);

            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while(rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_FIRM" );
                if (id==null)
                    continue;
                list.add( id );
            }
            return list;
        }
        catch(Exception e)
        {
            final String es = "Exception get firmID list";
            log.error(es, e);
            throw new IllegalStateException(es,e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    private String getGrantedGroupCompanyId(DatabaseAdapter db, String username) {
        return listToString( getGrantedGroupCompanyIdList(db, username) );
    }

    public String getGrantedGroupCompanyId(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedGroupCompanyId(db, username);
        }
        catch (Exception e) {
            String es = "Error get grnted userd id";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    public List<Long> getGrantedGroupCompanyIdList(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedGroupCompanyIdList(db, username);
        }
        catch(Exception e) {
            final String es = "Exception get granted group company id list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    public List<Long> getGrantedGroupCompanyIdList(DatabaseAdapter adapter, String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select  a01.id_service "+
                "from    WM_AUTH_USER a01 "+
                "where   a01.is_service = 1 and a01.user_login=?  "+
                "union "+
                "select  b02.id_service "+
                "from    WM_AUTH_USER a02, WM_LIST_R_HOLDING_GR_COMPANY b02 "+
                "where   a02.is_road = 1 and a02.id_road = b02.id_road and a02.user_login=?  "+
                "union "+
                "select  b04.id_service "+
                "from    WM_AUTH_USER a04, WM_LIST_GROUP_COMPANY b04 "+
                "where   a04.is_root = 1 and a04.user_login=? ";


            ps = adapter.prepareStatement(sql_);
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);

            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while(rs.next())
            {
                Long id = RsetTools.getLong(rs, "id_service" );
                if (id==null)
                    continue;
                list.add( id );
            }
            return list;
        }
        catch (Exception e) {
            final String es = "Exception get granted group company id list";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close( rs, ps);
            rs = null;
            ps = null;
        }
    }

    private String getGrantedHoldingId(DatabaseAdapter db, String username) {
        return listToString( getGrantedHoldingIdList(db, username) );
    }

    public String getGrantedHoldingId(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedHoldingId(db, username);
        }
        catch (Exception e) {
            String es = "Error get grnted userd id";
            log.error(es,e);
            throw new IllegalStateException(es,e);
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    public List<Long> getGrantedHoldingIdList(String username) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getGrantedUserIdList(db, username);
        }
        catch(Exception e) {
            final String es = "Exception get userID list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    private List<Long> getGrantedHoldingIdList(DatabaseAdapter adapter, String username) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select  a01.id_road "+
                "from    WM_AUTH_USER a01 "+
                "where   a01.is_road=1 and a01.user_login=?  "+
                "union "+
                "select  b04.id_road "+
                "from    WM_AUTH_USER a04, WM_LIST_HOLDING b04 "+
                "where   a04.is_root=1 and a04.user_login=?";

            ps = adapter.prepareStatement(sql_);
            ps.setString(1, username);
            ps.setString(2, username);

            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while(rs.next())
            {
                Long id = RsetTools.getLong(rs, "id_road" );
                if (id==null)
                    continue;
                list.add( id );
            }
            return list;
        }
        catch(Exception e)
        {
            final String es = "Exception get granted holding  id list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public Long checkCompanyId(Long companyId, String userLogin) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    ps = db.prepareStatement(
                        "select ID_FIRM from WM_LIST_COMPANY " +
                        "where ID_FIRM in ("+getGrantedCompanyId(db, userLogin)+") and ID_FIRM=?"
                    );
                    ps.setObject(1, companyId);
                    break;
                default:
                    ps = db.prepareStatement(
                        "select ID_FIRM from v$_read_list_firm where USER_LOGIN=? and ID_FIRM=?"
                    );

                    ps.setString(1, userLogin);
                    ps.setObject(2, companyId);
                    break;
            }


            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getLong(rs, "ID_FIRM");

            return null;
        }
        catch (Exception e) {
            final String es = "Error check company id";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long checkGroupCompanyId(Long groupCompanyId, String userLogin) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch (db.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    ps = db.prepareStatement(
                        "select ID_SERVICE from WM_LIST_GROUP_COMPANY " +
                        "where ID_SERVICE in ("+getGrantedGroupCompanyId(db, userLogin)+") and ID_SERVICE=?"
                    );
                    ps.setObject(1, groupCompanyId);
                    break;
                default:
                    ps = db.prepareStatement(
                        "select ID_SERVICE from v$_read_list_service where USER_LOGIN=? and ID_SERVICE=?"
                    );

                    ps.setString(1, userLogin);
                    ps.setObject(2, groupCompanyId);

                    break;
            }
            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getLong(rs, "ID_SERVICE");

            return null;
        }
        catch (Exception e) {
            final String es = "Error check group company id";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long checkHoldingId(Long holdingId, String userLogin) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch (db.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    ps = db.prepareStatement(
                        "select ID_ROAD from WM_LIST_HOLDING " +
                        "where ID_ROAD in ("+getGrantedHoldingId(db, userLogin)+") and ID_ROAD=?"
                    );
                    ps.setObject(1, holdingId);
                    break;
                default:
                    ps = db.prepareStatement(
                        "select ID_ROAD from v$_read_list_road where USER_LOGIN = ? and ID_ROAD=?"
                    );

                    ps.setString(1, userLogin);
                    ps.setObject(2, holdingId);

                    break;
            }
            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getLong(rs, "ID_ROAD");

            return null;
        }
        catch (Exception e) {
            final String es = "Error check company id";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            db = null;
            rs = null;
            ps = null;
        }
    }

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    WmAuthUserItemType auth = GetWmAuthUserItem.getInstance(db, id_auth_user_owner).item;
                    if (auth==null)
                        return false;

                    ps = db.prepareStatement(
                        "select null " +
                        "from   WM_AUTH_USER a, WM_LIST_USER b " +
                        "where  a.ID_USER=b.ID_USER and a.ID_AUTH_USER=? and " +
                        "       b.ID_FIRM  in ("+getGrantedCompanyId(db, auth.getUserLogin())+") "
                    );

                    RsetTools.setLong(ps, 1, id_auth_user_check);
                    break;
                default:
                    ps = db.prepareStatement(
                        "select null " +
                        "from   WM_AUTH_USER a, WM_LIST_USER b, V$_READ_LIST_FIRM z1 " +
                        "where  a.ID_USER=b.ID_USER and a.ID_AUTH_USER=? and " +
                        "       b.ID_FIRM = z1.ID_FIRM and z1.ID_AUTH_USER=? "
                    );

                    RsetTools.setLong(ps, 1, id_auth_user_check);
                    RsetTools.setLong(ps, 2, id_auth_user_owner);
                    break;
            }
            rs = ps.executeQuery();

            return rs.next();
        }
        catch (Exception e) {
            final String es = "Error check right on user";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ ) {
        if ( log.isDebugEnabled() )
            log.debug( "role '" + role_ + "', user login '" + userLogin + "'  " );

        if ( StringTools.isEmpty(userLogin) ||
            userPassword == null ||
            StringTools.isEmpty(role_)
        )
            return false;

        long startMills = 0;
        if ( log.isInfoEnabled() )
            startMills = System.currentTimeMillis();

        PreparedStatement ps = null;
        ResultSet rset = null;
        DatabaseAdapter db_ = null;
        AuthInfo authInfo = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            authInfo = getAuthInfo( db_, userLogin, userPassword );

            if ( authInfo == null )
                return false;

            if ( log.isDebugEnabled() )
                log.debug( "userLogin " + userLogin + " role " + role_ );

            if ( authInfo.isRoot() )
                return true;

            if ( log.isDebugEnabled() )
                log.debug( "#1.011" );

            ps = db_.prepareStatement( "select null " +
                "from   WM_AUTH_USER a, WM_AUTH_RELATE_ACCGROUP b, WM_AUTH_ACCESS_GROUP c " +
                "where  a.USER_LOGIN=? and " +
                "       a.ID_AUTH_USER=b.ID_AUTH_USER and " +
                "       b.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and " +
                "       c.NAME_ACCESS_GROUP=?" );

            ps.setString( 1, userLogin );
            ps.setString( 2, role_ );

            rset = ps.executeQuery();

            return rset.next();
        }
        catch( Exception e ) {
            final String es = "Error getRigth(), role " + role_;
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rset, ps );
            rset = null;
            ps = null;
            db_ = null;

            if ( log.isInfoEnabled() )
                log.info( "right processed for " + ( System.currentTimeMillis() - startMills ) + " milliseconds" );
        }
    }


    private AuthInfo set(ResultSet rs) throws SQLException {
        AuthInfoImpl bean = new AuthInfoImpl();
        bean.setAuthUserId( RsetTools.getLong(rs, "ID_AUTH_USER") );
        bean.setUserId( RsetTools.getLong(rs, "ID_USER") );
        bean.setCompanyId( RsetTools.getLong(rs, "ID_FIRM") );
        bean.setGroupCompanyId( RsetTools.getLong(rs, "ID_SERVICE") );
        bean.setHoldingId( RsetTools.getLong(rs, "ID_ROAD") );
        bean.setUserLogin( RsetTools.getString(rs, "USER_LOGIN") );
        bean.setUserPassword( RsetTools.getString(rs, "USER_PASSWORD") );

        int isUseCurrentFirmTemp = RsetTools.getInt(rs, "IS_USE_CURRENT_FIRM", 0);
        int isServiceTemp = RsetTools.getInt(rs, "IS_SERVICE", 0);
        int isRoadTemp = RsetTools.getInt(rs, "IS_ROAD", 0);
        int isRootTemp = RsetTools.getInt(rs, "IS_ROOT", 0);

        bean.setCompany( (isUseCurrentFirmTemp+isServiceTemp+isRoadTemp+isRootTemp)>0 );
        bean.setGroupCompany( (isServiceTemp+isRoadTemp+isRootTemp)>0 );
        bean.setHolding( (isRoadTemp+isRootTemp)>0 );
        bean.setRoot( (isRootTemp)>0 );

        return bean;
    }

    public AuthInfo getAuthInfo(String login_, String pass_) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getAuthInfo(db, login_, pass_);
        }
        catch(Exception e) {
            final String es = "Exception get granted group company id list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    private AuthInfo getAuthInfo(DatabaseAdapter db_, String login_, String pass_) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select * from WM_AUTH_USER where USER_LOGIN=? and USER_PASSWORD=?"
            );

            ps.setString(1, login_);
            ps.setString(2, pass_);

            rs = ps.executeQuery();
            if (rs.next()) {
                return set(rs);
            }
            return null;
        }
        catch (Exception e)
        {
            String es = "Error in getInstance(DatabaseAdapter db_, String login_, String pass_)";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public AuthInfo getAuthInfo(Long authUserId) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getAuthInfo(db, authUserId);
        }
        catch(Exception e) {
            final String es = "Exception get granted group company id list";
            log.error(es, e);
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
            db = null;
        }
    }

    private AuthInfo getAuthInfo(DatabaseAdapter db_, Long authUserId) {
        if (authUserId==null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = db_.prepareStatement( "select * from  WM_AUTH_USER where ID_AUTH_USER=?" );
            RsetTools.setLong(ps, 1, authUserId);
            rs = ps.executeQuery();
            if (rs.next()){
                return set(rs);
            }
            return null;
        }
        catch (Throwable e){
            String es = "AuthInfo.getInstance() exception";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    boolean checkAccess( final DatabaseAdapter adapter, final String userLogin, String userPassword, final String serverName ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isValid = false;

        String sql_ =
            "select a.ID_USER from WM_AUTH_USER a, WM_LIST_USER b, " +
            "( " +
            "select z1.USER_LOGIN from V$_READ_LIST_FIRM z1, WM_PORTAL_LIST_SITE x1 " +
            "where x1.ID_SITE=? and z1.ID_FIRM = x1.ID_FIRM " +
            "union " +
            "select y1.USER_LOGIN from WM_AUTH_USER y1 where y1.IS_ROOT=1 " +
            ") c " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "a.ID_USER = b.ID_USER and b.is_deleted=0 and a.USER_LOGIN=c.USER_LOGIN ";

        try {

            Long idSite = SiteList.getIdSite( serverName );
            if ( log.isDebugEnabled() )
                log.debug( "serverName " + serverName + ", idSite " + idSite );

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, userLogin );
            ps.setString( 3, userPassword );

            rs = ps.executeQuery();
            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if ( log.isDebugEnabled() )
            log.debug( "isValid " + isValid );

        return isValid;
    }

    boolean checkAccessMySql( final DatabaseAdapter adapter, final String userLogin, String userPassword, final String serverName ) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isValid = false;

        String sql_ =
            "select a.* from WM_AUTH_USER a, WM_LIST_USER b " +
            "where  a.USER_LOGIN=? and a.USER_PASSWORD=? and " +
            "       a.ID_USER = b.ID_USER and b.is_deleted=0";

        try {

            Long idSite = SiteList.getIdSite( serverName );
            if ( log.isDebugEnabled() )
                log.debug( "serverName " + serverName + ", idSite " + idSite );

            ps = adapter.prepareStatement( sql_ );

            ps.setString( 1, userLogin );
            ps.setString( 2, userPassword );

            rs = ps.executeQuery();
            if ( !rs.next() )
                return false;

            WmAuthUserItemType item = GetWmAuthUserItem.fillBean( rs );
            rs.close();
            rs = null;
            ps.close();
            ps = null;

            if ( Boolean.TRUE.equals( item.getIsRoot() ) )
                return true;

            sql_ =
                "select  a01.id_firm, a01.user_login, a01.id_user, a01.id_auth_user " +
                "from    WM_AUTH_USER a01, WM_PORTAL_LIST_SITE f01 " +
                "where   a01.is_use_current_firm = 1 and a01.ID_FIRM = f01.ID_FIRM and f01.ID_SITE=? and " +
                "        a01.user_login=? " +
                "union " +
                "select  d02.id_firm, a02.user_login, a02.id_user, a02.id_auth_user " +
                "from    WM_AUTH_USER a02, WM_LIST_R_GR_COMP_COMP d02, WM_PORTAL_LIST_SITE f02 " +
                "where   a02.is_service = 1 and a02.id_service = d02.id_service and " +
                "        d02.id_firm= f02.ID_FIRM and f02.ID_SITE=? and a02.user_login=? " +
                "union " +
                "select  e03.id_firm, a03.user_login, a03.id_user, a03.id_auth_user " +
                "from    WM_AUTH_USER a03, WM_LIST_R_HOLDING_GR_COMPANY d03, WM_LIST_R_GR_COMP_COMP e03, WM_PORTAL_LIST_SITE f03 " +
                "where   a03.is_road = 1 and a03.id_road = d03.id_road and " +
                "        d03.id_service = e03.id_service and e03.id_firm = f03.ID_FIRM and f03.ID_SITE=? and " +
                "        a03.user_login=? " +
                "union " +
                "select  b04.id_firm, a04.user_login, a04.id_user, a04.id_auth_user " +
                "from    WM_AUTH_USER a04, WM_LIST_COMPANY b04, WM_PORTAL_LIST_SITE f04 " +
                "where   a04.is_root = 1 and b04.ID_FIRM = f04.ID_FIRM and f04.ID_SITE=? and " +
                "        a04.user_login=? ";

            ps = adapter.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            ps.setString( 2, userLogin );
            RsetTools.setLong( ps, 3, idSite );
            ps.setString( 4, userLogin );
            RsetTools.setLong( ps, 5, idSite );
            ps.setString( 6, userLogin );
            RsetTools.setLong( ps, 7, idSite );
            ps.setString( 8, userLogin );

            rs = ps.executeQuery();

            if ( rs.next() )
                isValid = true;

        }
        catch( Exception e1 ) {
            log.error( "SQL:\n" + sql_ );
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        if ( log.isDebugEnabled() )
            log.debug( "isValid " + isValid );

        return isValid;
    }

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            switch( db_.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    return checkAccessMySql( db_, userLogin, userPassword, serverName );
                default:
                    return checkAccess( db_, userLogin, userPassword, serverName );
            }
        }
        catch( Exception e1 ) {
            final String es = "Error check checkAccess()";
            log.error( es, e1 );
            throw new IllegalStateException( es, e1 );
        }
        finally {
            DatabaseManager.close( db_ );
            db_ = null;
        }
    }

    private String listToString(List<Long> list) {
        if (list.isEmpty())
            return "NULL";

        Iterator<Long> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Long aLong = iterator.next();

            if (isFirst) {
                sb.append( ", " );
            }

            sb.append( aLong );
        }
        return sb.toString();
    }


// From InternalAuthDaoTools

    public static Long addRole(DatabaseAdapter db_, String role_name)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            String sql_ =
                "insert into WM_AUTH_ACCESS_GROUP " +
                "( ID_ACCESS_GROUP, NAME_ACCESS_GROUP ) " +
                "VALUES "+
                "( ?, ? )";

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_auth_access_group");
            seq.setTableName( "WM_AUTH_ACCESS_GROUP");
            seq.setColumnName( "ID_ACCESS_GROUP" );
            Long id = db_.getSequenceNextValue(seq);

            ps = db_.prepareStatement(sql_);

            ps.setObject(1, id);
            ps.setString(2, role_name);
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id;
        }
        catch (Exception e)
        {
            log.error("Error add role", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long checkWithCreateRole(DatabaseAdapter ora_, String role_name)
        throws Exception
    {

        Long id_group = getIDRole(ora_, role_name);
        if (id_group == null)
            id_group = addRole(ora_, role_name);

        return id_group;

    }

    public static Long getUserRole(DatabaseAdapter ora_, Long id_auth_user,
        Long id_role)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = ora_.prepareStatement(
                "select id_relate_accgroup from WM_AUTH_RELATE_ACCGROUP " +
                "where  id_auth_user=? and id_access_group=?"
            );
            ps.setObject(1, id_auth_user);
            ps.setObject(2, id_role);
            rs = ps.executeQuery();

            Long result = null;
            if (rs.next())
                result = RsetTools.getLong(rs, "id_relate_accgroup");

            return result;
        }
        catch (Exception e)
        {
            log.error("Error get user role", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static Long getUserRole(DatabaseAdapter ora_, Long id_user,
        String role_name)
        throws Exception
    {
        Long id_role = getIDRole(ora_, role_name);
        if (id_role == null)
            return null;

        return getUserRole(ora_, id_user, id_role);
    }

    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user, String role_name)
        throws Exception
    {
        Long roleId = getIDRole(ora_, role_name);
        if (roleId==null) {
             return false;
        }
        return bindUserRole(ora_, id_auth_user, roleId );
    }

    public static boolean bindUserRole(DatabaseAdapter ora_, Long id_auth_user, Long id_role) {
        if (id_auth_user==null)  {
            throw new IllegalStateException( "authUserId argument must not be null" );
        }

        if (id_role==null)  {
            throw new IllegalStateException( "roleId argument must not be null" );
        }

        WmAuthRelateAccgroupItemType item = new WmAuthRelateAccgroupItemType();
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_WM_AUTH_RELATE_ACCGROUP");
        seq.setTableName( "WM_AUTH_RELATE_ACCGROUP");
        seq.setColumnName( "ID_RELATE_ACCGROUP" );
        try {
            long id = ora_.getSequenceNextValue( seq );

            item.setIdRelateAccgroup( id );
            item.setIdAuthUser( id_auth_user );
            item.setIdAccessGroup( id_role );
            InsertWmAuthRelateAccgroupItem.process( ora_, item );
        }
        catch (Exception e) {
            final String es = "error bind role to user";
            log.error(es,e);
            throw new IllegalStateException( es );
        }
        return true;
    }

    public static Long addUserAuth(DatabaseAdapter db_, Long id_user,
        Long id_firm, Long id_service, Long id_road,
        String username, String password,
        boolean isFirm, boolean isService, boolean isRoad)
        throws Exception
    {

        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("SEQ_WM_AUTH_USER");
            seq.setTableName( "WM_AUTH_USER");
            seq.setColumnName( "ID_AUTH_USER" );
            long id_auth_user = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_AUTH_USER " +
                "(ID_AUTH_USER, id_user, ID_FIRM, user_login, user_password, " +
                "is_service, is_road, is_use_current_firm, is_root, id_road, id_service)" +
                "values "+
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"
            );

            ps.setLong(1, id_auth_user);
            RsetTools.setLong(ps, 2, id_user);
            RsetTools.setLong(ps, 3, id_firm);
            ps.setString(4, username);
            ps.setString(5, password);
            ps.setInt(6, isService ? 1 : 0);
            ps.setInt(7, isRoad ? 1 : 0);
            ps.setInt(8, isFirm ? 1 : 0);
            ps.setInt(9, 0);
            RsetTools.setLong(ps, 10, id_road);
            RsetTools.setLong(ps, 11, id_service);

            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id_auth_user;
        }
        catch (Exception e)
        {
            log.error("Error add user auth", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void setRelateGroupCompanyCompany(DatabaseAdapter ora_, Long id_service, Long id_firm)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            if (!getRelateServiceFirm(ora_, id_firm, id_service))
            {
                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName("seq_WM_LIST_R_GR_COMP_COMP");
                seq.setTableName( "WM_LIST_R_GR_COMP_COMP");
                seq.setColumnName( "ID_REL_SERVICE" );
                long id = ora_.getSequenceNextValue( seq );

                ps = ora_.prepareStatement(
                    "insert into WM_LIST_R_GR_COMP_COMP " +
                    "(ID_REL_SERVICE, ID_SERVICE, ID_FIRM) " +
                    "values "+
                    "(?, ?, ?)"
                );
                ps.setLong(1, id);
                ps.setObject(2, id_service);
                ps.setObject(3, id_firm);
                int i = ps.executeUpdate();

                if (log.isDebugEnabled())
                    log.debug("Count of added role - "+i);
            }
        }
        catch (Exception e)
        {
            log.error("Error setRelateServiceFirm", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }

    }

    public static void deleteRelateGroupCompanyCompany(DatabaseAdapter ora_, Long id_service, Long id_firm)
    {
        PreparedStatement ps = null;
        try
        {
            if (!getRelateServiceFirm(ora_, id_firm, id_service))
            {
                ps = ora_.prepareStatement(
                    "delete from WM_LIST_R_GR_COMP_COMP " +
                    "where ID_SERVICE=? and ID_FIRM=? "
                );
                ps.setObject(2, id_service);
                ps.setObject(3, id_firm);
                int i = ps.executeUpdate();

                if (log.isDebugEnabled())
                    log.debug("Count of deleted record: "+i);
            }
        }
        catch (Exception e)
        {
		String es = "Error setRelateServiceFirm";
            log.error(es, e);
            throw new IllegalStateException(es,e);
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }

    }

    public static void setRelateServiceFirm(DatabaseAdapter ora_, String service_name, String firm_name)
        throws Exception
    {
        setRelateServiceFirm(ora_, service_name, firm_name, false, false);
    }

    public static void setRelateServiceFirm(DatabaseAdapter ora_, String service_name, String firm_name, boolean add_service, boolean add_firm)
        throws Exception
    {
        Long id_firm = getIDFirm(ora_, firm_name);
        if ((id_firm == null) && (add_firm))
            id_firm = addNewFirm(ora_, firm_name);
        else
            return;

        Long id_service = getIDService(ora_, service_name);
        if ((id_service == null) && (add_service))
            id_service = addNewService(ora_, service_name);
        else
            return;

        setRelateGroupCompanyCompany(ora_, id_service, id_firm);
    }

    public static Long addNewUser(DatabaseAdapter ora_, String first_name,
        String last_name, String middle_name, String firm_name,
        String service_name, String email, String address,
        String phone)
        throws Exception
    {

        setRelateServiceFirm(ora_, service_name, firm_name, true, true);

        return addNewUser(ora_, first_name, last_name, middle_name,
            getIDFirm(ora_, firm_name), email, address, phone);
    }

    public static Long addNewUser(DatabaseAdapter db_, String first_name,
        String last_name, String middle_name, Long id_firm,
        String email, String address, String phone)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_USER");
            seq.setTableName( "WM_LIST_USER");
            seq.setColumnName( "ID_USER" );
            long id = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_LIST_USER " +
                "(ID_USER, FIRST_NAME, LAST_NAME, MIDDLE_NAME, " +
                "ID_FIRM, EMAIL, ADDRESS, TELEPHONE, DATE_START_WORK ) " +
                "values "+
                "( ?, ?, ?, ?, ?, ?, ?, ?, "+db_.getNameDateBind()+" )	"
            );
            ps.setLong(1, id);
            ps.setString(2, first_name);
            ps.setString(3, last_name);
            ps.setString(4, middle_name);
            ps.setObject(5, id_firm);
            ps.setString(6, email);
            ps.setString(7, address);
            ps.setString(8, phone);
            db_.bindDate(ps, 9, DateTools.getCurrentTime());
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return id;
        }
        catch (Exception e)
        {
            log.error("Error addNewUser", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long addNewFirm(DatabaseAdapter db_, String nameFirm)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_COMPANY");
            seq.setTableName( "WM_LIST_COMPANY");
            seq.setColumnName( "ID_FIRM" );
            long ID = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_LIST_COMPANY " +
                "(ID_FIRM, FULL_NAME, SHORT_NAME) " +
                "values "+
                "(?, ?, ?)"
            );
            ps.setLong(1, ID);
            ps.setString(2, nameFirm);
            ps.setString(3, nameFirm);
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return ID;
        }
        catch (Exception e)
        {
            log.error("Error addNewFirm", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long addNewService(DatabaseAdapter db_, String nameService)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_GROUP_COMPANY");
            seq.setTableName( "WM_LIST_GROUP_COMPANY");
            seq.setColumnName( "ID_SERVICE" );
            long ID = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_LIST_GROUP_COMPANY " +
                "(ID_SERVICE, FULL_NAME_SERVICE, SHORT_NAME_SERVICE) " +
                "values "+
                "(?, ?, ? )"
            );
            ps.setLong(1, ID);
            ps.setString(2, nameService);
            ps.setString(3, nameService);
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return ID;
        }
        catch (Exception e)
        {
            log.error("Error addNewService", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static long addNewRoad(DatabaseAdapter db_, String nameRoad)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_LIST_HOLDING");
            seq.setTableName( "WM_LIST_HOLDING");
            seq.setColumnName( "ID_ROAD" );
            long ID = db_.getSequenceNextValue( seq );

            ps = db_.prepareStatement(
                "insert into WM_LIST_HOLDING " +
                "(ID_ROAD, FULL_NAME_ROAD, NAME_ROAD) " +
                "values "+
                "(?, ?, ? )"
            );
            ps.setLong(1, ID);
            ps.setString(2, nameRoad);
            ps.setString(3, nameRoad);
            int i = ps.executeUpdate();

            if (log.isDebugEnabled())
                log.debug("Count of added role - "+i);

            return ID;
        }
        catch (Exception e)
        {
            log.error("Error addNewRoad", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static Long getIDFirm(DatabaseAdapter db_, String nameFirm)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_FIRM from WM_LIST_COMPANY where SHORT_NAME=?"
            );
            ps.setString(1, nameFirm);
            rs = ps.executeQuery();
            Long id = null;
            if (rs.next())
                id = RsetTools.getLong(rs, "ID_FIRM");

            return id;
        }
        catch (Exception e)
        {
            log.error("Error getIDFirm", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static Long getIDService(DatabaseAdapter db_, String nameService)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_SERVICE from WM_LIST_GROUP_COMPANY where SHORT_NAME_SERVICE=?"
            );
            ps.setString(1, nameService);
            rs = ps.executeQuery();
            Long id = null;
            if (rs.next())
                id = RsetTools.getLong(rs, "ID_SERVICE");

            return id;
        }
        catch (Exception e)
        {
            log.error("Error getIDService", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static Long getIDRoad(DatabaseAdapter db_, String nameRoad)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_ROAD from WM_LIST_HOLDING where NAME_ROAD=?"
            );
            ps.setString(1, nameRoad);
            rs = ps.executeQuery();
            Long id = null;
            if (rs.next())
                id = RsetTools.getLong(rs, "ID_ROAD");

            return id;
        }
        catch (Exception e)
        {
            log.error("Error getIDRoad", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static boolean getRelateServiceFirm(DatabaseAdapter ora_,
        Long id_firm, Long id_service)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = ora_.prepareStatement(
                "select null COUNT_REC from WM_LIST_R_GR_COMP_COMP " +
                "where ID_FIRM=? and ID_SERVICE=?"
            );
            ps.setObject(1, id_firm);
            ps.setObject(2, id_service);
            rs = ps.executeQuery();

            return rs.next();
        }
        catch (Exception e)
        {
            log.error("Error getRelateServiceFirm", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static Long getIDRole(DatabaseAdapter db_, String role_name)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(
                "select ID_ACCESS_GROUP from WM_AUTH_ACCESS_GROUP where NAME_ACCESS_GROUP=?"
            );
            ps.setString(1, role_name);
            rs = ps.executeQuery();
            if (rs.next())
                return RsetTools.getLong(rs, "ID_ACCESS_GROUP");

            return null;
        }
        catch (Exception e)
        {
            log.error("Error getIDRole", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
}
