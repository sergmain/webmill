/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Collections;

import org.riverock.interfaces.common.TreeItem;

/**
 * $Id$
 */
public final class TreeUtils {

    private static TreeItemComparator comparator = new TreeItemComparator();

    /**
     * transform tree in list
     * @param nodes tree
     * @return List list
     */
    public static List<TreeItem> toPlainList(List<TreeItem> nodes) {
        List<TreeItem> result = new ArrayList<TreeItem>();
        if (nodes==null) {
            return result;
        }
        for (TreeItem treeItem : nodes) {
            result.add(treeItem);
            result.addAll(toPlainList(treeItem.getSubTree()));
        }
        return result;
    }

    public static List<TreeItem> rebuildTree( final List<TreeItem> source) {

        List<TreeItem> treeItems = new LinkedList<TreeItem>(source);
        List<TreeItem> sortedTreeItems = new LinkedList<TreeItem>(source);
        Collections.sort(sortedTreeItems, comparator);
        List<TreeItem> result = null;
        List<TreeItem> v = new ArrayList<TreeItem>();
        Long id = null;

        while (treeItems.size()>0) {

            for (TreeItem item : treeItems ) {
                if (
                    (item.getTopId()==null && id==null) ||
                        (id==null && item.getTopId()==0) ||
                        (item.getTopId()!=null && id!=null && id.equals( item.getTopId() ))
                    )
                    v.add(item);
            }

            if (result == null) {

                result = new ArrayList<TreeItem>();
                for (TreeItem vItem : v)
                    result.add(vItem);

            }
            else {
                putList(result, v, id);
            }

            treeItems.removeAll(v);
            sortedTreeItems.removeAll(v);
            if (treeItems.size()!=sortedTreeItems.size()) {
                throw new RuntimeException("sizes of lists are miismatch");
            }

            v.clear();

            if (sortedTreeItems.size() > 0) {
                id = sortedTreeItems.get(0).getTopId();
            }
        }
        treeItems.clear();

        if (result != null) {
            treeItems = result;
        }
        else {
            treeItems = new ArrayList<TreeItem>();
        }

        return treeItems;
    }

    private static boolean putList( final List<TreeItem> target, final List<TreeItem> data, final Long id) {

        if (target==null || id==null)
            return false;

        ListIterator it = target.listIterator();
        while (it.hasNext()) {
            TreeItem item = (TreeItem)it.next();

            if (id.equals(item.getId())) {
                List<TreeItem> list = new ArrayList<TreeItem>();
                for ( TreeItem treeItem : data) {
                    list.add( treeItem );
                }
                item.setSubTree( list );
                return true;
            }
            else {
                if (putList(item.getSubTree(), data, id))
                    return true;
            }
        }
        return false;
    }
}