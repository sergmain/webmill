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
package org.riverock.webmill.portlet.menu;

import junit.framework.TestCase;
import org.riverock.common.collections.TreeUtils;
import org.riverock.webmill.portal.menu.MenuItem;
import org.riverock.webmill.schema.core.WmPortalCatalogItemType;
import org.riverock.interfaces.common.TreeItemInterface;
import org.riverock.generic.db.DatabaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * User: smaslyukov
 * Date: 16.08.2004
 * Time: 17:50:04
 * $Id$
 */
public class TestMenuTreeRebuild extends TestCase {

    private static class MenuItemTest {
        static WmPortalCatalogItemType get( Long idSiteCtxCatalog, Long idTopCtxCatalog ){
            WmPortalCatalogItemType item = new WmPortalCatalogItemType();
            item.setIdSiteCtxCatalog( idSiteCtxCatalog );
            item.setIdTopCtxCatalog( idTopCtxCatalog );
            item.setKeyMessage( "message_"+idSiteCtxCatalog+"-"+idTopCtxCatalog );
            return item;
        }
    }

    public void testRebuildTree()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
//        DatabaseAdapter db = DatabaseAdapter.getInstance(true);
        DatabaseAdapter db = null;

        LinkedList list = new LinkedList();

        list.add( new MenuItem( db, MenuItemTest.get(new Long(1), new Long(0)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(2), new Long(0)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(3), new Long(0)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(4), new Long(0)) ) );

        list.add( new MenuItem( db, MenuItemTest.get(new Long(10), new Long(1)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(11), new Long(1)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(12), new Long(1)) ) );

        list.add( new MenuItem( db, MenuItemTest.get(new Long(100), new Long(10)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(101), new Long(10)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(102), new Long(10)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(103), new Long(10)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(104), new Long(10)) ) );

        list.add( new MenuItem( db, MenuItemTest.get(new Long(21), new Long(2)) ) );
        list.add( new MenuItem( db, MenuItemTest.get(new Long(21), new Long(2)) ) );

        list.add( new MenuItem( db, MenuItemTest.get(new Long(30), new Long(3)) ) );

        List result = TreeUtils.rebuildTree(list);

        System.out.println("result.size() = " + result.size());
        assertFalse("Count items in top level wrong", result.size()!=4 );
        assertFalse(((TreeItemInterface)result.get(0)).getSubTree().size()!=3 );
        assertFalse(((TreeItemInterface)result.get(1)).getSubTree().size()!=2 );
        assertFalse(((TreeItemInterface)result.get(2)).getSubTree().size()!=1 );

        MenuItem tempItem = (MenuItem)((TreeItemInterface)result.get(0)).getSubTree().get(0);

        assertFalse( ((TreeItemInterface)tempItem).getSubTree().size()!=5 );
    }

}

