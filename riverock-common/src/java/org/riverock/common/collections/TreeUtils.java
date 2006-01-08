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

import org.apache.log4j.Logger;
import org.riverock.interfaces.common.TreeItem;

import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;


public final class TreeUtils {

    private final static Logger log = Logger.getLogger( TreeUtils.class );

    public static LinkedList rebuildTree( final LinkedList source) {
                             
        LinkedList menuItem = (LinkedList)source.clone();
        LinkedList result = null;
        LinkedList v = new LinkedList();
        Long id = 0L;

        if (log.isDebugEnabled()) log.debug("Before rebuid. Size of menuItem " + menuItem.size());

        while (menuItem.size()>0) {
            if (log.isDebugEnabled()) log.debug("#1.01.01 menuItem.size() - " + menuItem.size() + "; ID -  " + id);


            ListIterator it = menuItem.listIterator();
            while (it.hasNext()) {
                TreeItem item = (TreeItem)it.next();
                if ( id.equals( item.getTopId() ) )
                    v.add(item);
            }

            if (log.isDebugEnabled()) {
                log.debug("#1.01.02 v.size() - " + v.size());
                if (result != null) log.debug("#1.01.05 result.size() -  " + result.size());
            }

            if (result == null) {
                if (log.isDebugEnabled()) log.debug("Init result Vector");

                result = (LinkedList)v.clone();

                if (log.isDebugEnabled()) log.debug("Finish init result Vector. result.size() - " + result.size());
            }
            else
                putList(result, v, id);

            if (log.isDebugEnabled()) log.debug("#1.01.07 result.size() - " + result.size());

            menuItem.removeAll(v);

            if (log.isDebugEnabled()) log.debug("#1.01.09 menuItem.size() - " + menuItem.size());

            v.clear();
            if (log.isDebugEnabled()) {
                log.debug("#1.01.11 ctx.List.size() - " + menuItem.size());
                log.debug("#1.01.13 result.size() - " + result.size());
            }

            if (menuItem.size() > 0)
                id = ((TreeItem) menuItem.get(0)).getTopId();
        }
        menuItem = null;
        if (result != null)
            menuItem = result;
        else
            menuItem = new LinkedList();

        if (log.isDebugEnabled()) log.debug("After rebuid. Size of menuItem " + menuItem.size());

        return menuItem;
    }

    private static boolean putList( final List target, final LinkedList data, final Long id) {

        if (target==null || id==null)
            return false;

        ListIterator it = target.listIterator();
        while (it.hasNext()) {
            TreeItem item = (TreeItem)it.next();

            if (id.equals(item.getId())) {
                item.setSubTree( (LinkedList)data.clone() );
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