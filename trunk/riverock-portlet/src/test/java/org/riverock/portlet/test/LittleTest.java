/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * User: Admin
 * Date: Jan 25, 2003
 * Time: 9:57:52 PM
 *
 * $Id$
 */
package org.riverock.portlet.test;

public class LittleTest
{

    public byte LitleTest()
    {
//        byte b; b = (b + 5);
//        float f = 3.1;

        double d = 1.0 + 0.2 - 1.0;
        return 0;
    }

    public static void main(String s[])
        throws Exception
    {
        Class c = Void.class;

        float f = 30;
        Integer i = new Integer((int)(30 * ((f+100)/100)) );

        System.out.println("i - "+i.intValue());

        System.out.println("Class name - "+c.getName());
//        byte b = '\l';
    }
}
