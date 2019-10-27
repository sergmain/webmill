/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.deploy.test;

/**
 * @author smaslyukov
 *         Date: 02.08.2005
 *         Time: 17:44:20
 *         $Id: OverflowTest.java 1055 2006-11-14 17:56:15Z serg_main $
 */
public class OverflowTest {
    public static void main(String[] args) {
        int maxValue = Integer.parseInt( args[0] );
        System.out.println("Integer: " +maxValue );
        System.out.println("Integer: " + (maxValue + 1 ) );
    }
}
