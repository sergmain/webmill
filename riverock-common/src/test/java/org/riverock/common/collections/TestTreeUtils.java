/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.common.collections;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.riverock.interfaces.common.TreeItem;

/**
 * User: smaslyukov
 * Date: 16.08.2004
 * Time: 16:08:38
 * $Id$
 */
public class TestTreeUtils extends TestCase {

    class SimpleTreeItem implements TreeItem {
        Long id;
        Long topId;
        List<TreeItem> subTree;

        public SimpleTreeItem(Long id, Long topId, List<TreeItem> tree) {
            this.id=id;
            this.topId=topId;
            this.subTree=tree;
        }

        public Long getTopId() {
            return topId;
        }

        public Long getId() {
            return id;
        }

        public List<TreeItem> getSubTree() {
            return subTree;
        }

        public void setSubTree(List<TreeItem> list) {
            this.subTree = list;
        }
    }

    public void testRebuildTree()
        throws Exception
    {
        LinkedList<TreeItem> list = new LinkedList<TreeItem>();
        list.add( new SimpleTreeItem(1L, 0L, null) );
        list.add( new SimpleTreeItem(2L, 0L, null) );
        list.add( new SimpleTreeItem(3L, 0L, null) );
        list.add( new SimpleTreeItem(4L, 0L, null) );

        list.add( new SimpleTreeItem(10L, 1L, null) );
        list.add( new SimpleTreeItem(11L, 1L, null) );
        list.add( new SimpleTreeItem(12L, 1L, null) );

        list.add( new SimpleTreeItem(100L, 10L, null) );
        list.add( new SimpleTreeItem(101L, 10L, null) );
        list.add( new SimpleTreeItem(102L, 10L, null) );
        list.add( new SimpleTreeItem(103L, 10L, null) );
        list.add( new SimpleTreeItem(104L, 10L, null) );

        list.add( new SimpleTreeItem(21L, 2L, null) );
        list.add( new SimpleTreeItem(21L, 2L, null) );

        list.add( new SimpleTreeItem(30L, 3L, null) );

        List result = TreeUtils.rebuildTree(list);

        System.out.println("result.size() = " + result.size());
        assertFalse("Count items in top level wrong", result.size()!=4 );
//        assertFalse(((SimpleTreeItem)result.get(0)).getSubTree().size()!=3 );
//        assertFalse(((SimpleTreeItem)result.get(1)).getSubTree().size()!=2 );
//        assertFalse(((SimpleTreeItem)result.get(2)).getSubTree().size()!=1 );
//
//        SimpleTreeItem tempItem = (SimpleTreeItem)((SimpleTreeItem)result.get(0)).getSubTree().get(0);
//
//        assertFalse( tempItem.getSubTree().size()!=5 );
    }

    public void testRebuildBrokenTree() throws Exception {
        List<TreeItem> list = new ArrayList<TreeItem>();
        list.add( new SimpleTreeItem(146L, 0L, null) );
        list.add( new SimpleTreeItem(170L, 164L, null) );
        list.add( new SimpleTreeItem(171L, 170L, null) );
        list.add( new SimpleTreeItem(172L, 170L, null) );
        list.add( new SimpleTreeItem(166L, 164L, null) );
        list.add( new SimpleTreeItem(164L, 146L, null) );
        list.add( new SimpleTreeItem(165L, 164L, null) );

        List result = TreeUtils.rebuildTree(list);

        System.out.println("result.size() = " + result.size());
        assertTrue(result.size()==1 );


        List subTree;
        subTree = ((SimpleTreeItem) result.get(0)).getSubTree();
        assertNotNull(subTree);
        assertTrue(subTree.size()==1 );

        subTree = ((SimpleTreeItem) subTree.get(0)).getSubTree();
        assertNotNull(subTree);
        assertTrue(subTree.size()==3 );
    }
}
