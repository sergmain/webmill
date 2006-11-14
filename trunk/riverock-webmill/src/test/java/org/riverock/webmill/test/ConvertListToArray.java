/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.webmill.test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 12.05.2006
 *         Time: 14:31:07
 */
public class ConvertListToArray {
    public static void main(String[] args) {
        List<String> sun = new ArrayList<String>();
        sun.add("Feel");
        sun.add("the");
        sun.add("power");
        sun.add("of");
        sun.add("the");
        sun.add("Sun");
        String[] s1 = sun.toArray(new String[0]); //Collection to array
        for (String contents : s1) {
            System.out.print(contents);
        }
        System.out.println();
        List<String> sun2 = Arrays.asList(s1); //Array back to Collection
        for (String s2 : sun2) {
            String s3 = s2;
            System.out.print(s3);
        }
        System.out.println();

        String s = StringTools.arrayToString(sun.toArray(new String[0]));
        System.out.println("s = " + s);

//        sun2.add(new String("Hello")); // throws UnsupportedOperationException
    }
}
