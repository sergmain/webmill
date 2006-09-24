/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * User: Admin
 * Date: Feb 18, 2003
 * Time: 9:15:48 PM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.news.NewsSite;
import org.riverock.portlet.schema.portlet.news_block.NewsBlockType;
import org.riverock.portlet.schema.portlet.news_block.NewsGroupType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemSimpleType;
import org.riverock.portlet.schema.portlet.news_block.NewsItemType;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

public class TestCaseNews extends TestCase implements TestCaseInterface
{
    private static Logger log = Logger.getLogger(TestCaseNews.class);

    private final static String NEWS_GROUP_TEXT = "���� �������� ";
    private final static String NEWS_ITEM_TEXT = "������� ";

    private final static String NEWS_ITEM_TEXT_ANONS = "������� �����";
    private final static String NEWS_ITEM_TEXT_HEADER = "������� ���������";
    private final static String NEWS_ITEM_TEXT_TEXT = "������� �����";

    private final static String NEWS_GROUP_CODE = "NEWS_GROUP_CODE";

    private static NewsBlockType newsBlock = null;

    private final static int COUNT_TEST_NEWS_GROUP = 2;
    private final static int COUNT_TEST_NEWS_ITEM = 3;

    private static final String NEWS_ITEM_URL = "url";
    private static final String NEWS_ITEM_TO_URL = "to news";

    private TestCaseSiteAbstract testAbstract = null;

    public TestCaseNews(String testName)
    {
        super(testName);
    }

    public void tearDown()
        throws Exception
    {
        System.out.println("start tearDown()");

        if (testAbstract!=null)
        {
            if ( testAbstract.db_!=null && testAbstract.db_.getConnection() != null)
            {
                testAbstract.db_.commit();
            }
            DatabaseAdapter.close( testAbstract.db_);
            testAbstract.db_ = null;
        }
    }

    public void doTest()
        throws Exception
    {
        doTestNewsItemPortletById();
        doTestNewsBlockPortlet();
        doTestNewsBlockPortletByCode();
    }

    public void testWithOracleConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithOracleConnection( this );
    }

    public void testWithHypersonicConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithHypersonicConnection( this );
    }

    public void testWithMySqlConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMySqlConnection( this );
    }

    public void testWithIbmDB2Connection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithIbmDB2Connection( this );
    }

    public void testWithMSSQLConnection()
        throws Exception
    {
        testAbstract = new TestCaseSiteAbstract();
        testAbstract.testWithMSSQLConnection( this );
    }

    public void insertTestData()
        throws Exception
    {
        newsBlock = null;
        newsBlock = new NewsBlockType();
        newsBlock.setIdSiteLanguage( TestCaseSiteAbstract.testSite.idRuSiteLanguage );

        for (int i=0; i<COUNT_TEST_NEWS_GROUP; i++)
        {
            NewsGroupType newsGroup = new NewsGroupType();
            newsBlock.addNewsGroup( newsGroup );

            PreparedStatement ps = null;
            int i1 = 0 ;
            try
            {
                CustomSequenceType seqSite = new CustomSequenceType();
                seqSite.setSequenceName("SEQ_WM_NEWS_LIST");
                seqSite.setTableName( "WM_NEWS_LIST");
                seqSite.setColumnName( "ID_NEWS" );

                Long seqValue = new Long(testAbstract.db_.getSequenceNextValue( seqSite ) );
                newsGroup.setNewsGroupId( seqValue );
                newsGroup.setNewsGroupName( NEWS_GROUP_TEXT+i );
                newsGroup.setMaxNews( new Long(4) );
                newsGroup.setOrderValue( new Integer(20-i) );
                newsGroup.setNewsGroupCode( i==0?NEWS_GROUP_CODE:"" );

                assertFalse("Error get new value of sequence for table "+seqSite.getTableName(), seqValue==null);

                ps = testAbstract.db_.prepareStatement(
                    "insert into WM_NEWS_LIST " +
                    "(ID_NEWS, NAME_NEWS, COUNT_NEWS, ORDER_FIELD, CODE_NEWS_GROUP, ID_SITE_SUPPORT_LANGUAGE) "+
                    "values"+
                    "(?, ?, ?, ?, ?, ?)"
                );

                RsetTools.setLong(ps, 1, newsGroup.getNewsGroupId() );
                ps.setString(2, newsGroup.getNewsGroupName() );
                // Todo check how setObject work with other, than oracle, db
                ps.setObject(3, newsGroup.getMaxNews() );
                ps.setObject(4, newsGroup.getOrderValue() );
                ps.setString(5, newsGroup.getNewsGroupCode() );
                RsetTools.setLong(ps, 6, TestCaseSiteAbstract.testSite.idRuSiteLanguage );

                i1 = ps.executeUpdate();
                assertFalse("Error insert news block", i1==0);

                testAbstract.db_.commit();
                DatabaseManager.close( ps );
                ps = null;

                for (int j=0;j<COUNT_TEST_NEWS_ITEM; j++)
                {
                    NewsItemType newsItem = new NewsItemType();
                    newsGroup.addNewsItem( newsItem );

                    CustomSequenceType seqNewsItem = new CustomSequenceType();
                    seqNewsItem.setSequenceName("SEQ_WM_NEWS_ITEM");
                    seqNewsItem.setTableName( "WM_NEWS_ITEM");
                    seqNewsItem.setColumnName( "ID" );

                    Long seqNewsItemValue = new Long(testAbstract.db_.getSequenceNextValue( seqNewsItem ));
                    newsItem.setNewsItemId( seqNewsItemValue );
                    Timestamp stamp = DateTools.getCurrentTime();
                    stamp.setNanos(0);
                    newsItem.setNewsDateTime( stamp );

                    newsItem.setNewsDate(
                        DateUtils.getStringDate(
                            newsItem.getNewsDateTime(), "dd.MMM.yyyy",
                            StringTools.getLocale( TestSite.TEST_LANGUAGE ) )
                    );
                    newsItem.setNewsTime(
                        DateUtils.getStringDate(
                            newsItem.getNewsDateTime(), "HH:mm",
                            StringTools.getLocale( TestSite.TEST_LANGUAGE )
                        )
                    );

                    newsItem.setNewsHeader( NEWS_ITEM_TEXT_HEADER+" "+j );
                    newsItem.setNewsAnons( NEWS_ITEM_TEXT_ANONS+" "+j );
                    newsItem.setNewsText( NEWS_ITEM_TEXT_TEXT+" "+j );

                    // temporary value for correct validate
                    newsItem.setUrlToFullNewsItem( NEWS_ITEM_URL );
                    newsItem.setToFullItem( NEWS_ITEM_TO_URL );

                    assertFalse("Error get new value of sequence for table "+seqNewsItem.getTableName(), seqNewsItemValue==null);

                    ps = testAbstract.db_.prepareStatement(
                        "insert into WM_NEWS_ITEM " +
                        "(ID, ID_NEWS, EDATE, HEADER, ANONS) "+
                        "values"+
                        "(?, ?, "+testAbstract.db_.getNameDateBind()+", ?, ?)"
                    );

                    RsetTools.setLong(ps, 1, newsItem.getNewsItemId() );
                    RsetTools.setLong(ps, 2, newsGroup.getNewsGroupId());
                    testAbstract.db_.bindDate(ps, 3, (Timestamp)newsItem.getNewsDateTime() );
                    ps.setString(4, newsItem.getNewsHeader() );
                    ps.setString(5, newsItem.getNewsAnons() );

                    i1 = ps.executeUpdate();
                    assertFalse("Error insert news item", i1==0);

                    // wait for 1 second. Need for different timestamp in news item
                    long mills = System.currentTimeMillis();
                    while (true)
                        if ( (System.currentTimeMillis()-mills)>1000 )
                            break;

                    testAbstract.db_.commit();
                    DatabaseManager.close( ps );
                    ps = null;

                    CustomSequenceType seqNewsItemText = new CustomSequenceType();
                    seqNewsItemText.setSequenceName( "SEQ_WM_NEWS_ITEM_TEXT" );
                    seqNewsItemText.setTableName( "WM_NEWS_ITEM_TEXT" );
                    seqNewsItemText.setColumnName( "ID_MAIN_NEWS_TEXT" );

                    Long idNewsItemText = new Long(testAbstract.db_.getSequenceNextValue( seqNewsItemText ));

                    assertFalse("Error get new value of sequence for table "+seqNewsItemText.getTableName(), idNewsItemText==null);

                    ps = testAbstract.db_.prepareStatement(
                        "insert into WM_NEWS_ITEM_TEXT " +
                        "(ID_MAIN_NEWS_TEXT, ID, TEXT) "+
                        "values"+
                        "(?, ?, ?)"
                    );

                    RsetTools.setLong(ps, 1, idNewsItemText);
                    RsetTools.setLong(ps, 2, newsItem.getNewsItemId() );
                    ps.setString(3, newsItem.getNewsText() );

                    i1 = ps.executeUpdate();
                    assertFalse("Error insert news item text", i1==0);

                    testAbstract.db_.commit();

                    DatabaseManager.close( ps );
                    ps = null;

                }
            }
            catch(Exception e)
            {
                testAbstract.db_.rollback();
                throw e;
            }
            finally
            {
                DatabaseManager.close( ps );
                ps = null;
            }
        }
        newsBlock.validate();
//        System.exit(0);
    }

    private void doTestNewsBlockPortletByCode()
        throws Exception
    {
        NewsBlockType resultBlock = new NewsBlockType();
        resultBlock.setIdSiteLanguage( newsBlock.getIdSiteLanguage() );

        for (int i=0;i< newsBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType group = newsBlock.getNewsGroup(i);
            if (NEWS_GROUP_CODE.equals(group.getNewsGroupCode()) )
            {
                NewsGroupType targetGroup = new NewsGroupType();
                targetGroup.setNewsGroupName( group.getNewsGroupName() );
                targetGroup.setOrderValue( group.getOrderValue() );
                targetGroup.setNewsGroupId( group.getNewsGroupId() );
                targetGroup.setMaxNews( group.getMaxNews() );
                targetGroup.setNewsGroupCode( group.getNewsGroupCode() );

                long startDate = Long.MAX_VALUE;
                for (int j=0; j<group.getNewsItemCount() && targetGroup.getNewsItemCount()<group.getMaxNews().intValue(); j++)
                {
                    NewsItemType item = getLatestNewsItem( group, startDate );
                    startDate = item.getNewsDateTime().getTime();
                    targetGroup.addNewsItem( item );
                }
                resultBlock.addNewsGroup( targetGroup );
                break;
            }
        }

        XmlTools.writeToFile(
            resultBlock,
            SiteUtils.getTempDir() + "news-block-result.xml",
            "utf-8",
            null
        );

        for (int i=0;i< resultBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType groupResult = resultBlock.getNewsGroup(i);
            for (int j=0; j<newsBlock.getNewsGroupCount();j++)
            {
                NewsGroupType groupOrigin = newsBlock.getNewsGroup(j);
                if (groupOrigin.getNewsGroupId()==groupResult.getNewsGroupId())
                    assertFalse("Wrong number of item in group",
                        groupOrigin.getMaxNews().intValue()<groupResult.getNewsItemCount());
            }
        }

        testAbstract.initRequestSession();
        String str = null;
        try
        {
/*
            if (testAbstract.jspPage.sMain.checkKey("main.next-news"))
                str = testAbstract.jspPage.sMain.getStr("main.next-news");
            else
                str = testAbstract.jspPage.sMain.getStr("main.next");
*/
        }
        catch (Exception e)
        {
            log.error("Error get localized string", e);
            str = "error";
        }

        for (int i=0;i< resultBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType groupResult = resultBlock.getNewsGroup(i);
            groupResult.setOrderValue( null );
            for (int j=0; j<groupResult.getNewsItemCount();j++)
            {
                NewsItemType newsItem = groupResult.getNewsItem(j);

                newsItem.setToFullItem( str );
                newsItem.setUrlToFullNewsItem(
//                    PortletService.url( Constants .CTX_TYPE_NEWS ) + '&' +
                    NewsSite.NAME_ID_NEWS_PARAM + '=' + newsItem.getNewsItemId() + '&'
                );
            }
        }


        String codePortlet = "mill.news_block";
//        PortletDefinition desc = PortletManager.getPortletDescription( codePortlet );
//        assertFalse("Description for portlet '"+codePortlet+"' not found", desc==null);


//        ctxInstance.setType( "mill.news_block" );

//        TemplateItemType templateItem = new TemplateItemType();
//        templateItem.setCode( NEWS_GROUP_CODE );

        // localePackage ���� ����� �� ����� ��������� ���������

//        testAbstract.session.setAttribute(Constants.PORTLET_CODE_SESSION, NEWS_GROUP_CODE);

        PortletResultObject obj;
//        obj = PortletTools.getPortletObject(desc, portletParameter, testAbstract.db_, renderRequest);

//        assertFalse("Portlet item '"+codePortlet+"' not found", obj==null);

//        assertFalse("Type of return object id wrong. Expected '"+
//            NewsBlock.class.getName()+"', returned '"+obj.getClass().getName()+"'",
//            !NewsBlock.class.getName().equals(obj.getClass().getName()));

        byte[] resultByte;
//        resultByte = obj.getXml();
        resultBlock.setIdSiteLanguage( null );
        byte[] originByte = XmlTools.getXml( resultBlock, "NewsBlock" );

//        MainTools.writeToFile(SiteUtils.getTempDir()+"news-by-code-bytes-from-portlet.xml", resultByte);
        MainTools.writeToFile(SiteUtils.getTempDir()+"news-by-code-bytes-from-object.xml", originByte);

//        assertFalse("Transforming failed. Origin and Result NewsBlock not equals",
//            !new String(originByte).equals(new String( resultByte))
//            );
    }

    private void initNewsItemRequestByCode()
        throws Exception
    {
        testAbstract.initRequestSession();

//        TemplateItemType templateItem = new TemplateItemType();
//        templateItem.setCode( "RELEASE_NEWS_ITEM" );

//        testAbstract.session.setAttribute(Constants.PORTLET_CODE_SESSION, "RELEASE_NEWS_ITEM");
//        portletParameter.portletCode = NEWS_GROUP_CODE;

        testAbstract.request.setSession( testAbstract.session );
    }


    private void doTestNewsItemPortletById()
        throws Exception
    {
        for (int i=0;i< newsBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType group = newsBlock.getNewsGroup(i);
            for (int j=0; j<group.getNewsItemCount(); j++)
            {
                NewsItemType item = group.getNewsItem(j);

                testAbstract.initRequestSession();

                PortletDefinition desc;
//                desc = PortletManager.getPortletDescription( "mill.news" );
//                assertFalse("Description for portlet 'mill.news' not found", desc==null);

//                testAbstract.request.setParameter(
//                    PortletService.getStringParam(
//                        desc, PortletTools.name_portlet_id
//                    ),
//                    ""+item.getNewsItemId()
//                );


//                TemplateItemType templateItem = new TemplateItemType();

                // localePackage ���� ����� �� ����� ��������� ���������
//                PortletParameter portletParameter =
//                    new PortletParameter(ctxInstance,
//                        PortletService.getStringParam(
//                            desc, ContainerConstants.locale_name_package
//                        ),
//                        templateItem
//                    );

//        PortletFile[] portlet = PortletManager.getPortletFileArray();
//        PortletClassGetListType portletClassList = new PortletClassGetListType();

                PortletResultObject obj
                    ;
//                obj = PortletTools.getPortletObject(desc, portletParameter, testAbstract.db_, renderRequest);

//                assertFalse("Portlet item mill.news with id - "+item.getNewsItemId()+" not found",
//                    obj==null);

//                assertFalse("Type of return object id wrong. Expected '"+
//                    desc.getPortletClass()+"', returned '"+obj.getClass().getName()+"'",
//                    !desc.getPortletClass().equals(obj.getClass().getName()));
//
//                byte[] resultByte = obj.getXml();
                NewsItemSimpleType originItem = new NewsItemSimpleType();
                originItem.setNewsAnons( item.getNewsAnons() );
                originItem.setNewsDate(
                    DateUtils.getStringDate(
                        item.getNewsDateTime(), "dd.MMM.yyyy",
                        StringTools.getLocale( TestSite.TEST_LANGUAGE ) )
                );
                originItem.setNewsHeader( item.getNewsHeader() );
                originItem.setNewsText( item.getNewsText() );
                originItem.setNewsTime(
                    DateUtils.getStringDate(
                        item.getNewsDateTime(), "HH:mm",
                        StringTools.getLocale( TestSite.TEST_LANGUAGE )
                    )
                );
                byte[] originByte = XmlTools.getXml( originItem, "NewsItemSimple" );

//                MainTools.writeToFile(SiteUtils.getTempDir()+"news-by-id-bytes-from-portlet.xml", resultByte);
                MainTools.writeToFile(SiteUtils.getTempDir()+"news-by-id-bytes-from-test-object.xml", originByte);
                System.out.println("news id - "+item.getNewsItemId());

//                System.out.println("result of compare "+(new String(originByte).equals(new String( resultByte))) );
                System.out.println("result #1 "+new String( originByte, "utf-8") );
//                System.out.println("result #2 "+new String( resultByte, "utf-8") );

//                assertFalse("Transforming failed. Origin and Result not equals",
//                    !new String(originByte).equals(new String( resultByte))
//                    );
            }
        }
    }

    private NewsGroupType getMinNewsBlock( int startOrder )
    {
        int returnValue = Integer.MAX_VALUE;

        NewsGroupType returnGroup = null;
        for (int i=0;i< newsBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType group = newsBlock.getNewsGroup(i);
            if ( startOrder<group.getOrderValue().intValue() && group.getOrderValue().intValue()< returnValue )
            {
                returnValue = group.getOrderValue().intValue();
                returnGroup = group;
            }
        }

        return returnGroup;
    }

    private NewsItemType getLatestNewsItem( NewsGroupType group, long mills)
    {
        long returnValue = Long.MIN_VALUE;

        NewsItemType returnItem = null;
        for (int i=0;i< group.getNewsItemCount(); i++)
        {
            NewsItemType item = group.getNewsItem(i);
            long currMills = item.getNewsDateTime().getTime();
            if ( mills>currMills && currMills>returnValue)
            {
                returnValue = currMills;
                returnItem = item;
            }
        }

        return returnItem;
    }

    private void doTestNewsBlockPortlet()
        throws Exception
    {
        NewsBlockType resultBlock = new NewsBlockType();
        resultBlock.setIdSiteLanguage( newsBlock.getIdSiteLanguage() );

        int startOrder = Integer.MIN_VALUE;
        for (int i=0;i< newsBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType group = getMinNewsBlock(startOrder);
            startOrder = group.getOrderValue().intValue();
            NewsGroupType targetGroup = new NewsGroupType();
            targetGroup.setNewsGroupName( group.getNewsGroupName() );
            targetGroup.setOrderValue( group.getOrderValue() );
            targetGroup.setNewsGroupId( group.getNewsGroupId() );
            targetGroup.setMaxNews( group.getMaxNews() );
            targetGroup.setNewsGroupCode( group.getNewsGroupCode() );

            long startDate = Long.MAX_VALUE;
            for (int j=0; j<group.getNewsItemCount() && targetGroup.getNewsItemCount()<group.getMaxNews().intValue(); j++)
            {
                NewsItemType item = getLatestNewsItem( group, startDate );
                startDate = item.getNewsDateTime().getTime();
                targetGroup.addNewsItem( item );
            }
            resultBlock.addNewsGroup( targetGroup );
        }

        XmlTools.writeToFile(
            newsBlock,
            SiteUtils.getTempDir() + "news-block-origin.xml",
            "utf-8",
            null
        );
        XmlTools.writeToFile(
            resultBlock,
            SiteUtils.getTempDir() + "news-block-result.xml",
            "utf-8",
            null
        );

        for (int i=0;i< resultBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType groupResult = resultBlock.getNewsGroup(i);
            for (int j=0; j<newsBlock.getNewsGroupCount();j++)
            {
                NewsGroupType groupOrigin = newsBlock.getNewsGroup(j);
                if (groupOrigin.getNewsGroupId()==groupResult.getNewsGroupId())
                    assertFalse("Wrong number of item in group",
                        groupOrigin.getMaxNews().intValue()<groupResult.getNewsItemCount());
            }
        }

        testAbstract.initRequestSession();
        String str = null;
        try
        {
//            if (testAbstract.jspPage.sMain.checkKey("main.next-news"))
//                str = testAbstract.jspPage.sMain.getStr("main.next-news");
//            else
//                str = testAbstract.jspPage.sMain.getStr("main.next");

        }
        catch (Exception e)
        {
            log.error("Error get localized string", e);
            str = "error";
        }

        for (int i=0;i< resultBlock.getNewsGroupCount(); i++)
        {
            NewsGroupType groupResult = resultBlock.getNewsGroup(i);
            groupResult.setOrderValue( null );
            for (int j=0; j<groupResult.getNewsItemCount();j++)
            {
                NewsItemType newsItem = groupResult.getNewsItem(j);

                newsItem.setToFullItem( str );
/*
                newsItem.setUrlToFullNewsItem(
                    testAbstract.response.encodeURL( CtxURL.ctx()) + '?' +
                    testAbstract.jspPage.getAsURL() + Constants.NAME_ID_NEWS_PARAM + '=' +
                    newsItem.getNewsItemId() + '&' +
                    Constants.NAME_TYPE_CONTEXT_PARAM  + '='+
                    Constants.CTX_TYPE_NEWS
                );
*/
            }
        }


        String codePortlet = "mill.news_block";
//        PortletDefinition desc = PortletManager.getPortletDescription( codePortlet );
//        assertFalse("Description for portlet '"+codePortlet+"' not found", desc==null);

//        TemplateItemType templateItem = new TemplateItemType();
//
        // localePackage ���� ����� �� ����� ��������� ���������
//        PortletParameter portletParameter =
//            new PortletParameter(ctxInstance,
//                PortletService.getStringParam(
//                    desc, ContainerConstants.locale_name_package
//                ),
//                templateItem
//            );
//
//        PortletResultObject obj =
//            PortletTools.getPortletObject(desc, portletParameter, testAbstract.db_, renderRequest);

//        assertFalse("Portlet item '"+codePortlet+"' not found", obj==null);

//        assertFalse("Type of return object id wrong. Expected '"+
//            NewsBlock.class.getName()+"', returned '"+obj.getClass().getName()+"'",
//            !NewsBlock.class.getName().equals(obj.getClass().getName()));
//
//        byte[] resultByte = obj.getXml();
        resultBlock.setIdSiteLanguage( null );
        byte[] originByte = XmlTools.getXml( resultBlock, "NewsBlock" );

//        MainTools.writeToFile(SiteUtils.getTempDir()+"bytes-from-portlet.xml", resultByte);
        MainTools.writeToFile(SiteUtils.getTempDir()+"bytes-from-test-object.xml", originByte);

//        assertFalse("Transforming failed. Origin and Result NewsBlock not equals",
//            !new String(originByte).equals(new String( resultByte))
//            );
    }

}