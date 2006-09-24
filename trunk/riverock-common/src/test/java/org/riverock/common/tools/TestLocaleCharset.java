/*
 * org.riverock.common - Supporting classes and utilities
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

package org.riverock.common.tools;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * LocaleCharset Tester.
 *
 * @author <Authors name>
 * @since <pre>09/05/2006</pre>
 * @version 1.0
 * 
 * $Id$
 */
public class TestLocaleCharset extends TestCase {

    public void testConstructor() throws Exception {
       new LocaleCharset();
    }

    public void testGetCharset() throws Exception {
        //TODO: Test goes here...
    }

    public static Test suite() {
        return new TestSuite(TestLocaleCharset.class);
    }
}
