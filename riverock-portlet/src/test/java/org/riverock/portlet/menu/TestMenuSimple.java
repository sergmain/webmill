/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.menu;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * $Author: serg_main $
 *
 * $Id: TestMenuSimple.java 1426 2007-09-13 11:18:09Z serg_main $
 *
 */
public class  TestMenuSimple extends TestCase {
    
    public void testDecodeLevel() {
        Assert.assertEquals(MenuLevel.UNKNOWN_LEVEL, MenuSimple.decodeLevel("hkfjsdhfksdj"));
        Assert.assertEquals(MenuLevel.UNKNOWN_LEVEL, MenuSimple.decodeLevel(null));
        Assert.assertEquals(MenuLevel.EQUAL_LEVEL, MenuSimple.decodeLevel("equal"));
        Assert.assertEquals(MenuLevel.GREAT_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("great_or_equal"));
        Assert.assertEquals(MenuLevel.GREAT_THAN_LEVEL, MenuSimple.decodeLevel("great"));
        Assert.assertEquals(MenuLevel.LESS_OR_EQUAL_LEVEL, MenuSimple.decodeLevel("less_or_equal"));
        Assert.assertEquals(MenuLevel.LESS_THAN_LEVEL, MenuSimple.decodeLevel("less"));
    }
}