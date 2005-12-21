package org.riverock.common.collections;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.riverock.interfaces.common.TreeItemInterface;

/**
 * User: smaslyukov
 * Date: 16.08.2004
 * Time: 16:08:38
 * $Id$
 */
public class TestTreeUtils extends TestCase {

    class SimpleTreeItem implements TreeItemInterface {
        Long id;
        Long topId;
        List subTree;

        public SimpleTreeItem(Long id, Long topId, List tree) {
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

        public List getSubTree() {
            return subTree;
        }

        public void setSubTree(List list) {
            this.subTree = list;
        }
    }

    public void testRebuildTree()
        throws Exception
    {
        ArrayList<TreeItemInterface> list = new ArrayList<TreeItemInterface>();
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
        assertFalse(((SimpleTreeItem)result.get(0)).getSubTree().size()!=3 );
        assertFalse(((SimpleTreeItem)result.get(1)).getSubTree().size()!=2 );
        assertFalse(((SimpleTreeItem)result.get(2)).getSubTree().size()!=1 );

        SimpleTreeItem tempItem = (SimpleTreeItem)((SimpleTreeItem)result.get(0)).getSubTree().get(0);

        assertFalse( tempItem.getSubTree().size()!=5 );
    }

}
