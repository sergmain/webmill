package org.riverock.portlet.featured_article;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;

/**
 * User: SergeMaslyukov
 * Date: 14.10.2007
 * Time: 14:25:59
 * $Id$
 */
public class FeaturedArticlePortlet implements Portlet {
    private final static Logger log = Logger.getLogger(FeaturedArticlePortlet.class);

    public static final String[] FEATURE_ARTICLE_MANAGER_ROLES =
        new String[]{"webmill.portal-manager", "webmill.feature-article-manager"};
    public static final String FEATURE_ARTICLE_ERROR_MESSAGE = "errorMessage";
    public static final String RIVEROCK_WEBLICP_INDEX_JSP = "/jsp/index.jsp";
    public static final String FEATURE_ARTICLE_LIST = "articles";
    public static final String FEATURE_ARTICLE_ERROR_MESSAGE_PARAM = "feature_article_error_message";

    private static final String ARTICLE_TEXT_PARAM = "articleText";
    private static final String IMAGE_URL_PARAM = "imageUrl";
    private static final String THEME_PARAM = "theme";
    private static final String ARTICLE_ID_PARAM = "articleId";

    private PortletConfig portletConfig;

    public void init(PortletConfig portletConfig) throws PortletException {
        this.portletConfig=portletConfig;
    }

    public void destroy() {
        portletConfig=null;
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
        try {
            PortletUtils.checkRights(actionRequest, FEATURE_ARTICLE_MANAGER_ROLES);

            Long siteId = new Long( actionRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            String theme = actionRequest.getParameter(THEME_PARAM);
            if (theme==null) {
                actionResponse.setRenderParameter(FEATURE_ARTICLE_ERROR_MESSAGE, "'theme' parameter not defined.");
                return;
            }

            if (StringUtils.isNotBlank(actionRequest.getParameter("add-action"))) {
                log.debug("    execute 'add' action");
                addArticle(actionRequest, siteId, theme);
            }
            else if (StringUtils.isNotBlank(actionRequest.getParameter("save-action"))) {
                log.debug("    execute 'save' action");
                saveArticle(actionRequest, siteId);
            }
            else if (StringUtils.isNotBlank(actionRequest.getParameter("delete-action"))) {
                log.debug("    execute 'delete' action");
                deleteArticle(actionRequest, siteId);
            }
            else {
                throw new RuntimeException("Unknown action type");
            }
        }
        catch (Throwable e) {
            log.error("Error", e);
            actionResponse.setRenderParameter(FEATURE_ARTICLE_ERROR_MESSAGE_PARAM, e.toString());
        }
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        String theme = renderRequest.getParameter(THEME_PARAM);
        if (theme!=null) {
            renderRequest.setAttribute(FEATURE_ARTICLE_LIST, loadArticles(siteId, theme) );
        }
        else {
            renderRequest.setAttribute(FEATURE_ARTICLE_ERROR_MESSAGE, "'theme' parameter not defined.");
        }

        // set error of process action
        renderRequest.setAttribute(FEATURE_ARTICLE_ERROR_MESSAGE, PortletUtils.getString(renderRequest, FEATURE_ARTICLE_ERROR_MESSAGE_PARAM) );

        portletConfig.getPortletContext().getRequestDispatcher(RIVEROCK_WEBLICP_INDEX_JSP).include(renderRequest, renderResponse);
    }

    private List<FeaturedArticle> loadArticles(Long siteId, String theme) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            //noinspection unchecked
            return session.createQuery(
                "select bean from org.riverock.portlet.featured_article.FeaturedArticle as bean " +
                    "where bean.siteId=:siteId and bean.theme=:theme " +
                    "order by bean.createDate desc")
                .setLong("siteId", siteId)
                .setString("theme", theme)
                .list();
        }
        finally {
            session.close();
        }
    }

    private void addArticle(ActionRequest actionRequest, Long siteId, String theme) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            FeaturedArticle bean = new FeaturedArticle();
            bean.setSiteId(siteId);
            bean.setTheme(theme);
            bean.setArticleText(actionRequest.getParameter(ARTICLE_TEXT_PARAM));
            bean.setCreateDate(new Date());
            bean.setImageUrl(actionRequest.getParameter(IMAGE_URL_PARAM));

            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private void saveArticle(ActionRequest actionRequest, Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            FeaturedArticle bean = (FeaturedArticle)session.createQuery(
                "select bean from org.riverock.portlet.featured_article.FeaturedArticle as bean " +
                    "where bean.articleId=:articleId and bean.siteId=:siteId")
                .setLong("articleId", PortletUtils.getLong(actionRequest, ARTICLE_ID_PARAM))
                .setLong("siteId", siteId)
                .uniqueResult();
            if (bean!=null) {
                bean.setArticleText(actionRequest.getParameter(ARTICLE_TEXT_PARAM));
                bean.setImageUrl(actionRequest.getParameter(IMAGE_URL_PARAM));
            }
            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private void deleteArticle(ActionRequest actionRequest, Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            session.createQuery(
            "delete org.riverock.portlet.featured_article.FeaturedArticle as bean " +
                    "where bean.articleId=:articleId and bean.siteId=:siteId")
                .setLong("articleId", PortletUtils.getLong(actionRequest, ARTICLE_ID_PARAM))
                .setLong("siteId", siteId)
                .executeUpdate();

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
