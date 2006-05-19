package org.riverock.portlet.manager.users;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.common.tools.RsetTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:50:56
 *         $Id$
 */
public class PortalUserDaoImpl implements PortalUserDao {
    private final static Logger log = Logger.getLogger(PortalUserDaoImpl.class);

    public PortalUserBean getPortalUser( Long portalUserId, AuthSession authSession ) {
        if( portalUserId == null ) {
            return null;
        }

        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select a.ID_USER,a.ID_FIRM,a.FIRST_NAME,a.MIDDLE_NAME,a.LAST_NAME, " +
                "       a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL " +
                "from   WM_LIST_USER a " +
                "where  a.ID_USER=? and a.IS_DELETED=0 and a.ID_FIRM in ";

            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = db.prepareStatement( sql );
            int num = 1;
            ps.setLong( num++, portalUserId );
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, authSession.getUserLogin() );
                    break;
            }

            rs = ps.executeQuery();

            PortalUserBeanImpl beanPortal = null;
            if( rs.next() ) {
                beanPortal = loadPortalUserFromResultSet( rs );
                final Company company = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany( beanPortal.getCompanyId() );
                if (company!=null)
                    beanPortal.setCompanyName( company.getName() );
                else
                    beanPortal.setCompanyName( "Warning. Ccompany not found" );
            }
            return beanPortal;
        }
        catch( Exception e ) {
            String es = "Error load portal user for id: " + portalUserId;
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<PortalUserBean> getPortlalUserList( AuthSession authSession ) {

        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select a.ID_USER,a.ID_FIRM,a.FIRST_NAME,a.MIDDLE_NAME,a.LAST_NAME,a.DATE_START_WORK,a.DATE_FIRE,a.ADDRESS,a.TELEPHONE,a.EMAIL "+
                "from   WM_LIST_USER a "+
                "where  a.IS_DELETED=0  and a.ID_FIRM in ";

            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = db.prepareStatement( sql );

            int num = 1;
            switch( db.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, authSession.getUserLogin() );
                    break;
            }
            rs = ps.executeQuery();

            List<PortalUserBean> list = new ArrayList<PortalUserBean>();
            while( rs.next() ) {
                PortalUserBeanImpl beanPortal = loadPortalUserFromResultSet( rs );
                final Company company = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany( beanPortal.getCompanyId() );
                if (company!=null)
                    beanPortal.setCompanyName( company.getName() );
                else
                    beanPortal.setCompanyName( "Warning. Ccompany not found" );

                list.add( beanPortal );
            }
            return list;
        }
        catch( Exception e ) {
            String es = "Error load list of portlet names";
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public Long addPortalUser( PortalUserBean portalUserBean, AuthSession authSession ) {
	if (portalUserBean==null) {
		throw new IllegalStateException("portalUserBean is null");
	}

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_PORTLET_NAME" );
            seq.setTableName( "WM_PORTAL_PORTLET_NAME" );
            seq.setColumnName( "ID_SITE_CTX_TYPE" );
            Long sequenceValue = dbDyn.getSequenceNextValue( seq );

            String sql =
                "insert into WM_LIST_USER " +
                "(ID_USER,ID_FIRM,FIRST_NAME,MIDDLE_NAME,LAST_NAME,DATE_START_WORK, " +
                "ADDRESS,TELEPHONE,EMAIL) "+
                "values "+
                ( dbDyn.getIsNeedUpdateBracket() ? "(" : "" ) +
                "?,?,?,?,?,?,?,?,? " +
                ( dbDyn.getIsNeedUpdateBracket() ? ")" : "" );

            ps = dbDyn.prepareStatement( sql );
            int num = 1;
            ps.setLong( num++, sequenceValue );
            ps.setLong( num++, portalUserBean.getCompanyId() );
            ps.setString( num++, portalUserBean.getFirstName() );
            ps.setString( num++, portalUserBean.getMiddleName() );
            ps.setString( num++, portalUserBean.getLastName() );
            RsetTools.setTimestamp( ps, num++, new Timestamp(System.currentTimeMillis()) );
            ps.setString( num++, portalUserBean.getAddress() );
            ps.setString( num++, portalUserBean.getPhone() );
            ps.setString( num++, portalUserBean.getEmail() );
            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, authSession.getUserLogin() );
                    break;
            }

            ps.executeUpdate();

            dbDyn.commit();
            return sequenceValue;
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }
            String es = "Error add new portlet name ";
            log.error( es, e );
            throw new IllegalStateException( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public void updatePortalUser( PortalUserBean portalUserBean, AuthSession authSession ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            String sql =
                "update WM_LIST_USER "+
                "set    FIRST_NAME=?,MIDDLE_NAME=?,LAST_NAME=?, " +
                "       ADDRESS=?,TELEPHONE=?,EMAIL=? "+
                "where  ID_USER=? and is_deleted=0 and  ID_FIRM in ";

            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement( sql );
            int num = 1;
            ps.setString( num++, portalUserBean.getFirstName() );
            ps.setString( num++, portalUserBean.getMiddleName() );
            ps.setString( num++, portalUserBean.getLastName() );
            ps.setString( num++, portalUserBean.getAddress() );
            ps.setString( num++, portalUserBean.getPhone() );
            ps.setString( num++, portalUserBean.getEmail() );
            ps.setLong( num++, portalUserBean.getId() );
            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, authSession.getUserLogin() );
                    break;
            }

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of updated record - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            String es = "Error save portlet name";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    public void deletePortalUser( PortalUserBean portalUserBean, AuthSession authSession ) {

        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            if( portalUserBean.getId() == null )
                throw new IllegalArgumentException( "id of portal user is null" );

            String sql =
                "update WM_LIST_USER " +
                "set    is_deleted=1 " +
                "where  ID_USER=? and is_deleted = 0 and ID_FIRM in ";

            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sql += " (" + idList + ") ";
                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement( sql );
            int num = 1;
            ps.setLong( num++, portalUserBean.getId() );
            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, authSession.getUserLogin() );
                    break;
            }

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of deleted records - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            String es = "Error delete portlet name";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

    private PortalUserBeanImpl loadPortalUserFromResultSet( ResultSet rs ) throws Exception {

        PortalUserBeanImpl bean = new PortalUserBeanImpl();
        bean.setId( RsetTools.getLong(rs, "ID_USER") );
        bean.setCompanyId( RsetTools.getLong(rs, "ID_FIRM") );
        bean.setFirstName( RsetTools.getString(rs, "FIRST_NAME") );
        bean.setMiddleName( RsetTools.getString(rs, "MIDDLE_NAME") );
        bean.setLastName( RsetTools.getString(rs, "LAST_NAME") );
        bean.setCreatedDate( RsetTools.getTimestamp(rs, "DATE_START_WORK") );
        bean.setClosedDate( RsetTools.getTimestamp(rs, "DATE_FIRE") );
        bean.setAddress( RsetTools.getString(rs, "ADDRESS") );
        bean.setPhone( RsetTools.getString(rs, "TELEPHONE") );
        bean.setEmail( RsetTools.getString(rs, "EMAIL") );

        return bean;
    }
}
