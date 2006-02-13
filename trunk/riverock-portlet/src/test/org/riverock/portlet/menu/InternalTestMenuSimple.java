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
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.webmill.portal.menu;

import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.schema.types.MenuModuleType;
import org.riverock.interfaces.portlet.menu.Menu;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.generic.tools.XmlTools;

import org.apache.log4j.Logger;
import junit.framework.Assert;

import java.util.ListIterator;

public class  InternalTestMenuSimple

{
    private static Logger log = Logger.getLogger(InternalTestMenuSimple.class);

    private final static int COUNT_TOP_LEVEL_MENU = 4;
    private final static int COUNT_SUB_MENU = 3;
    private final static String menuItem = "Menu item ";
/*
    public static void testProcessInstance(Menu defMenu, String PortletDefinition, PortletParameter param)
            throws Exception
    {
        System.out.println("PortletDefinition "+PortletDefinition);
//        PortletDefinition descFromManager = PortletManager.getPortletDescription( PortletDefinition );
//        Assert.assertEquals(true, descFromManager!=null);

        MenuSimple menu = new MenuSimple();
        menu.setParameter( param );

        PortletDefinition desc = menu.initPortletId(PortletDefinition);

//        Assert.assertEquals(true, desc!=null);
//        Assert.assertEquals(PortletDefinition, desc.getPortletName().getContent());
//        Assert.assertEquals(true, menu.getId()!=-1);

        Assert.assertEquals(true, menu.param!=null);
        Assert.assertEquals(true, menu.param.getResponse()!=null);

        ListIterator it = defMenu.getMenuItem().listIterator();
        while (it.hasNext()) {
            MenuItem ci = (MenuItem)it.next();

            MenuModuleType tempMenu = menu.getMenuModule(
                    ci, menu.param.getResponse(), 1, menu.getId(), desc, PortletDefinition
            );
            Assert.assertEquals(true, tempMenu!=null);
        }


        menu.initMenuSimple(defMenu, desc, PortletDefinition);
        Assert.assertEquals(true, menu.menuSimple.getMenuModuleCount()>0);

        XmlTools.writeToFile(menu.menuSimple, WebmillConfig.getWebmillDebugDir()+"test-menu-#1.xml");

        MenuModuleType[] result =
            menu.getMenuModuleWithLevel(menu.menuSimple.getMenuModule(), 0, 0);

        Assert.assertEquals(true, result!=null);
        Assert.assertEquals(TestSite.COUNT_TOP_LEVEL_MENU, result.length);

        menu.processMenuLevel(0, MenuSimple.GREAT_OR_EQUAL_LEVEL);

        XmlTools.writeToFile(result, WebmillConfig.getWebmillDebugDir()+"test-menu-#2.xml");

    }
*/
    public static void testDecodeLevel()
    {
        Assert.assertEquals(MenuSimple.UNKNOWN_LEVEL, MenuSimple.decodeLevel("hkfjsdhfksdj"));
        Assert.assertEquals(MenuSimple.UNKNOWN_LEVEL, MenuSimple.decodeLevel(null));
        Assert.assertEquals(MenuSimple.EQUAL_LEVEL, MenuSimple.decodeLevel("equal"));
        Assert.assertEquals(MenuSimple.GREAT_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("great_or_equal"));
        Assert.assertEquals(MenuSimple.GREAT_THAN_LEVEL, MenuSimple.decodeLevel("great"));
        Assert.assertEquals(MenuSimple.LESS_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("less_or_equal"));
        Assert.assertEquals(MenuSimple.LESS_THAN_LEVEL, MenuSimple.decodeLevel("less"));
    }
}