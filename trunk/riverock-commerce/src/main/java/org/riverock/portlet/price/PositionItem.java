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
package org.riverock.portlet.price;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public class PositionItem
{
    public String name = null;
    public String url = null;
    public long id_group_current = 0;
    public long id_group_top = 0;

    protected void finalize() throws Throwable
    {
        name = null;
        url = null;

        super.finalize();
    }

    public PositionItem(String n_, String u_, long id_curr, long id_top)
    {
        name = n_;
        url = u_;

        id_group_current = id_curr;
        id_group_top = id_top;

    }

    public PositionItem(){}
}
