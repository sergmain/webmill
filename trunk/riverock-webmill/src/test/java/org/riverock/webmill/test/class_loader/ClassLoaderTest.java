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
package org.riverock.webmill.test.class_loader;

import java.io.InputStream;

/**
 * @author Serge Maslyukov
 *         Date: 23.04.2005
 *         Time: 23:44:23
 *         $Id$
 */
public class ClassLoaderTest {
    public static void main(String[] s) throws Exception {

        String res = "/org/riverock/webmill/test/class_loader/prop/LocaleTest.properties";
        InputStream inputStream = ClassLoaderTest.class.getClassLoader().getResourceAsStream( res );

        System.out.println("inputStream = " + inputStream);

    }
}
