package org.riverock.portlet.article;

import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.common.tools.StringTools;
import org.apache.log4j.Logger;

import javax.portlet.RenderRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 21.11.2006
 * Time: 1:33:40
 * <p/>
 * $Id$
 */
public class ArticleUtils {
    private final static Logger log = Logger.getLogger( ArticlePlain.class );

    static List<ClassQueryItem> getListInternal(RenderRequest renderRequest, Long idSiteCtxLangCatalog, Long idContext, boolean isPlain) {
        if( log.isDebugEnabled() ) {
            log.debug( "Get list of Article. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog );
        }

        List<ClassQueryItem> items = new ArrayList<ClassQueryItem>();

        PortalInfo portalInfo = ( PortalInfo ) renderRequest.getAttribute( ContainerConstants.PORTAL_INFO_ATTRIBUTE );
        Long siteLangaugeId = portalInfo.getSiteLanguageId( renderRequest.getLocale() );

        PortalDaoProvider provider =
            (PortalDaoProvider) renderRequest.getAttribute( ContainerConstants.PORTAL_PORTAL_DAO_PROVIDER );
        List<Article> articles = provider.getPortalCmsArticleDao().getArticleList(siteLangaugeId, !isPlain);
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
