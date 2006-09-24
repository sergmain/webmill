/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.menu;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.riverock.common.collections.TreeUtils;
import org.riverock.interfaces.common.TreeItem;

/**
 * User: smaslyukov
 * Date: 16.08.2004
 * Time: 17:50:04
 * $Id$
 */
public class TestMenuTreeRebuild extends TestCase {

    private static interface MenuItemTest extends TreeItem {

    }

    private static MenuItemTest get( final Long idSiteCtxCatalog, final Long idTopCtxCatalog ){
            return new MenuItemTest(){

                private List<TreeItem> items = null;

                public Long getTopId() {
                    return idTopCtxCatalog;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public Long getId() {
                    return idSiteCtxCatalog;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public List<TreeItem> getSubTree() {
                    return items;
                }

                public void setSubTree(List<TreeItem> list) {
                    this.items = list;
                }
            };
    }

    public void testRebuildTree()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        List<TreeItem> list = new ArrayList<TreeItem>();

        list.add( get((long) 1, (long) 0) );
        list.add( get((long) 2, (long) 0) );
        list.add( get((long) 3, (long) 0) );
        list.add( get((long) 4, (long) 0) );

        list.add( get((long) 10, (long) 1) );
        list.add( get((long) 11, (long) 1) );
        list.add( get((long) 12, (long) 1) );

        list.add( get((long) 100, (long) 10) );
        list.add( get((long) 101, (long) 10) );
        list.add( get((long) 102, (long) 10) );
        list.add( get((long) 103, (long) 10) );
        list.add( get((long) 104, (long) 10) );

        list.add( get((long) 21, (long) 2) );
        list.add( get((long) 21, (long) 2) );

        list.add( get((long) 30, (long) 3) );

        List result = TreeUtils.rebuildTree(list);

        System.out.println("result.size() = " + result.size());
        assertFalse("Count items in top level wrong", result.size()!=4 );
        assertFalse(((TreeItem)result.get(0)).getSubTree().size()!=3 );
        assertFalse(((TreeItem)result.get(1)).getSubTree().size()!=2 );
        assertFalse(((TreeItem)result.get(2)).getSubTree().size()!=1 );

        MenuItemTest tempItem = (MenuItemTest)((TreeItem)result.get(0)).getSubTree().get(0);

        assertFalse( tempItem.getSubTree().size()!=5 );
    }

}

