package org.riverock.portlet.auth.role.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.portlet.company.bean.CompanyBean;
import org.riverock.interfaces.sso.a3.AuthSession;

public class RoleDAOImpl implements RoleDAO, Serializable {
    private static final long serialVersionUID = 2057005512L;
    private static final Logger log = Logger.getLogger( RoleDAOImpl.class );

    /* (non-Javadoc)
     */
	public RoleBean loadRole( Long roleId, AuthSession authSession ) {
        if( roleId == null ) {
            return null;
        }
        DatabaseAdapter db = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            db = DatabaseAdapter.getInstance();

            String sql =
                "select ID_ACCESS_GROUP, NAME_ACCESS_GROUP " +
                "from 	WM_AUTH_ACCESS_GROUP " +
                "where  ID_ACCESS_GROUP=? ";

            ps = db.prepareStatement( sql );
            ps.setLong( 1, roleId );

            rs = ps.executeQuery();

            RoleBean role = null;
            if( rs.next() ) {
                role = loadRoleFromResultSet( rs );
            }
            return role;
        }
        catch( Exception e ) {
            String es = "Error load role for id: " + roleId;
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db, rs, ps );
            db = null;
            rs = null;
            ps = null;
        }
    }

    public List<RoleBean> getRoleList( AuthSession authSession ) {
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

    public Long processAddRole( RoleBean roleBean, AuthSession authSession ) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_AUTH_ACCESS_GROUP" );
            seq.setTableName( "WM_AUTH_ACCESS_GROUP" );
            seq.setColumnName( "ID_ACCESS_GROUP" );
            Long sequenceValue = dbDyn.getSequenceNextValue( seq );


            ps = dbDyn.prepareStatement( 
		"insert into WM_AUTH_ACCESS_GROUP " +
		"( ID_ACCESS_GROUP, NAME_ACCESS_GROUP )" +
                ( dbDyn.getIsNeedUpdateBracket() ? "(" : "" ) +
                " ?, ? " +
                ( dbDyn.getIsNeedUpdateBracket() ? ")" : "" ) );

            RsetTools.setLong( ps, 1, sequenceValue );
            ps.setString( num++, roleBean.getName() );

            int i1 = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "Count of inserted records - " + i1 );

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
            String es = "Error add new role";
            log.error( es, e );
            throw new IllegalStateException( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }


    public void processSaveRole( RoleBean roleBean, AuthSession authSession ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            String sql =
		"update WM_AUTH_ACCESS_GROUP " +
		"set    ID_ACCESS_GROUP=?, NAME_ACCESS_GROUP=? " +
                "WHERE  ID_ACCESS_GROUP=? ";


            ps = dbDyn.prepareStatement( sql );

            ps.setString( num++, roleBean.getName() );

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

            String es = "Error save role";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }


    public void processDeleteRole( RoleBean roleBean, AuthSession authSession ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            if( companyBean.getId() == null )
                throw new IllegalArgumentException( "companyId is null" );

            String sql =
                "delete from WM_AUTH_ACCESS_GROUP " +
                "where  ID_ACCESS_GROUP=? ";

            ps = dbDyn.prepareStatement( sql );

            RsetTools.setLong( ps, 1, roleBean.getId() );

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

            String es = "Error delete role";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }


    private CompanyBean loadRoleFromResultSet( ResultSet rs ) throws Exception {

        RoleBean role = new RoleBean();
        role.setId( RsetTools.getLong( rs, "ID_ACCESS_GROUP" ) );
        role.setName( RsetTools.getString( rs, "NAME_ACCESS_GROUP" ) );

        return role;                      
    }


}
