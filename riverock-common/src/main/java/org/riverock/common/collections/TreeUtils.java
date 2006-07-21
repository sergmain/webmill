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

/**
 * $Id$
 */
package org.riverock.common.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.Collections;

import org.apache.log4j.Logger;

import org.riverock.interfaces.common.TreeItem;


public final class TreeUtils {

    private final static Logger log = Logger.getLogger( TreeUtils.class );
    private final static TreeItemComparator comparator = new TreeItemComparator();

    private static class TreeItemComparator implements Comparator<TreeItem> {
        public int compare(TreeItem item1, TreeItem item2) {

            if (item1 ==null && item2 ==null)
                return 0;

            if (item1 ==null)
                return 1;

            if (item2 ==null)
                return -1;

            if ( item1.getTopId()==null && item2.getTopId()==null)
                return 0;

            if ( item1.getTopId()!=null && item2.getTopId()==null )
                return -1;

            if ( item1.getTopId()==null && item2.getTopId()!=null)
                return 1;


            if ( item1.getTopId().equals( item2.getTopId()))
                return 1;

            if ( item1.getTopId() > item2.getTopId())
                return 1;
            else
                return -1;
        }
    }

    public static List<TreeItem> rebuildTree( final List<TreeItem> source) {

        List<TreeItem> treeItems = new LinkedList<TreeItem>(source);
        Collections.sort(treeItems, comparator);
        List<TreeItem> result = null;
        List<TreeItem> v = new ArrayList<TreeItem>();
        Long id = 0L;

        if (log.isDebugEnabled()) log.debug("Before rebuid. Size of treeItems " + treeItems.size());

        while (treeItems.size()>0) {
            if (log.isDebugEnabled()) log.debug("#1.01.01 treeItems.size() - " + treeItems.size() + "; ID -  " + id);

            for (TreeItem item : treeItems ) {
                if ( id.equals( item.getTopId() ) )
                    v.add(item);
            }

            if (log.isDebugEnabled()) {
                log.debug("#1.01.02 v.size() - " + v.size());
                if (result != null) log.debug("#1.01.05 result.size() -  " + result.size());
            }

            if (result == null) {
                if (log.isDebugEnabled()) log.debug("Init result Vector");

                result = new ArrayList<TreeItem>();
                for (TreeItem vItem : v)
                    result.add(vItem);

                if (log.isDebugEnabled()) log.debug("Finish init result Vector. result.size() - " + result.size());
            }
            else
                putList(result, v, id);

            if (log.isDebugEnabled()) log.debug("#1.01.07 result.size() - " + result.size());

            treeItems.removeAll(v);

            if (log.isDebugEnabled()) log.debug("#1.01.09 treeItems.size() - " + treeItems.size());

            v.clear();
            if (log.isDebugEnabled()) {
                log.debug("#1.01.11 ctx.List.size() - " + treeItems.size());
                log.debug("#1.01.13 result.size() - " + result.size());
            }

            if (treeItems.size() > 0)
                id = treeItems.get(0).getTopId();
        }
        treeItems.clear();

        if (result != null)
            treeItems = result;
        else
            treeItems = new ArrayList<TreeItem>();

        if (log.isDebugEnabled()) log.debug("After rebuid. Size of treeItems " + treeItems.size());

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