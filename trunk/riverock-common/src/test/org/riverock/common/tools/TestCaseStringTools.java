/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * Author: mill
 * Date: Feb 17, 2003
 * Time: 12:17:51 PM
 *
 * $Id$
 */

package org.riverock.common.tools;

import java.util.Locale;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class TestCaseStringTools extends TestCase
{
    private static Logger cat = Logger.getLogger("org.riverock.common.tools.TestCaseStringTools");

    public TestCaseStringTools(String testName)
    {
        super(testName);
    }

    private void testLocale(TestLocale tl)
        throws Exception
    {
        Locale l = StringTools.getLocale( tl.s );
        String locale = tl.s;
        if (tl.isVariant)
        {
            assertFalse(locale+" Locale variant is null", l.getVariant()==null );
            assertFalse(locale+" Locale variant not valid", (!l.getVariant().equals( tl.v )));
        }

        if (tl.isCountry)
        {
            assertFalse(locale+" Locale country is null", l.getCountry()==null );
            assertFalse(locale+" Locale country not valid", (!l.getCountry().equals( tl.c )));
        }

        if (tl.isLang)
        {
            assertFalse(locale+" Locale language is null", l.getLanguage()==null );
            assertFalse(locale+" Locale language not valid", (!l.getLanguage().equals( tl.l )));
        }
    }

    class TestLocale
    {
        String s;
        String l;
        String c;
        String v;
        boolean isLang;
        boolean isCountry;
        boolean isVariant;

        public TestLocale( String s,
            String  l,String  c,String  v,
            boolean isLang, boolean isCountry, boolean isVariant)
        {
            this.s = s;
            this.l = l;
            this.c = c;
            this.v = v;
            this.isLang = isLang;
            this.isCountry = isCountry;
            this.isVariant = isVariant;
        }
    }

    public void testGetLocaleFromString()
        throws Exception
    {
        TestLocale[] localeToTest =
            {
                new TestLocale("ru_RU_TEST", "ru", "RU", "TEST", true, true, true),
                new TestLocale("en", "en", null, null, true, false, false),
                new TestLocale("en_GB", "en", "GB", null, true, true, false),
                new TestLocale("en_GB_HE", "en", "GB", "HE", true, true, true),
                new TestLocale("ru", "ru", null, null, true, false, false),
                new TestLocale("ru_RU", "ru", "RU", null, true, true, false),
                new TestLocale("en_US_TEST", "en", "US", "TEST", true, true, true)
            };

        for (int i=0; i<localeToTest.length; i++)
            testLocale( localeToTest[i] );
    }

    class TestCapitalizeString
    {
        String s;
        String t;

        public TestCapitalizeString( String s, String t)
        {
            this.s = s;
            this.t = t;
        }
    }

    private void testCapitalizeInternal(TestCapitalizeString tl)
        throws Exception
    {
        assertTrue(
            "\nError capitalize string '"+tl.s+"'\n" +
            "Expected '"+tl.t+"', but result is '"+StringTools.capitalizeString(tl.s)+"'",
            tl.t.equals(StringTools.capitalizeString(tl.s))
        );
    }

    public void testCapitalizeString()
        throws Exception
    {
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

        for (int i=0; i<capitalize.length; i++)
        {
            System.out.println("s '"+capitalize[i].s+"', t '"+capitalize[i].t+"'");
            testCapitalizeInternal( capitalize[i] );
        }
    }


    public static void main(String[] s)
    {
        Locale l = StringTools.getLocale( "en" );

        String loc = l.toString();
        System.out.println(loc);
    }
}
