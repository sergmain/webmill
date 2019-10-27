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
package org.riverock.webmill.portal.menu;

import junit.framework.TestCase;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 10:49:57 AM
 *
 * $Id: TestCaseMenu.java 1246 2007-07-12 17:22:46Z serg_main $
 */
public class TestCaseMenu /*extends TestCase*/ {

    public void doTest() throws Exception {

/*
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


            XmlTools.writeToFile(menu.menuSimple, GenericConfig.getGenericDebugDir()+"test-menu-full.xml");

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

            XmlTools.writeToFile(menu.menuSimple, GenericConfig.getGenericDebugDir()+"test-menu-great_or_equal.xml");


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

            XmlTools.writeToFile(menu.menuSimple, GenericConfig.getGenericDebugDir()+"test-menu-equal.xml");

            assertEquals(true, menu.menuSimple.getMenuModuleCount()==TestSite.COUNT_TOP_LEVEL_MENU);

            for (int i=0; i<menu.menuSimple.getMenuModuleCount(); i++)
            {
                MenuModuleType mod = menu.menuSimple.getMenuModule(i);
                assertEquals(true, mod.getMenuModuleCount()==0);
            }
        }
*/
    }
}
