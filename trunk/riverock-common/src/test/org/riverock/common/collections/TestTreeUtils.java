package org.riverock.common.collections;

import junit.framework.TestCase;

import java.util.List;
import java.util.LinkedList;

import org.riverock.common.tools.TestCaseStringTools;

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
        LinkedList list = new LinkedList();
        list.add( new SimpleTreeItem(new Long(1), new Long(0), null) );
        list.add( new SimpleTreeItem(new Long(2), new Long(0), null) );
        list.add( new SimpleTreeItem(new Long(3), new Long(0), null) );
        list.add( new SimpleTreeItem(new Long(4), new Long(0), null) );

        list.add( new SimpleTreeItem(new Long(10), new Long(1), null) );
        list.add( new SimpleTreeItem(new Long(11), new Long(1), null) );
        list.add( new SimpleTreeItem(new Long(12), new Long(1), null) );

        list.add( new SimpleTreeItem(new Long(100), new Long(10), null) );
        list.add( new SimpleTreeItem(new Long(101), new Long(10), null) );
        list.add( new SimpleTreeItem(new Long(102), new Long(10), null) );
        list.add( new SimpleTreeItem(new Long(103), new Long(10), null) );
        list.add( new SimpleTreeItem(new Long(104), new Long(10), null) );

        list.add( new SimpleTreeItem(new Long(21), new Long(2), null) );
        list.add( new SimpleTreeItem(new Long(21), new Long(2), null) );

        list.add( new SimpleTreeItem(new Long(30), new Long(3), null) );

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
