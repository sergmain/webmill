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
package org.riverock.common.html;

import java.util.Locale;

import junit.framework.TestCase;
import org.riverock.common.tools.StringTools;

/**
 * Author: mill
 * Date: Apr 2, 2003
 * Time: 4:37:39 PM
 *
 * $Id$
 */
public class TestAcceptLanguage extends TestCase
{
    public static class ITAL //InternalTestAcceptLanguage
    {
        public String s = null;
        public AcceptLanguageWithLevel acceptValueArray[] = null;
        public boolean isException = false;
        public boolean isResultNull = false;
        public Locale[] locales = null;

        public ITAL(){}

        public ITAL(
            String s_, AcceptLanguageWithLevel acceptValueArray_[],
            Locale locales[],
            boolean isException_, boolean isResultNull_)
        {
            this.s = s_;
            this.acceptValueArray = acceptValueArray_;
            this.locales = locales;
            this.isException = isException_;
            this.isResultNull = isResultNull_;
        }
    }

    public void testAcceptLanguage()
     throws Exception
    {
        ITAL[] testAcceptLanguageArray =
            {
                new ITAL(
                    "ru,en;q=0.8,en-gb;q=0.5,ja;q=0.33,de;q=0.8",
                    new AcceptLanguageWithLevel[]
                    {
                        new AcceptLanguageWithLevel("ru"),
                        new AcceptLanguageWithLevel("en", 0.8f),
                        new AcceptLanguageWithLevel("en-gb", 0.5f),
                        new AcceptLanguageWithLevel("ja", 0.33f),
                        new AcceptLanguageWithLevel("de", 0.8f)
                    },
                    new Locale[]{
                        StringTools.getLocale("ru"),
                        StringTools.getLocale("en"),
                        StringTools.getLocale("de"),
                        StringTools.getLocale("en-gb"),
                        StringTools.getLocale("ja"),
                    },
                    false, false
                ),
                new ITAL(
                    "en;q=1a",
                    null,
                    null,
                    true, false
                ),
                new ITAL(
                    null,
                    new AcceptLanguageWithLevel[]{},
                    new Locale[]{},
                    false, false
                ),
                new ITAL(
                    "",
                    new AcceptLanguageWithLevel[]{},
                    new Locale[]{},
                    false, false
                )
            };



        for (int i=0; i<testAcceptLanguageArray.length; i++)
        {
            try
            {
                AcceptLanguageWithLevel r[] = Header.getAcceptLanguageArray(testAcceptLanguageArray[i].s);

                if (testAcceptLanguageArray[i].isResultNull)
                    assertEquals(true, r==null);
                else
                {
                    assertEquals(true, r!=null);
                    assertEquals(true, r.length==testAcceptLanguageArray[i].acceptValueArray.length);

                    for (int k=0; k<r.length; k++)
                    {
                        AcceptLanguageWithLevel a = testAcceptLanguageArray[i].acceptValueArray[k];
                        AcceptLanguageWithLevel r1 = r[k];

                        assertEquals(a.locale, r1.locale);
                        assertEquals(true, a.level==r1.level);
                        assertNotNull(a.toString());
                    }

                    Locale locale[] = Header.getAcceptLanguageAsLocaleListSorted(testAcceptLanguageArray[i].s);
                    assertEquals(true, locale.length==testAcceptLanguageArray[i].locales.length);

                    for (Locale aLocale : locale) {
                        System.out.println("locale[l] = " + aLocale);
                    }

                    for (int l=0; l<locale.length; l++)
                    {
                        System.out.println("locale[l] = " + locale[l]+", testAcceptLanguageArray[i].locales[i]) "+testAcceptLanguageArray[i].locales[l]);
                        assertEquals(true, locale[l].equals( testAcceptLanguageArray[i].locales[l]) );
                    }

                }
            }
            catch(Exception exc)
            {
//                assertEquals(true, testAcceptLanguageArray[i].isException);

                if (!testAcceptLanguageArray[i].isException)
                {
                    System.out.println(
                        "array index - "+i+", string - "+
                        testAcceptLanguageArray[i].s
                    );
                    throw exc;
                }
            }
        }

        assertNotNull(new AcceptLanguageWithLevel(null).toString());
    }
}
