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

import junit.framework.TestCase;

/**
 * @author Sergei Maslyukov
 *         Date: 05.09.2006
 *         Time: 18:43:10
 *         <p/>
 *         $Id$
 */
public class TestExceptionTools extends TestCase {

    public void testConstructor() throws Exception {
        new ExceptionTools();
    }

    public void testGetStackTrace() throws Exception {
        Throwable th = new Throwable("eee");
        assertNotNull(ExceptionTools.getStackTrace(th, 2000));
        assertNotNull(ExceptionTools.getStackTrace(th, 2000, "<br>"));
        assertEquals(ExceptionTools.getStackTrace(null, 10), ExceptionTools.THROWABLE_IS_NULL);
        assertEquals(ExceptionTools.getStackTrace(null, 10, "<br>"), ExceptionTools.THROWABLE_IS_NULL);

        try {
            call1();
        }
        catch (Throwable e) {
            th = e;
        }
        assertNotNull(ExceptionTools.getStackTrace(th, 1));
        assertNotNull(ExceptionTools.getStackTrace(th, 1, "<br>"));

    }

    private void call1() {
        call2();
    }

    private void call2() {
        throw new IllegalStateException("error");
    }

}
