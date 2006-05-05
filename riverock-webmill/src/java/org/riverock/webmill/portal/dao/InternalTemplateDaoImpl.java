package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalTemplateItem;
import org.riverock.webmill.core.InsertWmPortalTemplateItem;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.schema.core.WmPortalTemplateItemType;
import org.riverock.webmill.site.PortalTemplateManagerImpl;

/**
 * @author Sergei Maslyukov
 *         Date: 03.05.2006
 *         Time: 17:51:20
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalTemplateDaoImpl implements InternalTemplateDao {
    private final static Logger log = Logger.getLogger(InternalTemplateDaoImpl.class);

    public Template getTemplate(Long templateId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalTemplateItemType template = GetWmPortalTemplateItem.getInstance(adapter, templateId).item;
            TemplateBean bean = null;
            if (template != null) {
                bean = new TemplateBean();
                bean.setSiteLanguageId(template.getIdSiteSupportLanguage());
                bean.setTemplateData(template.getTemplateData());
                bean.setTemplateId(template.getIdSiteTemplate());
                bean.setTemplateName(template.getNameSiteTemplate());
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Template getTemplate(String templateName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_TEMPLATE where NAME_SITE_TEMPLATE=?"
            );
            ps.setString(1, templateName);

            rs = ps.executeQuery();

            TemplateBean bean = null;
            if (rs.next()) {
                WmPortalTemplateItemType item = GetWmPortalTemplateItem.fillBean(rs);

                bean = new TemplateBean();
                bean.setTemplateId(item.getIdSiteTemplate());
                bean.setSiteLanguageId(item.getIdSiteSupportLanguage());
                bean.setTemplateName(item.getNameSiteTemplate());
                bean.setTemplateData(item.getTemplateData());
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
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

    static String templateLanguageSql =
        "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +
            "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
            "where  b.ID_SITE_SUPPORT_LANGUAGE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";

    static {
        try {
            SqlStatement.registerSql(templateLanguageSql, PortalTemplateManagerImpl.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public List<Template> getTemplateLanguageList(Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(templateLanguageSql);
            ps.setLong(1, siteLanguageId);

            rs = ps.executeQuery();

            List<Template> beans = new ArrayList<Template>();
            while (rs.next()) {

                TemplateBean bean = new TemplateBean();
                bean.setSiteLanguageId(siteLanguageId);
                bean.setTemplateData(RsetTools.getString(rs, "TEMPLATE_DATA"));
                bean.setTemplateId(RsetTools.getLong(rs, "ID_SITE_TEMPLATE"));
                bean.setTemplateName(RsetTools.getString(rs, "NAME_SITE_TEMPLATE"));
                bean.setTemplateLanguage(RsetTools.getString(rs, "CUSTOM_LANGUAGE"));

                beans.add(bean);
            }
            return beans;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
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

    static String templateSql =
        "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE " +
            "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
            "where  b.ID_SITE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";

    static {
        try {
            SqlStatement.registerSql(templateSql, PortalTemplateManagerImpl.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public List<Template> getTemplateList(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(templateSql);
            ps.setLong(1, siteId);

            rs = ps.executeQuery();

            List<Template> beans = new ArrayList<Template>();
            while (rs.next()) {

                TemplateBean bean = new TemplateBean();
                bean.setSiteLanguageId(RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE"));
                bean.setTemplateData(RsetTools.getString(rs, "TEMPLATE_DATA"));
                bean.setTemplateId(RsetTools.getLong(rs, "ID_SITE_TEMPLATE"));
                bean.setTemplateName(RsetTools.getString(rs, "NAME_SITE_TEMPLATE"));
                bean.setTemplateLanguage(RsetTools.getString(rs, "CUSTOM_LANGUAGE"));

                beans.add(bean);
            }
            return beans;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
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

    public Long createTemplate(Template template) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_PORTAL_TEMPLATE" );
            seq.setTableName( "WM_PORTAL_TEMPLATE" );
            seq.setColumnName( "ID_SITE_TEMPLATE" );
            Long id = adapter.getSequenceNextValue( seq );

            WmPortalTemplateItemType item = new WmPortalTemplateItemType();
            item.setIdSiteTemplate(id);
            item.setIdSiteSupportLanguage(template.getSiteLanguageId());
            item.setNameSiteTemplate(template.getTemplateName());
            item.setTemplateData(template.getTemplateData());

            InsertWmPortalTemplateItem.process(adapter, item);

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
