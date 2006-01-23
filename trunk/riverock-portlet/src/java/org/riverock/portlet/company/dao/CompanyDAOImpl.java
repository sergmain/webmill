package org.riverock.portlet.company.dao;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import org.riverock.portlet.company.bean.CompanyBean;
import org.riverock.generic.db.*;
import org.riverock.sso.utils.AuthHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.sso.a3.InternalAuthProviderTools;

public class CompanyDAOImpl implements CompanyDAO, Serializable {
    private static final long serialVersionUID = 2055005512L;
    private static final Logger log = Logger.getLogger( CompanyDAOImpl.class );
	
	/* (non-Javadoc)
	 */
	public CompanyBean loadCompany(Long companyId, String userLogin) {
		if (companyId==null) {
			return null;
		}
		DatabaseAdapter db = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			db = DatabaseAdapter.getInstance();
			
            String sql =
                "select ID_FIRM, full_name, short_name, "+
                "	    address, chief, buh, url,  "+
                "	    short_info, is_work, is_search "+
                "from 	WM_LIST_COMPANY "+
                "where  is_deleted=0 and ID_FIRM=? and ID_FIRM in ";

            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedCompanyId(db, userLogin);

                    sql += " ("+idList+") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = db.prepareStatement( sql );
            int idx = 1;
            ps.setLong(idx++, companyId);
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(idx++, userLogin);
                    break;
            }

            rs = ps.executeQuery();

            CompanyBean company = null;
            if ( rs.next() ) {
            	company = loadCompanyFromResultSet( rs );
            }
            return company;
		}
		catch(Exception e) {
			String es = "Error load company for id: " + companyId;
			throw new IllegalStateException(es, e);
		}
		finally {
			DatabaseManager.close( db, rs, ps);
			db = null;
			rs = null;
			ps = null;
		}
	}

	public List<CompanyBean> getCompanyList( String userLogin ) {
		DatabaseAdapter db = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			db = DatabaseAdapter.getInstance();
			
            String sql =
                "select ID_FIRM, full_name, short_name,\n"+
                "	address, chief, buh, url, \n"+
                "	short_info, is_work, is_search "+
                "from 	WM_LIST_COMPANY "+
                "where  is_deleted=0 and ID_FIRM in ";

            switch (db.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedCompanyId( db, userLogin );

                    sql += " ("+idList+") ";
                    break;
                default:
                    sql += "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }

            ps = db.prepareStatement( sql );
            switch (db.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString(1, userLogin);
                    break;
            }

            rs = ps.executeQuery();

	    List<CompanyBean> list = new ArrayList<CompanyBean>();
            while ( rs.next() ) {
            	list.add( loadCompanyFromResultSet( rs ) );
            }
            return list;
		}
		catch(Exception e) {
			String es = "Error load company list for userLogin: " + userLogin;
			throw new IllegalStateException(es, e);
		}
		finally {
			DatabaseManager.close( db, rs, ps);
			db = null;
			rs = null;
			ps = null;
		}
	}


    public Long processAddCompany( CompanyBean companyBean, String userLogin, Long groupCompanyId, Long holdingId ) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

                dbDyn = DatabaseAdapter.getInstance();

                CustomSequenceType seq = new CustomSequenceType();
                seq.setSequenceName( "seq_WM_LIST_COMPANY" );
                seq.setTableName( "WM_LIST_COMPANY" );
                seq.setColumnName( "ID_FIRM" );
                Long sequenceValue = dbDyn.getSequenceNextValue( seq );


                ps = dbDyn.prepareStatement( "insert into WM_LIST_COMPANY (" +
                    "	ID_FIRM, " +
                    "	full_name, " +
                    "	short_name, " +
                    "	address, " +
                    "	telefon_buh, " +
                    "	telefon_chief, " +
                    "	chief, " +
                    "	buh, " +
                    "	fax, " +
                    "	email, " +
                    "	icq, " +
                    "	short_client_info, " +
                    "	url, " +
                    "	short_info, " +
                    "is_deleted" +
                    ")" +

                    ( dbDyn.getIsNeedUpdateBracket() ?"(" :"" ) +

                    " select " +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?," +
                    "	?,0 from WM_AUTH_USER " +
                    "where USER_LOGIN=? " +
                    ( dbDyn.getIsNeedUpdateBracket() ?")" :"" ) );

                int num = 1;
                RsetTools.setLong( ps, num++, sequenceValue );
                ps.setString( num++, companyBean.getName() );
                ps.setString( num++, companyBean.getShortName() );
                ps.setString( num++, companyBean.getAddress() );
                ps.setString( num++, "" );
                ps.setString( num++, "" );
                ps.setString( num++, companyBean.getCeo() );
                ps.setString( num++, companyBean.getCfo() );
                ps.setString( num++, "" );
                ps.setString( num++, "" );
                RsetTools.setLong( ps, num++, null );
                ps.setString( num++, "" );
                ps.setString( num++, companyBean.getWebsite() );
                ps.setString( num++, companyBean.getInfo() );
                ps.setString( num++, userLogin );

                int i1 = ps.executeUpdate();

                if ( log.isDebugEnabled() )
                    log.debug( "Count of inserted records - " + i1 );

		if (groupCompanyId!=null) {
			InternalAuthProviderTools.setRelateServiceFirm(dbDyn, groupCompanyId, sequenceValue);
		}
//		if (groupCompanyId!=null && holdingId!=null) {
//			InternalAuthProviderTools.setRelateServiceRoad(dbDyn, holdingId, groupCompanyId);
//		}

                dbDyn.commit();
		return sequenceValue;
        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null)
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }
            String es = "Error add new company";
		log.error(es, e);
		throw new IllegalStateException( es, e ); 
	
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }


    public void processSaveCompany( CompanyBean companyBean, String userLogin ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            String sql =
                "UPDATE WM_LIST_COMPANY " +
                "SET " +
                "	full_name = ?, " +
                "	short_name = ?, " +
                "	address = ?, " +
                "	telefon_buh = ?, " +
                "	telefon_chief = ?, " +
                "	chief = ?, " +
                "	buh = ?, " +
                "	fax = ?, " +
                "	email = ?, " +
                "	icq = ?, " +
                "	short_client_info = ?, " +
                "	url = ?, " +
                "	short_info = ? " +
                "WHERE ID_FIRM = ? and ID_FIRM in ";


            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedCompanyId( dbDyn, userLogin );

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }

            ps = dbDyn.prepareStatement( sql );
            int num = 1;

                ps.setString( num++, companyBean.getName() );
                ps.setString( num++, companyBean.getShortName() );
                ps.setString( num++, companyBean.getAddress() );
                ps.setString( num++, "" );
                ps.setString( num++, "" );
                ps.setString( num++, companyBean.getCeo() );
                ps.setString( num++, companyBean.getCfo() );
                ps.setString( num++, "" );
                ps.setString( num++, "" );
                RsetTools.setLong( ps, num++, null );
                ps.setString( num++, "" );
                ps.setString( num++, companyBean.getWebsite() );
                ps.setString( num++, companyBean.getInfo() );
            	RsetTools.setLong( ps, num++, companyBean.getId() );

            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( num++, userLogin );
                    break;
            }

            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of updated record - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            String es = "Error save company";
		log.error(es, e);
		throw new IllegalStateException( es, e ); 
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }


    public void processDeleteCompany( CompanyBean companyBean, String userLogin ) {
        DatabaseAdapter dbDyn = null;
        PreparedStatement ps = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            if ( companyBean.getId()==null )
                throw new IllegalArgumentException( "companyId is null" );

            String sql =
                "update WM_LIST_COMPANY set is_deleted = 1 " +
                "where  ID_FIRM = ? and ID_FIRM in ";

            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = AuthHelper.getGrantedCompanyId( dbDyn, userLogin );

                    sql += " (" + idList + ") ";

                    break;
                default:
                    sql +=
                        "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                    break;
            }
            ps = dbDyn.prepareStatement( sql );

            RsetTools.setLong( ps, 1, companyBean.getId() );
            switch( dbDyn.getFamaly() ) {
                case DatabaseManager.MYSQL_FAMALY:
                    break;
                default:
                    ps.setString( 2, userLogin );
                    break;
            }

            int i1 = ps.executeUpdate();

            if ( log.isDebugEnabled() )
                log.debug( "Count of deleted records - " + i1 );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                dbDyn.rollback();
            }
            catch( Exception e001 ) {
            }

            String es = "Error delete company";
		log.error(es, e);
		throw new IllegalStateException( es, e ); 
        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }



	private CompanyBean loadCompanyFromResultSet(ResultSet rs) throws Exception {
	
		CompanyBean company = new CompanyBean();
		company.setId( RsetTools.getLong(rs, "ID_FIRM") );
		company.setName( RsetTools.getString(rs, "full_name") );
		company.setShortName( RsetTools.getString(rs, "short_name") );
		company.setAddress( RsetTools.getString(rs, "address") );
		company.setCeo( RsetTools.getString(rs, "chief") );
		company.setCfo( RsetTools.getString(rs, "buh") );
		company.setWebsite( RsetTools.getString(rs, "url") );
		company.setInfo( RsetTools.getString(rs, "short_info") );
		
		return company;
	}

	
	
}
