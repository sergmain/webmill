package org.riverock.webmill.portal.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.webmill.portal.bean.PortletAliasBean;
import org.riverock.webmill.portal.bean.UrlAliasBean;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:27:38
 */
public class HibernateAliasDaoImpl implements InternalAliasDao {

    public PortletAlias getPortletAlias(Long portletAliasId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            PortletAliasBean bean = (PortletAliasBean)session.createQuery(
                "select alias from org.riverock.webmill.portal.bean.PortletAliasBean as alias " +
                    "where alias.portletAliasId = :portletAliasId")
                .setLong("portletAliasId", portletAliasId)
                .uniqueResult();
            return bean;
        }
        finally {
            session.close();
        }

    }

    private PortletAliasBean getPortletAliasInternal(Session session, Long siteId, String shortUrl) {
        List<PortletAliasBean> beans = session.createQuery(
            "select bean from org.riverock.webmill.portal.bean.PortletAliasBean as bean " +
                "where bean.shortUrl=:shortUrl and bean.siteId=:siteId"
        )
            .setString("shortUrl", shortUrl)
            .setLong("siteId", siteId)
            .list();
        PortletAliasBean bean=null;
        if (beans.size()>0) {
            bean = beans.get(0);
        }
        return bean;
    }

    public Long createPortletAlias(PortletAlias portletAlias) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            if (getPortletAliasInternal(session, portletAlias.getSiteId(), portletAlias.getShortUrl())==null) {
                PortletAliasBean bean = new PortletAliasBean(portletAlias);
                session.save(bean);
                session.flush();
                session.clear();
                session.getTransaction().commit();
                return bean.getPortletAliasId();
            }
            return null;
        }
        finally {
            session.close();
        }
    }

    public void updatePortletAlias(PortletAlias portletAlias) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            PortletAliasBean bean = (PortletAliasBean) session.createQuery(
                "select bean from org.riverock.webmill.portal.bean.PortletAliasBean as bean " +
                    "where bean.portletAliasId = :portletAliasId")
                .setLong("portletAliasId", portletAlias.getPortletAliasId())
                .uniqueResult();
            if (bean!=null) {
                bean.setPortletNameId(portletAlias.getPortletNameId());
                bean.setShortUrl(portletAlias.getShortUrl());
                bean.setTemplateId(portletAlias.getTemplateId());
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deletePortletAlias(PortletAlias portletAlias) {
        if (portletAlias==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.PortletAliasBean as bean " +
                    "where bean.portletAliasId = :portletAliasId")
                .setLong("portletAliasId", portletAlias.getPortletAliasId())
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public List<PortletAlias> getPortletAliases(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<PortletAliasBean> list = session.createQuery(
                "select bean from org.riverock.webmill.portal.bean.PortletAliasBean as bean " +
                    "where bean.siteId = :siteId ")
            .setLong("siteId", siteId)
                .list();
            return (List)list;
        }
        finally {
            session.close();
        }
    }

    public UrlAlias getUrlAlias(Long urlAliasId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            UrlAliasBean bean = (UrlAliasBean)session.createQuery(
                "select bean from org.riverock.webmill.portal.bean.UrlAliasBean as bean " +
                    "where bean.urlAliasId = :urlAliasId")
                .setLong("urlAliasId", urlAliasId)
                .uniqueResult();
            return bean;
        }
        finally {
            session.close();
        }
    }

    private UrlAliasBean getUrlAliasInternal(Session session, Long siteId, String url, String alias) {
        List<UrlAliasBean> beans = session.createQuery(
            "select bean from org.riverock.webmill.portal.bean.UrlAliasBean as bean " +
                "where bean.url=:url and bean.siteId=:siteId bean.alias=:alias")
            .setString("url", url)
            .setString("alias", alias)
            .setLong("siteId", siteId)
            .list();
        UrlAliasBean bean=null;
        if (beans.size()>0) {
            bean = beans.get(0);
        }
        return bean;
    }

    public Long createUrlAlias(UrlAlias urlAlias) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            if (getUrlAliasInternal(session, urlAlias.getSiteId(), urlAlias.getUrl(), urlAlias.getAlias())==null) {
                UrlAliasBean bean = new UrlAliasBean(urlAlias);
                session.save(bean);
                session.flush();
                session.clear();
                session.getTransaction().commit();
                return bean.getUrlAliasId();
            }
            return null;
        }
        finally {
            session.close();
        }
    }

    public void updateUrlAlias(UrlAlias urlAlias) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            UrlAliasBean bean = (UrlAliasBean) session.createQuery(
                "select bean from org.riverock.webmill.portal.bean.UrlAliasBean as bean " +
                    "where bean.urlAliasId = :urlAliasId")
                .setLong("urlAliasId", urlAlias.getUrlAliasId())
                .uniqueResult();
            if (bean!=null) {
                bean.setAlias(urlAlias.getAlias());
                bean.setUrl(urlAlias.getUrl());
                bean.setSiteId(urlAlias.getSiteId());
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteUrlAlias(UrlAlias urlAlias) {
        if (urlAlias==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.webmill.portal.bean.UrlAliasBean as bean " +
                    "where bean.urlAliasId = :urlAliasId")
                .setLong("urlAliasId", urlAlias.getUrlAliasId())
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public List<UrlAlias> getUrlAliases(Long siteId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<UrlAliasBean> list = session.createQuery(
                "select bean from org.riverock.webmill.portal.bean.UrlAliasBean as bean " +
                    "where bean.siteId=:siteId")
            .setLong("siteId", siteId)
                .list();
            return (List)list;
        }
        finally {
            session.close();
        }
    }
}
