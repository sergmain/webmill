package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.webmill.core.GetWmPortalListSiteItem;
import org.riverock.webmill.portal.bean.SiteBean;
import org.riverock.webmill.schema.core.WmPortalListSiteItemType;
import org.riverock.interfaces.portal.bean.Site;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 15:51:49
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalSiteDaoImpl implements InternalSiteDao {
    private final static Logger log = Logger.getLogger(InternalSiteDaoImpl.class);

    public Site getSite(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalListSiteItemType site = GetWmPortalListSiteItem.getInstance(adapter, siteId).item;

            return initSite(site);
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    private Site initSite(WmPortalListSiteItemType site) {
        if (site==null)
            return null;

        SiteBean bean = new SiteBean();
        bean.setAdminEmail( site.getAdminEmail() );
        bean.setCompanyId( site.getIdFirm() );
        bean.setCssDynamic( site.getIsCssDynamic() );
        bean.setCssFile( site.getCssFile() );
        bean.setDefCountry( site.getDefCountry() );
        bean.setDefLanguage( site.getDefLanguage() );
        bean.setDefVariant( site.getDefVariant() );
        bean.setRegisterAllowed( site.getIsRegisterAllowed() );
        bean.setSiteId( site.getIdSite() );
        bean.setSiteName( site.getNameSite() );

        if (bean.getDefLanguage() == null)
            bean.setDefLanguage("");
        return bean;
    }

    public Site getSite(String siteName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement("select * from WM_PORTAL_LIST_SITE where NAME_SITE=?");

            RsetTools.setString(ps, 1, siteName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return initSite( GetWmPortalListSiteItem.fillBean(rs) );
            }
            return null;
        }
        catch (Exception e) {
            final String es = "Error get site bean for name: " + siteName;
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public Long createSite( Site site ) {

        PreparedStatement ps = null;
        DatabaseAdapter dbDyn = null;
        try {

            dbDyn = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_wm_portal_list_site" );
            seq.setTableName( "wm_portal_list_site" );
            seq.setColumnName( "ID_SITE" );
            Long sequenceValue = dbDyn.getSequenceNextValue( seq );




            ps = dbDyn.prepareStatement( "insert into WM_LIST_COMPANY (" +
                "ID_SITE, ID_FIRM, DEF_LANGUAGE, DEF_COUNTRY, DEF_VARIANT, " +
                "NAME_SITE, ADMIN_EMAIL, IS_CSS_DYNAMIC, CSS_FILE, " +
                "IS_REGISTER_ALLOWED " +
                ")values " +
                ( dbDyn.getIsNeedUpdateBracket() ? "(" : "" ) +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	?," +
                "	? " +
                ( dbDyn.getIsNeedUpdateBracket() ? ")" : "" ) );

            int num = 1;
            RsetTools.setLong( ps, num++, sequenceValue );
            RsetTools.setLong( ps, num++, site.getCompanyId() );
            ps.setString( num++, site.getDefLanguage() );
            ps.setString( num++, site.getDefCountry() );
            ps.setString( num++, site.getDefVariant() );
            ps.setString( num++, site.getSiteName() );
            ps.setString( num++, site.getAdminEmail() );
            ps.setInt( num++, site.getCssDynamic()?1:0 );
            ps.setString( num++, site.getCssFile() );
            ps.setInt( num++, site.getRegisterAllowed()?1:0 );

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
                //catch rollback error
            }
            String es = "Error add new site";
            log.error( es, e );
            throw new IllegalStateException( es, e );

        }
        finally {
            DatabaseManager.close( dbDyn, ps );
            dbDyn = null;
            ps = null;
        }
    }

}
