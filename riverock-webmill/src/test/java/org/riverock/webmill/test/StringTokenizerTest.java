/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.test;

import java.util.StringTokenizer;

/**
 * @author SMaslyukov
 *         Date: 29.05.2005
 *         Time: 16:05:11
 *         $Id$
 */
public class StringTokenizerTest {
    public static void main(String[] args) {
        String s = "role1, role2, role3";
        System.out.println("#1");
        StringTokenizer st = new StringTokenizer(s, ", ", false);
        while (st.hasMoreTokens()) {
            System.out.println(">"+st.nextToken()+"<");

        }
        System.out.println("#2");
        st = new StringTokenizer(s, ",");
        while (st.hasMoreElements()) {
            System.out.println(">"+st.nextElement()+"<");

        }
        System.out.println("#3");
        st = new StringTokenizer(s);
        while (st.hasMoreElements()) {
            System.out.println(">"+st.nextElement()+"<");
        }
    }
}
