/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.riverock.webmill.core.UpdateWmPortalTemplateItem;
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
            return getTemplate(adapter, templateId);
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

    public Template getTemplate(DatabaseAdapter adapter, Long templateId) {
        try {
            WmPortalTemplateItemType template = GetWmPortalTemplateItem.getInstance(adapter, templateId).item;
            TemplateBean bean = null;
            if (template != null) {
                bean = new TemplateBean();
                bean.setSiteLanguageId(template.getIdSiteSupportLanguage());
                bean.setTemplateData(template.getTemplateData());
                bean.setTemplateId(template.getIdSiteTemplate());
                bean.setTemplateName(template.getNameSiteTemplate());
                bean.setDefaultDynamic(template.getIsDefaultDynamic());
            }
            return bean;
        }
        catch (Exception e) {
            String es = "Error get getSiteBean()";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    public Template getTemplate(String templateName, Long siteLanguageId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(
                "select * from WM_PORTAL_TEMPLATE where NAME_SITE_TEMPLATE=? and ID_SITE_SUPPORT_LANGUAGE=?"
            );
            ps.setString(1, templateName);
            ps.setLong(2, siteLanguageId);

            rs = ps.executeQuery();

            TemplateBean bean = null;
            if (rs.next()) {
                WmPortalTemplateItemType item = GetWmPortalTemplateItem.fillBean(rs);

                bean = new TemplateBean();
                bean.setTemplateId(item.getIdSiteTemplate());
                bean.setSiteLanguageId(item.getIdSiteSupportLanguage());
                bean.setTemplateName(item.getNameSiteTemplate());
                bean.setTemplateData(item.getTemplateData());
                bean.setDefaultDynamic(item.getIsDefaultDynamic());
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
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE, a.IS_DEFAULT_DYNAMIC " +
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
                bean.setDefaultDynamic(RsetTools.getInt(rs, "IS_DEFAULT_DYNAMIC",0)==1);

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
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE, a.IS_DEFAULT_DYNAMIC " +
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
        if (log.isDebugEnabled())
            log.debug("Start getTemplateList(), siteId: " +siteId);

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
                bean.setDefaultDynamic(RsetTools.getInt(rs, "IS_DEFAULT_DYNAMIC",0)==1);

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

            clearDefaultDynamicFlag(template, adapter);

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
            item.setIsDefaultDynamic(template.isDefaultDynamic());

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
            String es = "Error create template";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void deleteTemplateForSite(DatabaseAdapter adapter, Long siteId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_TEMPLATE " +
                    "where ID_SITE_SUPPORT_LANGUAGE in " +
                    "(select a.ID_SITE_SUPPORT_LANGUAGE from WM_PORTAL_SITE_LANGUAGE a " +
                    "where   a.ID_SITE=? )",

                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete template for site";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void deleteTemplateForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_TEMPLATE where ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[]{siteLanguageId}, new int[]{Types.DECIMAL}
            );
        } catch (SQLException e) {
            String es = "Error delete template for site language";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
    }

    public void updateTemplate(Template template) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            clearDefaultDynamicFlag(template, adapter);

            WmPortalTemplateItemType item = new WmPortalTemplateItemType();
            item.setIdSiteSupportLanguage(template.getSiteLanguageId());
            item.setIdSiteTemplate(template.getTemplateId());
            item.setNameSiteTemplate(template.getTemplateName());
            item.setTemplateData(template.getTemplateData());
            item.setIsDefaultDynamic(template.isDefaultDynamic());

            UpdateWmPortalTemplateItem.process(adapter, item);

            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error update template";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void deleteTemplate(Long templateId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_TEMPLATE " +
                    "where ID_SITE_TEMPLATE=?",

                new Object[]{templateId}, new int[]{Types.DECIMAL}
            );
            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error delete template";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Template getDefaultDynamicTemplate(Long siteLanguageId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            Long templateId = DatabaseManager.getLongValue(
                adapter,
                "select a.ID_SITE_TEMPLATE " +
                    "from   WM_PORTAL_TEMPLATE a " +
                    "where  a.IS_DEFAULT_DYNAMIC=1 and a.ID_SITE_SUPPORT_LANGUAGE=?",
                new Object[]{siteLanguageId},
                new int[] {Types.DECIMAL}
            );
            if (templateId==null) {
                return null;
            }
            return getTemplate(adapter, templateId);

        } catch (Throwable e) {
            String es = "Error delete template";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    private void clearDefaultDynamicFlag(Template template, DatabaseAdapter adapter) throws SQLException {
        if (template.isDefaultDynamic()) {
            DatabaseManager.runSQL(
                adapter,
                "update WM_PORTAL_TEMPLATE set IS_DEFAULT_DYNAMIC=0 where ID_SITE_SUPPORT_LANGUAGE=? and IS_DEFAULT_DYNAMIC!=0",
                new Object[] {template.getSiteLanguageId()},
                new int[]{Types.NUMERIC}
            );
        }
    }
}
