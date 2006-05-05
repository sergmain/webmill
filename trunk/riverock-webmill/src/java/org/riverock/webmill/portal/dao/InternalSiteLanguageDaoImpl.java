package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.core.GetWmPortalSiteLanguageItem;
import org.riverock.webmill.core.GetWmPortalSiteLanguageWithIdSiteList;
import org.riverock.webmill.core.InsertWmPortalSiteLanguageItem;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageListType;

/**
 * @author Sergei Maslyukov
 *         Date: 03.05.2006
 *         Time: 17:12:47
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalSiteLanguageDaoImpl implements InternalSiteLanguageDao {
    private final static Logger log = Logger.getLogger(InternalSiteLanguageDaoImpl.class);

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            List<SiteLanguage> list = new ArrayList<SiteLanguage>();
            WmPortalSiteLanguageListType langs = GetWmPortalSiteLanguageWithIdSiteList.getInstance(adapter, siteId).item;

            for (int i = 0; i < langs.getWmPortalSiteLanguageCount(); i++) {
                WmPortalSiteLanguageItemType lang = langs.getWmPortalSiteLanguage(i);
                SiteLanguageBean bean = new SiteLanguageBean();
                bean.setCustomLanguage( StringTools.getLocale(lang.getCustomLanguage()).toString() );
                bean.setNameCustomLanguage( lang.getNameCustomLanguage() );
                bean.setSiteId( lang.getIdSite() );
                bean.setSiteLanguageId( lang.getIdSiteSupportLanguage() );
                list.add( bean );
            }

            return list;
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

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalSiteLanguageItemType lang = GetWmPortalSiteLanguageItem.getInstance(adapter, siteLanguageId).item;

            if (lang==null)
                return null;

            SiteLanguageBean bean = new SiteLanguageBean();
            bean.setCustomLanguage( StringTools.getLocale( lang.getCustomLanguage()).toString() );
            bean.setNameCustomLanguage( lang.getNameCustomLanguage() );
            bean.setSiteId( lang.getIdSite() );
            bean.setSiteLanguageId( lang.getIdSiteSupportLanguage() );
            return bean;
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

    public SiteLanguage getSiteLanguage(String languageLocale) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_SITE_LANGUAGE where CUSTOM_LANGUAGE=? "
            );
            ps.setString(1, languageLocale);
            rs = ps.executeQuery();
            SiteLanguageBean bean = null;
            if (rs.next()) {
                WmPortalSiteLanguageItemType item = GetWmPortalSiteLanguageItem.fillBean(rs);

                bean = new SiteLanguageBean();
                bean.setCustomLanguage( StringTools.getLocale(item.getCustomLanguage()).toString() );
                bean.setNameCustomLanguage( item.getNameCustomLanguage() );
                bean.setSiteId( item.getIdSite() );
                bean.setSiteLanguageId( item.getIdSiteSupportLanguage() );

            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getSiteLanguage()";
            log.error(es, e);
            throw new IllegalStateException(es,e );
        }
        finally{
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_SITE_LANGUAGE" );
            seq.setTableName( "WM_PORTAL_SITE_LANGUAGE" );
            seq.setColumnName( "ID_SITE_SUPPORT_LANGUAGE" );
            Long id = adapter.getSequenceNextValue( seq );

            WmPortalSiteLanguageItemType item = new WmPortalSiteLanguageItemType();
            item.setIdSiteSupportLanguage(id);
            item.setIdSite(siteLanguage.getSiteId());
            item.setCustomLanguage(siteLanguage.getCustomLanguage());
            item.setNameCustomLanguage(siteLanguage.getNameCustomLanguage());

            InsertWmPortalSiteLanguageItem.process(adapter, item);
            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error create site language";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }
}
