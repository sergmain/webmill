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
package org.riverock.portlet.menu;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public class  TestMenuSimple extends TestCase {
    public static void testDecodeLevel() {
        Assert.assertEquals(MenuSimple.UNKNOWN_LEVEL, MenuSimple.decodeLevel("hkfjsdhfksdj"));
        Assert.assertEquals(MenuSimple.UNKNOWN_LEVEL, MenuSimple.decodeLevel(null));
        Assert.assertEquals(MenuSimple.EQUAL_LEVEL, MenuSimple.decodeLevel("equal"));
        Assert.assertEquals(MenuSimple.GREAT_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("great_or_equal"));
        Assert.assertEquals(MenuSimple.GREAT_THAN_LEVEL, MenuSimple.decodeLevel("great"));
        Assert.assertEquals(MenuSimple.LESS_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("less_or_equal"));
        Assert.assertEquals(MenuSimple.LESS_THAN_LEVEL, MenuSimple.decodeLevel("less"));
    }
}