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

 * Date: Apr 2, 2003

 * Time: 4:37:39 PM

 *

 * $Id$

 */



package org.riverock.common.html;



import junit.framework.TestCase;



public class TestAcceptLanguage extends TestCase

{

    public static class ITAL //InternalTestAcceptLanguage

    {

        public String s = null;

        public AcceptLanguageWithLevel acceptValueArray[] = null;

        public boolean isException = false;

        public boolean isResultNull = false;





        public ITAL(){}



        public ITAL(String s_, AcceptLanguageWithLevel acceptValueArray_[], boolean isException_, boolean isResultNull_)

        {

            this.s = s_;

            this.acceptValueArray = acceptValueArray_;

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

                    "ru,en;q=0.8,en-gb;q=0.5,ja;q=0.33",

                    new AcceptLanguageWithLevel[]

                {

                    new AcceptLanguageWithLevel("ru", 1),

                    new AcceptLanguageWithLevel("en", 0.8f),

                    new AcceptLanguageWithLevel("en-gb", 0.5f),

                    new AcceptLanguageWithLevel("ja", 0.33f),



                },

                false, false),

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

                    }

                }

            }

            catch(Exception exc)

            {

                assertEquals(true, testAcceptLanguageArray[i].isException);



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

    }

}

