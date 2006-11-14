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

import java.util.List;
import java.util.Iterator;

/**
 * @author Sergei Maslyukov
 *         Date: 10.11.2006
 *         Time: 21:27:33
 *         <p/>
 *         $Id$
 */
public class ListUtils {
    public static String listToString(List<Long> list) {
        if (list.isEmpty())
            return "NULL";

        Iterator<Long> iterator = list.iterator();
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            Long aLong = iterator.next();

            if (isFirst) {
		isFirst = false;
	    }
	    else {
                sb.append( ',' );
            }

            sb.append( aLong );
        }
        return sb.toString();
    }
}
