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

 * Date: Jan 10, 2003

 * Time: 9:41:20 AM

 *

 * $Id$

 */



package org.riverock.portlet.portlets;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Calendar;

import java.util.List;

import java.util.ArrayList;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.main.CacheFactory;

import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.common.tools.StringTools;

import org.riverock.webmill.portlet.Portlet;

import org.riverock.webmill.portlet.PortletResultObject;

import org.riverock.webmill.portlet.PortletGetList;

import org.riverock.webmill.portlet.PortletParameter;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.portlet.schema.core.SiteCtxArticleItemType;

import org.riverock.portlet.core.GetSiteCtxArticleItem;

import org.riverock.portlet.member.ClassQueryItem;

import org.riverock.cache.impl.CacheException;



import org.apache.log4j.Logger;



public class ArticleXml implements Portlet, PortletResultObject, PortletGetList

{

    private static Logger log = Logger.getLogger( ArticleXml.class );



    private static CacheFactory cache = new CacheFactory( ArticleXml.class.getName() );



    public Calendar datePost = null;

    public String nameArticle = "";

    public String text = "";

    public Long id = null;

    public boolean isTranslateCR = false;

    public boolean isPlainHTML = false;

    public boolean isSimpleTextBlock = false;

    public String articleCode = "";

    public PortletParameter param = null;

    public Long idSupportLanguage = null;



    public void reinit()

    {

        cache.reinit();

    }



    public synchronized void terminate(Long id)

        throws CacheException

    {

        cache.terminate(id);

    }



    protected void finalize() throws Throwable

    {

        datePost = null;

        nameArticle = null;

        text = null;

        articleCode = null;

        param = null;



        super.finalize();

    }



    public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public PortletResultObject getInstance(DatabaseAdapter db__) throws Exception

    {

        return null;

    }



    public byte[] getXml(String name)

        throws Exception

    {

        if(log.isDebugEnabled())

            log.debug("ArticleXml. method is 'Xml'");



        return getXml();

    }



    public byte[] getXml()

        throws Exception

    {

        if(log.isDebugEnabled())

            log.debug("ArticleXml. method is 'Xml'");



        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+

                "<Article><ArticleDate>"+ getArticleDate() + "</ArticleDate>"+

                "<ArticleTime>"+ getArticleTime()+ "</ArticleTime>"+

                "<ArticleName>"+ getArticleName()+ "</ArticleName>"+

                "<ArticleText>"+ getArticleText()+"</ArticleText></Article>";



        if (log.isDebugEnabled())

            log.debug( "ArticleXml. getXml - "+xml );



        return xml.getBytes( WebmillConfig.getHtmlCharset() );

    }



    public boolean isXml(){ return true; }

    public boolean isHtml(){ return false; }



    public String getArticleDate()

    {

        return DateTools.getStringDate(datePost, "dd.MMM.yyyy", param.getPage().currentLocale);

    }



    public String getArticleTime()

    {

        return DateTools.getStringDate(datePost, "HH:mm", param.getPage().currentLocale);

    }



    public String getArticleName()

    {

        return nameArticle;

    }



    public String getArticleText()

    {

        if(log.isDebugEnabled())

            log.debug("ArticleXml text - "+text);



        String s = text;



        if ( isTranslateCR )

        {

            s = StringTools.prepareToParsingSimple( s );



            if (log.isDebugEnabled() )

                log.debug("isTranslateCR - "+isTranslateCR);

        }



        if ( isSimpleTextBlock )

        {

            if (log.isDebugEnabled() )

                log.debug("isSimpleTextBlock - "+isSimpleTextBlock);



            s = StringTools.toOrigin(s );

        }



        if(log.isDebugEnabled())

            log.debug("ArticleXml. result text - "+s);



        return s;

    }



    public int maxCountItems()

    {

        return 50;

    };

    public long maxTimePeriod()

    {

        return 1000000;

    };



    public String getNameClass()

    {

        return ArticleXml.class.getName();

    };



    public ArticleXml()

    {

    }



    public PortletResultObject getInstance(DatabaseAdapter db__, long id__)

            throws Exception

    {

        return getInstance(db__, new Long(id__) );

    }



    public PortletResultObject getInstance(DatabaseAdapter db__, Long id__)

            throws Exception

    {

        try

        {

            PortletResultObject portletObject = (PortletResultObject) cache.getInstanceNew(db__, id__);

            ((Portlet)portletObject).setParameter( this.param );

            return portletObject;

        }

        catch(Exception e)

        {

            log.error("Error get instance of ArticleXml", e);

            throw e;

        }

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select a.ID_SITE_CTX_ARTICLE " +

            "from SITE_CTX_ARTICLE a " +

            "where a.ID_SITE_SUPPORT_LANGUAGE=? and a.ARTICLE_CODE=? and a.IS_DELETED=0";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new ArticleXml().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



    public PortletResultObject getInstanceByCode(DatabaseAdapter db__, String articleCode_)

            throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("#10.01.01 " + articleCode_);



        Long idSupportLanguageCurrent = param.getPage().p.getIdSupportLanguage(param.getPage().currentLocale);



        //Todo Need optimize

//        for (int i = 0; i < dummy.maxCountItems(); i++)

//        {

//

//            if (cache[i] != null &&

//                ((ArticleXml) cache[i]).articleCode.equals(articleCode_) &&

//                ((ArticleXml) cache[i]).idSupportLanguage == idSupportLanguageCurrent

//                )

//            {

//                if (log.isDebugEnabled())

//                    log.debug("#10.01.03");

//

//                return (ArticleXml) cache[i];

//            }

//        }



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            ps = db__.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSupportLanguageCurrent );

            ps.setString(2, articleCode_);



            rs = ps.executeQuery();

            if (rs.next())

            {

                if (log.isDebugEnabled())

                    log.debug("#10.01.04 " + RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));



                return getInstance(db__, RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE"));

            }



            if (log.isDebugEnabled())

                log.debug("#10.01.05 ");



            // return dummy Article

            return getInstance(db__, -1);

        }

        catch(Exception e)

        {

            log.error("Exception in ArticleXml.getInstanceByCode()", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in ArticleXml.getInstanceByCode()", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



    static String sql1_ = null;

    static

    {

        sql1_ =

            "select * from SITE_CTX_ARTICLE where ID_SITE_CTX_ARTICLE=? and IS_DELETED=0";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql1_, new ArticleXml().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql1_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql1_, e);

        }

    }



    public ArticleXml(DatabaseAdapter db_, Long id_)

            throws Exception

    {

        SiteCtxArticleItemType article = GetSiteCtxArticleItem.getInstance(db_, id_).item;



        if (article==null || Boolean.TRUE.equals( article.getIsDeleted()) )

            return;



        PreparedStatement ps = null;

        ResultSet rs = null;

        id = id_;

        try

        {

            ps = db_.prepareStatement(sql1_);

            RsetTools.setLong(ps, 1, id);



            rs = ps.executeQuery();

            if (rs.next())

            {

                datePost = RsetTools.getCalendar(rs, "DATE_POST");

                nameArticle = RsetTools.getString(rs, "NAME_ARTICLE");

                isTranslateCR = Boolean.TRUE.equals(RsetTools.getInt(rs, "IS_TRANSLATE_CR"));

                isPlainHTML = Boolean.TRUE.equals(RsetTools.getInt(rs, "IS_PLAIN_HTML") );

                isSimpleTextBlock = Boolean.TRUE.equals(RsetTools.getInt(rs, "IS_SIMPLE_TEXT") );

                articleCode = RsetTools.getString(rs, "ARTICLE_CODE");

                idSupportLanguage = RsetTools.getLong(rs, "ID_SITE_SUPPORT_LANGUAGE");



                initTextField();

            }

        }

        catch(Exception e)

        {

            log.error("Exception in ArticleXml(..", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in ArticleXml(..", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }



    }



    static String sql2_ = null;

    static

    {

        sql2_ =

            "select ARTICLE_DATA " +

            "from SITE_CTX_ARTICLE_DATA " +

            "where ID_SITE_CTX_ARTICLE = ? " +

            "order by ID_SITE_CTX_ARTICLE_DATA ASC";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql2_, new ArticleXml().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql2_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql2_, e);

        }

    }



    public void initTextField()

            throws Exception

    {

        if (id == null)

            return;



        DatabaseAdapter db_ = null;

        PreparedStatement ps = null;

        ResultSet rset = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement( sql2_ );



            RsetTools.setLong(ps, 1, id);

            rset = ps.executeQuery();

            text = "";

            while (rset.next())

            {

                text += RsetTools.getString(rset, "ARTICLE_DATA");

            }

        }

        catch(Exception e)

        {

            log.error("Exception in ArticleXml.initTextField", e);

            throw e;

        }

        catch(Error e)

        {

            log.error("Error in ArticleXml.initTextField", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(db_, rset, ps);

            rset = null;

            ps = null;

            db_ = null;

        }

    }



    public byte[] getPlainHTML()

    {

        return null;

    }



    static String sql3_ = null;

    static

    {

        sql3_ =

            "SELECT b.ID_SITE_CTX_ARTICLE, b.NAME_ARTICLE, b.ARTICLE_CODE "+

            "FROM site_ctx_lang_catalog a, site_ctx_article b "+

            "where a.ID_SITE_CTX_LANG_CATALOG=? and "+

            "a.ID_SITE_SUPPORT_LANGUAGE=b.ID_SITE_SUPPORT_LANGUAGE and "+

            "b.IS_PLAIN_HTML=0";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql3_, new ArticleXml().getClass() );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql3_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql3_, e);

        }

    }



    public List getList( Long idSiteCtxLangCatalog, Long idContext)

    {

        if (log.isDebugEnabled())

            log.debug("Get list of ArticleXml. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog);



        PreparedStatement ps = null;

        ResultSet rs = null;

        DatabaseAdapter db_ = null;



        List v = new ArrayList();

        try

        {

            db_ = DatabaseAdapter.getInstance( false );

            ps = db_.prepareStatement( sql3_ );



            RsetTools.setLong(ps, 1, idSiteCtxLangCatalog );



            rs = ps.executeQuery();

            while (rs.next())

            {

                Long id = RsetTools.getLong(rs, "ID_SITE_CTX_ARTICLE");

                String name = ""+id + ", "+

                        RsetTools.getString(rs, "ARTICLE_CODE")+ ", "+

                        RsetTools.getString(rs, "NAME_ARTICLE");



                ClassQueryItem item =

                        new ClassQueryItem(id, StringTools.truncateString(name, 60) );



                if (item.index.equals(idContext) )

                    item.isSelected = true;



                v.add( item );

            }

            return v;



        }

        catch(Exception e)

        {

            log.error("Exception Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);

            return null;

        }

        catch(Error e)

        {

            log.error("Error Get list of ArticlePlain. idSiteCtxLangCatalog - " + idSiteCtxLangCatalog, e);

            return null;

        }

        finally

        {

            DatabaseManager.close(db_, rs, ps);

            rs = null;

            ps = null;

            db_ = null;

        }

    }

}

