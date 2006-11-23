package org.riverock.portlet.article;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.portlet.main.ClassQueryItemImpl;

/**
 * User: SergeMaslyukov
 * Date: 21.11.2006
 * Time: 1:33:40
 * <p/>
 * $Id$
 */
public class ArticleUtils {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    static List<ClassQueryItem> getListInternal(PortalDaoProvider provider, Long idSiteCtxLangCatalog, Long idContext, boolean isPlain) {
        if( log.isDebugEnabled() ) {
            log.debug( "Get list of Article. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );
        }

        List<ClassQueryItem> items = new ArrayList<ClassQueryItem>();

        CatalogLanguageItem catalogLanguage = provider.getPortalCatalogDao().getCatalogLanguageItem(idSiteCtxLangCatalog);
        List<Article> articles = provider.getPortalCmsArticleDao().getArticleList(catalogLanguage.getSiteLanguageId(), !isPlain);
        for (Article bean : articles) {
            String name = "" + bean.getArticleId() + ", " +
                bean.getArticleCode() + ", " +
                bean.getArticleName();

            ClassQueryItem item = new ClassQueryItemImpl(
                bean.getArticleId(), StringTools.truncateString( name, 60 ) );

            if( item.getIndex().equals( idContext ) )
                item.setSelected( true );

            items.add( item );
        }
        return items;
    }
}
