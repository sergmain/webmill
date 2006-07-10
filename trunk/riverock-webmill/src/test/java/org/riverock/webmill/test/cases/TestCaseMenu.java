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
 * User: Admin
 * Date: May 9, 2003
 * Time: 10:49:57 AM
 *
 * $Id$
 */
package org.riverock.webmill.test.cases;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.core.GetWmPortalVirtualHostItem;
import org.riverock.webmill.schema.core.WmPortalVirtualHostItemType;

public class TestCaseMenu // extends TestCase implements TestCaseInterface
{
/*
    private TestCaseSiteAbstract testAbstract = null;

    public TestCaseMenu(String testName)
    {
        super(testName);
    }

    public void insertTestData() throws Exception
    {
    }

    public void doTest() throws Exception
    {
        testAbstract.initRequestSession();

        InternalTestMenuSimple.testDecodeLevel();

//        Menu defMenu = testAbstract.jspPage.menuLanguage.getDefault();
//
//        Assert.assertEquals(true, defMenu!=null);
//        Assert.assertEquals(TestSite.NAME_DEFAULT_MENU, defMenu.getCatalogCode());
//        Assert.assertEquals(TestSite.COUNT_TOP_LEVEL_MENU, defMenu.getMenuItem().size());
//        Assert.assertEquals(true, defMenu.getIndexTemplate()!=null);

//        assertFalse("Menu list is null",testAbstract.jspPage.menuLanguage==null);
//        assertFalse("Menu is empty",testAbstract.jspPage.menuLanguage.getMenuCount()==0);


        {
            TemplateItemType templateItem = new TemplateItemType();
            MenuSimple menu = new MenuSimple();

            templateItem.setType( TemplateItemTypeTypeType.PORTLET );
            templateItem.setValue( TestSite.indexPortletDefinition );

            PortletParameter param = new PortletParameter(ctxInstance, null, templateItem) ;

            menu.setParameter( param );

            menu.getInstance( testAbstract.db_ );


            XmlTools.writeToFile(menu.menuSimple, WebmillConfig.getWebmillDebugDir()+"test-menu-full.xml");

            assertFalse("Wrong count of top level element, count = "+menu.menuSimple.getMenuModuleCount(),
                    menu.menuSimple.getMenuModuleCount()!=TestSite.COUNT_TOP_LEVEL_MENU
            );

            for (int i=0; i<menu.menuSimple.getMenuModuleCount(); i++)
            {
                MenuModuleType mod = menu.menuSimple.getMenuModule(i);
                assertFalse("Wrong count of sub menu, count = "+mod.getMenuModule().length,
                        mod.getMenuModule().length!=TestSite.COUNT_SUB_MENU
                );
            }
        }

        // Test get top level of menu
        {
            TemplateItemType templateItem = new TemplateItemType();
            templateItem.setType( TemplateItemTypeTypeType.PORTLET );
            templateItem.setValue( TestSite.indexPortletDefinition );

            MenuSimple menu = new MenuSimple();
            SiteTemplateParameterType templateParam1 = new SiteTemplateParameterType();
            templateParam1.setName("level");
            templateParam1.setValue("0");

            SiteTemplateParameterType templateParam2 = new SiteTemplateParameterType();
            templateParam2.setName( "type_level" );
            templateParam2.setValue( "great_or_equal" );

            templateItem.addParameter( templateParam1 );
            templateItem.addParameter( templateParam2 );

            PortletParameter param = new PortletParameter(ctxInstance, null, templateItem) ;

//            InternalTestMenuSimple.testProcessInstance(defMenu, TestSite.indexPortletDefinition, param);

            menu.setParameter( param );

            menu.getInstance( testAbstract.db_ );

            XmlTools.writeToFile(menu.menuSimple, WebmillConfig.getWebmillDebugDir()+"test-menu-great_or_equal.xml");


            assertFalse("Wrong count of top level element, count = "+menu.menuSimple.getMenuModuleCount(),
                    menu.menuSimple.getMenuModuleCount()!=TestSite.COUNT_TOP_LEVEL_MENU
            );
            for (int i=0; i<menu.menuSimple.getMenuModuleCount(); i++)
            {
                MenuModuleType mod = menu.menuSimple.getMenuModule(i);
                assertFalse("Wrong count of sub menu, count = "+mod.getMenuModule().length,
                        mod.getMenuModule().length!=TestSite.COUNT_SUB_MENU
                );
            }
        }
        // test 'equal' paramter
        {
            TemplateItemType templateItem = new TemplateItemType();
            templateItem.setType( TemplateItemTypeTypeType.PORTLET );
            templateItem.setValue( TestSite.indexPortletDefinition );

            MenuSimple menu = new MenuSimple();
            SiteTemplateParameterType templateParam11 = new SiteTemplateParameterType();
            templateParam11.setName("level");
            templateParam11.setValue("0");

            SiteTemplateParameterType templateParam21 = new SiteTemplateParameterType();
            templateParam21.setName( "type_level" );
            templateParam21.setValue( "equal" );

            templateItem.addParameter( templateParam11 );
            templateItem.addParameter( templateParam21 );

            PortletParameter param = new PortletParameter(ctxInstance, null, templateItem) ;

//            InternalTestMenuSimple.testProcessInstance(defMenu, TestSite.indexPortletDefinition, param);

            menu.setParameter( param );

            menu.getInstance( testAbstract.db_ );

            XmlTools.writeToFile(menu.menuSimple, WebmillConfig.getWebmillDebugDir()+"test-menu-equal.xml");

            assertEquals(true, menu.menuSimple.getMenuModuleCount()==TestSite.COUNT_TOP_LEVEL_MENU);

            for (int i=0; i<menu.menuSimple.getMenuModuleCount(); i++)
            {
                MenuModuleType mod = menu.menuSimple.getMenuModule(i);
                assertEquals(true, mod.getMenuModuleCount()==0);
            }
        }
    }

    public void tearDown()
        throws Exception
    {
        System.out.println("start tearDown()");

        if (testAbstract!=null)
        {
            if (testAbstract.db_!=null && testAbstract.db_.conn != null)
            {
                testAbstract.db_.commit();
            }
            DatabaseAdapter.close( testAbstract.db_);
            testAbstract.db_ = null;
        }
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
*/
    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        long id = 1;
        WmPortalVirtualHostItemType resultItem = GetWmPortalVirtualHostItem.getInstance( DatabaseAdapter.getInstance(), id ).item;

        String[][] ns = new String[][]
        {
            { "mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd" }
        };

        XmlTools.writeToFile(
            resultItem,
            WebmillConfig.getWebmillDebugDir()+"test-WM_PORTAL_VIRTUAL_HOST-item.xml",
            "utf-8",
            null,
            ns
        );
    }
}
