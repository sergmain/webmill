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
package org.riverock.portlet.menu;

import junit.framework.TestCase;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.collections.TreeUtils;
import org.riverock.common.collections.TreeItemInterface;

import java.util.LinkedList;
import java.util.List;

/**
 * User: smaslyukov
 * Date: 16.08.2004
 * Time: 17:50:04
 * $Id$
 */
public class TestMenuTreeRebuild extends TestCase {

    public void testRebuildTree()
        throws Exception
    {
        LinkedList list = new LinkedList();
        list.add( new MenuItem(new Long(1), new Long(0), null, null, null, null, null) );
        list.add( new MenuItem(new Long(2), new Long(0), null, null, null, null, null) );
        list.add( new MenuItem(new Long(3), new Long(0), null, null, null, null, null) );
        list.add( new MenuItem(new Long(4), new Long(0), null, null, null, null, null) );

        list.add( new MenuItem(new Long(10), new Long(1), null, null, null, null, null) );
        list.add( new MenuItem(new Long(11), new Long(1), null, null, null, null, null) );
        list.add( new MenuItem(new Long(12), new Long(1), null, null, null, null, null) );

        list.add( new MenuItem(new Long(100), new Long(10), null, null, null, null, null) );
        list.add( new MenuItem(new Long(101), new Long(10), null, null, null, null, null) );
        list.add( new MenuItem(new Long(102), new Long(10), null, null, null, null, null) );
        list.add( new MenuItem(new Long(103), new Long(10), null, null, null, null, null) );
        list.add( new MenuItem(new Long(104), new Long(10), null, null, null, null, null) );

        list.add( new MenuItem(new Long(21), new Long(2), null, null, null, null, null) );
        list.add( new MenuItem(new Long(21), new Long(2), null, null, null, null, null) );

        list.add( new MenuItem(new Long(30), new Long(3), null, null, null, null, null) );

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

