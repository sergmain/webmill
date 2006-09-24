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

import java.util.Locale;

import junit.framework.TestCase;

/**
 * Author: mill
 * Date: Feb 17, 2003
 * Time: 12:17:51 PM
 * <p/>
 * $Id$
 */
public class TestCaseStringTools extends TestCase {
    public TestCaseStringTools() {
        super();
    }

    public TestCaseStringTools(String testName) {
        super(testName);
    }

    private void testLocale(TestLocale tl)
        throws Exception {
        Locale l = StringTools.getLocale(tl.s);
        String locale = tl.s;
        if (tl.isVariant) {
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale variant is null", l.getVariant() == null);
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale variant not valid", (!l.getVariant().equals(tl.v)));
        }

        if (tl.isCountry) {
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale country is null", l.getCountry() == null);
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale country not valid", (!l.getCountry().equals(tl.c)));
        }

        if (tl.isLang) {
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale language is null", l.getLanguage() == null);
            assertFalse(tl.s + ", " + l + ", " + locale + " Locale language not valid", (!l.getLanguage().equals(tl.l)));
        }
    }

    class TestLocale {
        String s;
        String l;
        String c;
        String v;
        boolean isLang;
        boolean isCountry;
        boolean isVariant;

        public TestLocale(String s,
                          String l, String c, String v,
                          boolean isLang, boolean isCountry, boolean isVariant) {
            this.s = s;
            this.l = l;
            this.c = c;
            this.v = v;
            this.isLang = isLang;
            this.isCountry = isCountry;
            this.isVariant = isVariant;
        }
    }

    public void testGetLocaleFromString() throws Exception {
        TestLocale[] localeToTest =
            {
                new TestLocale("ru_RU_TEST", "ru", "RU", "test", true, true, true),
                new TestLocale("en", "en", null, null, true, false, false),
                new TestLocale("en_GB", "en", "GB", null, true, true, false),
                new TestLocale("en_GB_HE", "en", "GB", "he", true, true, true),
                new TestLocale("ru", "ru", null, null, true, false, false),
                new TestLocale("ru_RU", "ru", "RU", null, true, true, false),
                new TestLocale("en_US_TEST", "en", "US", "test", true, true, true),
                new TestLocale("en-US-TEST", "en", "US", "test", true, true, true),
                new TestLocale("en_US-TEST", "en", "US", "test", true, true, true)
            };

        for (TestLocale aLocaleToTest : localeToTest)
            testLocale(aLocaleToTest);
    }

    class TestCapitalizeString {
        String s;
        String t;

        public TestCapitalizeString(String s, String t) {
            this.s = s;
            this.t = t;
        }
    }

    private void testCapitalizeInternal(TestCapitalizeString tl)
        throws Exception {
        assertTrue(
            "\nError capitalize string '" + tl.s + "'\n" +
                "Expected '" + tl.t + "', but result is '" + StringTools.capitalizeString(tl.s) + "'",
            tl.t.equals(StringTools.capitalizeString(tl.s))
        );
    }

    public void testCapitalizeString()
        throws Exception {
        TestCapitalizeString[] capitalize =
            {
                new TestCapitalizeString("TEST", "Test"),
                new TestCapitalizeString("TEST_", "Test"),
                new TestCapitalizeString("TEST__", "Test"),
                new TestCapitalizeString("_TEST", "Test"),
                new TestCapitalizeString("TEST_TEST", "TestTest"),
                new TestCapitalizeString("TEST_TEST_TEST", "TestTestTest"),
                new TestCapitalizeString("TEST_T", "TestT"),
                new TestCapitalizeString("TEST__T", "TestT")
            };

        for (TestCapitalizeString aCapitalize : capitalize) {
            System.out.println("s '" + aCapitalize.s + "', t '" + aCapitalize.t + "'");
            testCapitalizeInternal(aCapitalize);
        }
    }

    public void testAppendString() throws Exception {
        assertNull(StringTools.appendString(null, ' ', 0, true));
        assertEquals("aaaBBB", StringTools.appendString("aaa", 'B', 6, false));
        assertEquals("BBBaaa", StringTools.appendString("aaa", 'B', 6, true));
    }

    public void testAddString() throws Exception {
        assertNull(StringTools.addString(null, ' ', 0, true));
        assertEquals("aaaBBB", StringTools.addString("aaa", 'B', 3, false));
        assertEquals("BBBaaa", StringTools.addString("aaa", 'B', 3, true));
    }

    public static void main(String[] s) {
        Locale l = StringTools.getLocale("en");

        String loc = l.toString();
        System.out.println(loc);
    }
}
