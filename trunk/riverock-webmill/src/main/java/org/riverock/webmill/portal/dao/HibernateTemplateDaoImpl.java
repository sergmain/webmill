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

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.common.exception.DatabaseException;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.dao.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 15.11.2006
 *         Time: 19:30:26
 *         <p/>
 *         $Id$
 */
public class HibernateTemplateDaoImpl implements InternalTemplateDao {
    private final static Logger log = Logger.getLogger(HibernateTemplateDaoImpl.class);

    public TemplateBean getTemplate(Long templateId) {
        if (log.isDebugEnabled()) {
            log.debug("Start getTemplateInternal() for templateId "+ templateId);
        }
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.templateId=:templateId ")
                .setLong("templateId", templateId)
                .uniqueResult();

            prepareBlob(bean);
            return bean;
        }
        finally {
            session.close();
        }
    }

    static void prepareBlob(TemplateBean bean) {
        if (bean!=null) {
            Blob blob = bean.getTemplateBlob();
            if (blob!=null) {
                try {
                    bean.setTemplateData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                }
                catch (UnsupportedEncodingException e) {
                    String es = "Error prepare Template";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
                catch (SQLException e) {
                    String es = "Error get template";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
        }
    }

    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.templateName=:templateName and template.siteLanguageId=:siteLanguageId ")
                .setString("templateName", templateName)
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();

            prepareBlob(bean);
            return bean;
        }
        finally {
            session.close();
        }
    }

    /**
     * Attention! template data not initalized
     * 
     * @param templateName String
     * @param lang String
     * @return org.riverock.interfaces.portal.bean.Template - Attention! template data not initalized
     */
    public TemplateBean getTemplate(Long  siteId, String templateName, String lang) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template " +
                    "from  org.riverock.webmill.portal.bean.TemplateBean as template, " +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLang " +
                    "where template.templateName=:templateName and template.siteLanguageId=siteLang.siteLanguageId and " +
                    "      siteLang.customLanguage=:customLanguage and siteLang.siteId=:siteId")
                .setString("templateName", templateName)
                .setString("customLanguage", lang)
                .setLong("siteId", siteId)
                .uniqueResult();
            // Do not process blob at this point

            return bean;
        }
        finally {
            session.close();
        }
    }

    static String templateLanguageSql =
        "select a.ID_SITE_TEMPLATE, b.CUSTOM_LANGUAGE, a.NAME_SITE_TEMPLATE, " +
            "       a.TEMPLATE_DATA, a.ID_SITE_SUPPORT_LANGUAGE, a.IS_DEFAULT_DYNAMIC " +
            "from   WM_PORTAL_TEMPLATE a, WM_PORTAL_SITE_LANGUAGE b " +
            "where  b.ID_SITE_SUPPORT_LANGUAGE=? and a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE ";

    public List<TemplateBean> getTemplateLanguageList(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<TemplateBean> bean = session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                "where  template.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .list();

            for (TemplateBean templateBean : bean) {
                prepareBlob(templateBean);
            }
            return bean;
        }
        finally {
            session.close();
        }
    }

    public List<TemplateBean> getTemplateList(Long siteId) {
        if (log.isDebugEnabled())
            log.debug("Start getTemplateList(), siteId: " +siteId);

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<TemplateBean> beans = session.createQuery(
                "select template " +
                    "from  org.riverock.webmill.portal.bean.TemplateBean as template," +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLanguage " +
                    "where template.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (TemplateBean templateBean : beans) {
                prepareBlob(templateBean);
            }
            return beans;
        }
        finally {
            session.close();
        }
    }

    public Long createTemplate(TemplateBean template) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            TemplateBean bean;

            if (template.isDefaultDynamic()) {
                bean = (TemplateBean)session.createQuery(
                        "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                                "where template.siteLanguageId=:siteLanguageId and template.isDefaultDynamic=true ")
                        .setLong("siteLanguageId", template.getSiteLanguageId())
                        .uniqueResult();
                if (bean!=null) {
                    bean.setDefaultDynamic(false);
                }
            }

            bean = new TemplateBean();
            bean.setDefaultDynamic(template.isDefaultDynamic());
            bean.setSiteLanguageId(template.getSiteLanguageId());
            bean.setTemplateName(template.getTemplateName());
            if (StringUtils.isNotBlank(template.getTemplateData())) {
                try {
                    bean.setTemplateBlob( Hibernate.createBlob(template.getTemplateData().getBytes(CharEncoding.UTF_8)));
                }
                catch (UnsupportedEncodingException e) {
                    String es = "Error create Template";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
            else {
                bean.setTemplateBlob(null);
            }
            session.save(bean);
            session.flush();
            session.clear();
            session.getTransaction().commit();
            return bean.getTemplateId();
        }
        finally {
            session.close();
        }
    }

    public void deleteTemplateForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            List<TemplateBean> bean = session.createQuery(
                "select template " +
                    "from  org.riverock.webmill.portal.bean.TemplateBean as template," +
                    "      org.riverock.webmill.portal.bean.SiteLanguageBean siteLanguage " +
                    "where template.siteLanguageId=siteLanguage.siteLanguageId and " +
                    "      siteLanguage.siteId=:siteId")
                .setLong("siteId", siteId)
                .list();
            for (TemplateBean templateBean : bean) {
                session.delete(templateBean);
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteTemplateForSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            List<TemplateBean> templateBeans = session.createQuery(
                "select template " +
                    "from  org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .list();
            for (TemplateBean templateBean : templateBeans) {
                session.delete(templateBean);
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void updateTemplate(TemplateBean template) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            TemplateBean bean;

            if (template.isDefaultDynamic()) {
                bean = (TemplateBean)session.createQuery(
                        "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                                "where template.siteLanguageId=:siteLanguageId and template.isDefaultDynamic=true ")
                        .setLong("siteLanguageId", template.getSiteLanguageId())
                        .uniqueResult();
                if (bean!=null) {
                    bean.setDefaultDynamic(false);
                }
            }

            bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.templateId=:templateId ")
                .setLong("templateId", template.getTemplateId())
                .uniqueResult();

            if (bean!=null) {
                bean.setDefaultDynamic(template.isDefaultDynamic());
                bean.setSiteLanguageId(template.getSiteLanguageId());
                bean.setTemplateName(template.getTemplateName());
                if (StringUtils.isNotBlank(template.getTemplateData())) {
                    try {
                        bean.setTemplateBlob( Hibernate.createBlob(template.getTemplateData().getBytes(CharEncoding.UTF_8)));
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error update Template";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
                else {
                    bean.setTemplateBlob(null);
                }

            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void setMaximizedTemplate(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDefaultDynamic(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPopupTemplate(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getMaximizedTemplate(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.siteLanguageId=:siteLanguageId and template.isMaximizedTemplate=true ")
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    public TemplateBean getPopupTemplate(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.siteLanguageId=:siteLanguageId and template.isPopupTemplate=true ")
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    public TemplateBean getDefaultDynamicTemplate(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.siteLanguageId=:siteLanguageId and template.isDefaultDynamic=true ")
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();

            return bean;
        }
        finally {
            session.close();
        }
    }

    public void deleteTemplate(Long templateId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            TemplateBean bean = (TemplateBean)session.createQuery(
                "select template from org.riverock.webmill.portal.bean.TemplateBean as template " +
                    "where template.templateId=:templateId ")
                .setLong("templateId", templateId)
                .uniqueResult();

            if (bean!=null) {
                session.delete(bean);
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

}
