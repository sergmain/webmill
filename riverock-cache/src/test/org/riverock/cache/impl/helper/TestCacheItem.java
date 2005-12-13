/*
 * org.riverock.cache -- Generic cache implementation
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

package org.riverock.cache.impl.helper;

/**
 * User: serg_main
 * Date: Aug 7, 2003
 * Time: 2:24:42 PM
 * $Id$
 * @author Serge Maslyukov
 */
public class TestCacheItem
{
    public long id;
    public long value;

    public TestCacheItem(){}

    public TestCacheItem(long id_, long value_)
    {
        this.id = id_;
        this.value = value_;
    }
}