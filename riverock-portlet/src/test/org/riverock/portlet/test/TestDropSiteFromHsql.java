/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * Author: mill
 * Date: Mar 18, 2003
 * Time: 3:35:38 PM
 *
 * $Id$
 */

package org.riverock.portlet.test;

import org.apache.log4j.Logger;

public class TestDropSiteFromHsql
{
    private static Logger cat = Logger.getLogger( "org.riverock.portlet.test.TestDropSiteFromHsql" );

    private final static String nameConnection = "HSQLDB";
    private final static String  nameVirtualHost = "localhost";

    public TestDropSiteFromHsql()
    {
    }


    public static void main(String args[])
            throws Exception
    {
/*
        StartupApplication.init();

        WebmillConfig conf = null;
        DatabaseAdapter db_ = null;
        long idSite;

        conf = InitParam.getWebmillConfig();
        conf.setDefaultConnectionName( "ORACLE_WEBMILL" );
        db_ = DatabaseAdapter.getInstance(false);
        System.out.println("db conn - "+db_.conn.getClass().getName());

        idSite = SiteListSite.getInstance().getLongIdSite( "webmill.askmore.info" );
        System.out.println("idSite - "+idSite);
        SiteExtendItemType site = SiteService.getExtendData(db_, idSite);

        XmlTools.writeToFile(
            site,
            WebmillConfig.getWebmillDebugDir()+"test-site-extend-data.xml",
            "utf-8",
            null,
            org.riverock.webmill.site.WebmillNamespace.webmillNamespace
        );

        CacheItemV2.reinitFullCache();
        SiteListSite.getInstance().reinit();

        conf = InitParam.getWebmillConfig();
        conf.setDefaultConnectionName( nameConnection );

        db_ = DatabaseAdapter.getInstance(false, nameConnection);
        idSite = SiteListSite.getInstance().getLongIdSite( nameVirtualHost );
        if (idSite!=-1)
        {
            SiteService.dropSite(db_, idSite );
        }

//        if (true)return;

        System.out.println("db conn - "+DatabaseAdapter.getInstance(false).conn.getClass().getName());

        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_SITE_LIST_SITE");
        seq.setTableName( "SITE_LIST_SITE");
        seq.setColumnName( "ID_SITE" );
        long idSiteNew = db_.getSequenceNextValue( seq );

        site.setIdSite( idSiteNew );
        InsertSiteListSiteItem.processData(db_, site);

        insertVirtualHost(db_, idSiteNew, nameVirtualHost);
        insertVirtualHost(db_, idSiteNew, "www.me.askmore");

        insertCtxTypes( db_, site );

        SiteContentCssItemType css = null;
        for (int i=0; i<site.getCss().getSiteContentCssCount(); i++)
        {
            SiteContentCssItemType cssTemp = site.getCss().getSiteContentCss(i);
            if (cssTemp.getIsCurrent())
            {
                css = cssTemp;
                break;
            }
        }
        if (css !=null)
        {
            css.setIdSite( idSiteNew );
            String cssData = css.getCssData();
            css.setCssData( "" );
            seq.setSequenceName("SEQ_SITE_CONTENT_CSS");
            seq.setTableName( "SITE_CONTENT_CSS");
            seq.setColumnName( "ID_SITE_CONTENT_CSS" );
            long idCssNew = db_.getSequenceNextValue( seq );
            css.setIdSiteContentCss( idCssNew );

            InsertSiteContentCssItem.processData(db_, css);

            DatabaseManager.insertBigText(db_, new Long(idCssNew),
                "ID_SITE_CONTENT_CSS",
                PrimaryKeyTypeType.NUMBER_TYPE,
                "SITE_CONTENT_CSS_DATA",
                "ID_SITE_CONTENT_CSS_DATA",
                "CSS_DATA",
                cssData,
                false
            );
        }

        for (int i=0; i<site.getSiteLanguage().getItemCount(); i++)
        {
            SiteExtendLanguageType siteLangTemp = site.getSiteLanguage().getItem(i);
            seq.setSequenceName("SEQ_SITE_SUPPORT_LANGUAGE");
            seq.setTableName( "SITE_SUPPORT_LANGUAGE");
            seq.setColumnName( "ID_SITE_SUPPORT_LANGUAGE" );
            long idSiteLangNew = db_.getSequenceNextValue( seq );
            siteLangTemp.setIdSiteSupportLanguage( idSiteLangNew );
            siteLangTemp.setIdSite( idSiteNew );
            InsertSiteSupportLanguageItem.processData( db_, siteLangTemp );

            // вставляем статьи
            insertArticle( db_, siteLangTemp, siteLangTemp.getArticleXml(), site, true);
            insertArticle( db_, siteLangTemp, siteLangTemp.getArticlePlain(), site, false );

            // вставляем шаблоны.
            // вставляем перед меню т.к. надо изменить ссылки в меню
            SiteTemplateListType templateList = siteLangTemp.getTemplate();
            for (int j=0; j<templateList.getSiteTemplateCount(); j++)
            {
                SiteTemplateItemType templateItem = templateList.getSiteTemplate(j);
                seq.setSequenceName("SEQ_SITE_TEMPLATE");
                seq.setTableName( "SITE_TEMPLATE");
                seq.setColumnName( "ID_SITE_TEMPLATE" );
                long idTemplateNew = db_.getSequenceNextValue( seq );

                updateIdTemplate(siteLangTemp, templateItem.getIdSiteTemplate(), idTemplateNew);

                templateItem.setIdSiteTemplate( idTemplateNew );
                templateItem.setIdSiteSupportLanguage( idSiteLangNew );
                InsertSiteTemplateItem.processData(db_, templateItem);

            }

            SiteTemplateMemberItemType memberTemplate = siteLangTemp.getMemberTemplate();
            if (memberTemplate!=null)
            {
                memberTemplate.setIdSiteSupportLanguage( idSiteLangNew );
                seq.setSequenceName("SEQ_SITE_TEMPLATE_MEMBER");
                seq.setTableName( "SITE_TEMPLATE_MEMBER");
                seq.setColumnName( "ID_SITE_TEMPLATE_MEMBER" );
                long idMemberTemplateNew = db_.getSequenceNextValue( seq );
                memberTemplate.setIdSiteTemplateMember( idMemberTemplateNew );
                InsertSiteTemplateMemberItem.processData(db_, memberTemplate);
            }

            // вставляем новости
            SiteExtendNewsGroupListType newsGroupList = siteLangTemp.getNewsGroup();
            for (int j=0; j<newsGroupList.getGroupCount(); j++)
            {
                SiteExtendNewsGroupType newsGroup = newsGroupList.getGroup(j);
                seq.setSequenceName("SEQ_MAIN_LIST_NEWS");
                seq.setTableName( "MAIN_LIST_NEWS");
                seq.setColumnName( "ID_NEWS" );
                long idNewsGroupNew = db_.getSequenceNextValue( seq );

                newsGroup.setIdNews( idNewsGroupNew );
                newsGroup.setIdSiteSupportLanguage( idSiteLangNew );
                InsertMainListNewsItem.processData(db_, newsGroup );

                for (int k=0; k<newsGroup.getNewsItem().getMainNewsCount(); k++)
                {
                    MainNewsItemType newsItem = newsGroup.getNewsItem().getMainNews(k);
                    String newsText = newsItem.getText();
                    newsItem.setText( "" );
                    seq.setSequenceName("SEQ_MAIN_NEWS");
                    seq.setTableName( "MAIN_NEWS");
                    seq.setColumnName( "ID" );
                    long idNewsNew = db_.getSequenceNextValue( seq );
                    newsItem.setId( idNewsNew );
                    newsItem.setIdNews( idNewsGroupNew );

                    InsertMainNewsItem.processData(db_, newsItem );

                    DatabaseManager.insertBigText(db_, new Long(idNewsNew),
                        "ID",
                        PrimaryKeyTypeType.NUMBER_TYPE,
                        "MAIN_NEWS_TEXT",
                        "ID_MAIN_NEWS_TEXT",
                        "TEXT",
                        newsText,
                        false
                    );
                }
            }

            // вставляем меню
            // меню должно вставляться в последнюю очередь
            SiteExtendCtxLangCatalogListType catalogLangList = siteLangTemp.getContext();
            for (int j=0; j<catalogLangList.getItemCount();j++ )
            {
                SiteExtendCtxLangCatalogType catalogLangItem = catalogLangList.getItem(j);
                seq.setSequenceName("SEQ_SITE_CTX_LANG_CATALOG");
                seq.setTableName( "SITE_CTX_LANG_CATALOG");
                seq.setColumnName( "ID_SITE_CTX_LANG_CATALOG" );
                long idCtxLangCatalogNew = db_.getSequenceNextValue( seq );
                catalogLangItem.setIdSiteCtxLangCatalog( idCtxLangCatalogNew );
                catalogLangItem.setIdSiteSupportLanguage( idSiteLangNew );
                InsertSiteCtxLangCatalogItem.processData(db_, catalogLangItem );

                SiteCtxCatalogListType catalogList = catalogLangItem.getContextList();
                for (int k=0; k<catalogList.getSiteCtxCatalogCount(); k++)
                {
                    SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(k);
                    seq.setSequenceName("SEQ_SITE_CTX_CATALOG");
                    seq.setTableName( "SITE_CTX_CATALOG");
                    seq.setColumnName( "ID_SITE_CTX_CATALOG" );
                    long idCtxCatalogNew = db_.getSequenceNextValue( seq );

                    updateIdTopCatalog(catalogList, catalogItem.getIdSiteCtxCatalog(), idCtxCatalogNew);

                    catalogItem.setIdSiteCtxCatalog( idCtxCatalogNew );
                    catalogItem.setIdSiteCtxLangCatalog( idCtxLangCatalogNew );
                    InsertSiteCtxCatalogItem.processData(db_, catalogItem);
                }
            }

            SiteXsltListType xsltList = siteLangTemp.getXslt();
            for (int j=0; j<xsltList.getSiteXsltCount(); j++)
            {
                SiteXsltItemType xsltItem = xsltList.getSiteXslt(j);
                String xsltData = xsltItem.getXslt();
                xsltItem.setXslt("");
                seq.setSequenceName("SEQ_SITE_XSLT");
                seq.setTableName( "SITE_XSLT");
                seq.setColumnName( "ID_SITE_XSLT" );
                long idXsltNew = db_.getSequenceNextValue( seq );
                xsltItem.setIdSiteXslt( idXsltNew );
                xsltItem.setIdSiteSupportLanguage( idSiteLangNew );
                InsertSiteXsltItem.processData(db_, xsltItem);

                DatabaseManager.insertBigText(db_, new Long(idXsltNew),
                    "ID_SITE_XSLT",
                    PrimaryKeyTypeType.NUMBER_TYPE,
                    "SITE_XSLT_DATA",
                    "ID_SITE_XSLT_DATA",
                    "XSLT",
                    xsltData,
                    false
                );
            }
        }
        db_.commit();
        site = SiteService.getExtendData(db_, idSite);
        XmlTools.writeToFile(
            site, WebmillConfig.getWebmillDebugDir()+"result-webmill.xml", "utf-8", null,
            WebmillNamespace.webmillNamespace
        );
        */
    }
/*
    private static void insertCtxTypes(DatabaseAdapter db_, SiteExtendItemType site)
        throws Exception
    {

        // получаем текущие значения типов контекстов из базы данных
        // куда заливаем новые данные
        SiteCtxTypeListType ctxTypeTargetList = GetSiteCtxTypeFullList.getInstance(db_, 0).item;

        // теперь крутим цикл по питам контекстов в исходных данных
        for (int i=0; i<site.getCtxType().getSiteCtxTypeCount(); i++)
        {
            SiteCtxTypeItemType ctxTypeSource = site.getCtxType().getSiteCtxType(i);

            SiteCtxTypeItemType ctxTypeTarget = null;
            // ищем в целевых данных очередной тип контекста из изходных данных
            for (int j=0; j<ctxTypeTargetList.getSiteCtxTypeCount(); j++)
            {
                SiteCtxTypeItemType ctxItemTemp = ctxTypeTargetList.getSiteCtxType(j);
                if (ctxItemTemp.getDefaultPortletType().equals(ctxTypeSource.getDefaultPortletType()))
                {
                    ctxTypeTarget = ctxItemTemp;
                    break;
                }
            }

//            System.out.println(
//                "Looking for ctx type "+ctxTypeSource.getDefaultPortletType()+", id " + ctxTypeSource.getIdSiteCtxType()+
//                " result - "+
//                (ctxTypeTarget==null?"null":"type - "+ctxTypeTarget.getDefaultPortletType()+", id - "+ctxTypeTarget.getIdSiteCtxType() )
//            );

            // если в целевых даных нет такого типа контекста(т.е. это новый тип контекста)
            // вставляем его
            if (ctxTypeTarget==null)
            {
                // проверяем не занят ли id
                SiteCtxTypeItemType item =
                    GetSiteCtxTypeItem.getInstance(db_, ctxTypeSource.getIdSiteCtxType()).item;

                // если id исходного типа данных не занят, то просто вставляен новый тип
                if (item==null)
                    InsertSiteCtxTypeItem.processData(db_, ctxTypeSource);
                else
                {
                    // иначе нолучаем новый id и вставляем его в целевую базу
                    CustomSequenceType seq = new CustomSequenceType();
                    seq.setSequenceName( "SEQ_SITE_CTX_TYPE" );
                    seq.setTableName( "SITE_CTX_TYPE" );
                    seq.setColumnName( "ID_SITE_CTX_TYPE" );
                    long idNew = db_.getSequenceNextValue( seq );

                    replaceIdCtxType( site, ctxTypeSource.getIdSiteCtxType(), idNew );

                    ctxTypeSource.setIdSiteCtxType( idNew );
                    InsertSiteCtxTypeItem.processData(db_, ctxTypeSource);
                }
            }
            else
            {
                // уже выяснили что исходный тип контекста есть в целевой базе данных
                // теперь проверяем, значения ключей. Если они равны, то ничего не делаем
                if ( ctxTypeSource.getIdSiteCtxType()!= ctxTypeTarget.getIdSiteCtxType())
                {
                    replaceIdCtxType( site, ctxTypeSource.getIdSiteCtxType(), ctxTypeTarget.getIdSiteCtxType() );
                    ctxTypeSource.setIdSiteCtxType( ctxTypeTarget.getIdSiteCtxType() );
                }
            }
        }

    }

    private static void replaceIdCtxType(SiteExtendItemType site, long idCtxTypeOld, long idCtxTypeNew)
    {
        for (int k=0; k<site.getSiteLanguage().getItemCount();k++ )
        {
            SiteExtendLanguageType siteLang = site.getSiteLanguage().getItem(k);

            for (int j=0; j<siteLang.getContext().getItemCount();j++ )
            {
                SiteExtendCtxLangCatalogType catalogLangItem = siteLang.getContext().getItem(j);
                SiteCtxCatalogListType catalogList = catalogLangItem.getContextList();
                for (int l=0; l<catalogList.getSiteCtxCatalogCount(); l++)
                {
                    SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(l);

//                    System.out.println(
//                        "#1 id " +catalogItem.getIdContext()+
//                        ", old "+ idCtxTypeOld +
//                        ", new "+ idCtxTypeNew
//                    );

                    if (catalogItem.getIdSiteCtxType()==idCtxTypeOld )
                    {
                        catalogItem.setIdSiteCtxType( idCtxTypeNew );
                    }
                }
            }
        }
    }

    private static void insertVirtualHost(DatabaseAdapter db_, long idSiteNew, String serverName) throws Exception
    {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("SEQ_SITE_VIRTUAL_HOST");
        seq.setTableName( "SITE_VIRTUAL_HOST");
        seq.setColumnName( "ID_SITE_VIRTUAL_HOST" );
        long idVirtualHostNew = db_.getSequenceNextValue( seq );
        SiteVirtualHostItemType virtualHost = new SiteVirtualHostItemType();
        virtualHost.setIdSite( idSiteNew );
        virtualHost.setIdSiteVirtualHost( idVirtualHostNew );
        virtualHost.setNameVirtualHost( serverName );
        InsertSiteVirtualHostItem.processData(db_, virtualHost);
    }

    private static void updateIdTemplate( SiteExtendLanguageType siteLangTemp, long idOld, long idNew)
    {
        SiteExtendCtxLangCatalogListType catalogLangList = siteLangTemp.getContext();
        for (int j=0; j<catalogLangList.getItemCount();j++ )
        {
            SiteExtendCtxLangCatalogType catalogLangItem = catalogLangList.getItem(j);
            SiteCtxCatalogListType catalogList = catalogLangItem.getContextList();
            for (int k=0; k<catalogList.getSiteCtxCatalogCount(); k++)
            {
                SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(k);
                if (catalogItem.getIdSiteTemplate()==idOld)
                    catalogItem.setIdSiteTemplate( idNew );
            }
        }
        SiteTemplateMemberItemType memberTemplate = siteLangTemp.getMemberTemplate();
        if (memberTemplate!=null && memberTemplate.getIdSiteTemplate()==idOld )
            memberTemplate.setIdSiteTemplate( idNew );
    }

    private static long getIdCtxType( SiteExtendItemType site, String portletCode)
    {

        for (int k=0; k<site.getCtxType().getSiteCtxTypeCount(); k++)
        {
            SiteCtxTypeItemType ctxType = site.getCtxType().getSiteCtxType(k);
            if (ctxType.getDefaultPortletType().equals(portletCode))
                return ctxType.getIdSiteCtxType();
        }
        return -1;
    }

    private static void updateIdArticleXml(
        SiteExtendCtxLangCatalogListType catalogLangList, SiteExtendItemType site, long idOld, long idNew
        )
    {

        long idCtxType = getIdCtxType( site, "mill.article_xml");
//        System.out.println("ctx type 'mill.article_xml', id "+idCtxType);
        // PK не может быть равен 0
        if (idCtxType==-1 || idCtxType==0)
            return;

        for (int j=0; j<catalogLangList.getItemCount();j++ )
        {
            SiteExtendCtxLangCatalogType catalogLangItem = catalogLangList.getItem(j);
            SiteCtxCatalogListType catalogList = catalogLangItem.getContextList();
            for (int k=0; k<catalogList.getSiteCtxCatalogCount(); k++)
            {
                SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(k);
                if (catalogItem.getIdSiteCtxType()==idCtxType && catalogItem.getIdContext()==idOld)
                    catalogItem.setIdContext( idNew );
            }
        }
    }

    private static void updateIdArticlePlain(
        SiteExtendCtxLangCatalogListType catalogLangList, SiteExtendItemType site, long idOld, long idNew
        )
    {

        long idCtxType = getIdCtxType( site, "mill.article_plain");
        // PK не может быть равен 0
        if (idCtxType==-1 || idCtxType==0)
        {
            System.out.println("ctx type 'mill.article_plain' not found");
            return;
        }

        for (int j=0; j<catalogLangList.getItemCount();j++ )
        {
            SiteExtendCtxLangCatalogType catalogLangItem = catalogLangList.getItem(j);
            SiteCtxCatalogListType catalogList = catalogLangItem.getContextList();
            for (int k=0; k<catalogList.getSiteCtxCatalogCount(); k++)
            {
                SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(k);
                if (catalogItem.getIdSiteCtxType()==idCtxType && catalogItem.getIdContext()==idOld)
                    catalogItem.setIdContext( idNew );
            }
        }
    }

    private static void updateIdTopCatalog( SiteCtxCatalogListType catalogList, long idOld, long idNew)
    {
        for (int k=0; k<catalogList.getSiteCtxCatalogCount(); k++)
        {
            SiteCtxCatalogItemType catalogItem = catalogList.getSiteCtxCatalog(k);

//            System.out.println(
//                "#1 id " +catalogItem.getIdSiteCtxCatalog()+
//                ", old "+ idOld +
//                ", new "+ idNew
//            );

            if ( catalogItem.getIdTopCtxCatalog()==idOld )
                catalogItem.setIdTopCtxCatalog( idNew );
        }
    }

    public static void insertContext( DatabaseAdapter db_, SiteExtendLanguageType siteLang, SiteCtxArticleListType articleList )
        throws Exception
    {
        CustomSequenceType seq = new CustomSequenceType();
        for (int i=0; i<articleList.getSiteCtxArticleCount(); i++)
        {
            seq.setSequenceName( "SEQ_SITE_CTX_ARTICLE" );
            seq.setTableName( "SITE_CTX_ARTICLE");
            seq.setColumnName( "ID_SITE_CTX_ARTICLE" );
            long idArticleNew = db_.getSequenceNextValue( seq );

            SiteCtxArticleItemType articleItem = articleList.getSiteCtxArticle(i);
            String articleData = articleItem.getArticleData();
            articleItem.setArticleData( "" );
            articleItem.setIdSiteCtxArticle( idArticleNew );
            articleItem.setIdSiteSupportLanguage( siteLang.getIdSiteSupportLanguage() );
            InsertSiteCtxArticleItem.processData( db_, articleItem );

            DatabaseManager.insertBigText(db_, new Long(idArticleNew),
                "ID_SITE_CTX_ARTICLE",
                PrimaryKeyTypeType.NUMBER_TYPE,
                "SITE_CTX_ARTICLE_DATA",
                "ID_SITE_CTX_ARTICLE_DATA",
                "ARTICLE_DATA",
                articleData,
                false
            );
        }
    }

    public static void insertArticle(
        DatabaseAdapter db_, SiteExtendLanguageType siteLang, SiteCtxArticleListType articleList,
        SiteExtendItemType site, boolean isXml)
        throws Exception
    {
        CustomSequenceType seq = new CustomSequenceType();
        for (int i=0; i<articleList.getSiteCtxArticleCount(); i++)
        {
            SiteCtxArticleItemType articleItem = articleList.getSiteCtxArticle(i);

            seq.setSequenceName( "SEQ_SITE_CTX_ARTICLE" );
            seq.setTableName( "SITE_CTX_ARTICLE");
            seq.setColumnName( "ID_SITE_CTX_ARTICLE" );
            long idArticleNew = db_.getSequenceNextValue( seq );

            if (isXml)
                updateIdArticleXml(
                    siteLang.getContext(), site,
                    articleItem.getIdSiteCtxArticle(),
                    idArticleNew
                );
            else
                updateIdArticlePlain(
                    siteLang.getContext(), site,
                    articleItem.getIdSiteCtxArticle(),
                    idArticleNew
                );

            String articleData = articleItem.getArticleData();
            articleItem.setArticleData( "" );
            articleItem.setIdSiteCtxArticle( idArticleNew );
            articleItem.setIdSiteSupportLanguage( siteLang.getIdSiteSupportLanguage() );
            InsertSiteCtxArticleItem.processData( db_, articleItem );

            DatabaseManager.insertBigText(db_, new Long(idArticleNew),
                "ID_SITE_CTX_ARTICLE",
                PrimaryKeyTypeType.NUMBER_TYPE,
                "SITE_CTX_ARTICLE_DATA",
                "ID_SITE_CTX_ARTICLE_DATA",
                "ARTICLE_DATA",
                articleData,
                false
            );
        }
    }
*/
}
