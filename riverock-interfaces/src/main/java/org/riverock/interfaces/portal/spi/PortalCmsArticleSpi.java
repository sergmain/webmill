package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.dao.PortalCmsArticleDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:43:08
 * $Id$
 */
public interface PortalCmsArticleSpi extends PortalCmsArticleDao {
    List<Article> getArticleList(Long siteLanguageId, boolean isXml);

    Article getArticle(Long articleId);
    Article getArticleByCode(Long siteLanguageId, String articleCode);

    Long createArticle(Article article);

    void updateArticle(Article articleBean);

    void deleteArticle(Long articleId);
}
