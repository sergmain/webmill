/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.tools;

import junit.framework.TestCase;

/**
 * User: Admin
 * Date: Dec 14, 2002
 * Time: 2:22:59 PM
 *
 * $Id$
 */
public class TestCaseNumberTools extends TestCase
{
    public TestCaseNumberTools() {
        super();
    }

    public TestCaseNumberTools(String testName) {
        super(testName);
    }

    private final static int countLoop = 50000;

    public void testDoubleMultiply_4_4_6()
    {
        testDoubleMultiply(4,4,6);
    }

    public void testDoubleMultiply_2_6_6()
    {
        testDoubleMultiply(2,6,6);
    }

    public void testDoubleMultiply_0_6_8()
    {
        testDoubleMultiply(0,6,8);
    }




//    int maxPrecision = 4; // количество цифр после запятой
//    int maxDecimalPart = 4; // количество цифр до запятой
//    int multiplyDigit = 6; // количество цифр в множителе
//    int count = 50000;
    private void testDoubleMultiply( int maxPrecision, int maxDecimalPart, int multiplyDigit)
    {
        for (int i=0; i<countLoop; i++)
        {
            double initRandom1 = Math.random();
            double randomRightPart = NumberTools.truncate(initRandom1, maxPrecision );

            double initRandom2 = Math.random();
            double doubleDecimalPartInit = NumberTools.truncate(
                        initRandom2,
                        maxDecimalPart
                    );
            double doubleDecimalPart =
                NumberTools.truncate(
                    doubleDecimalPartInit,
                    maxDecimalPart
                );
            long randomLeftPart =
                (long)(NumberTools.movePrecisionRight(
                    doubleDecimalPart,
                    maxDecimalPart).doubleValue());

            double value = randomLeftPart + randomRightPart;

            double initRandom3 = Math.random();
            long randomMultiply =
                (long)NumberTools.movePrecisionRight(
                NumberTools.truncate(initRandom3, multiplyDigit ),
                    multiplyDigit).doubleValue();

            double resultValue = NumberTools.multiply(value, (int)randomMultiply, maxPrecision);

            double checkRightValue =
                NumberTools.truncate(
                    NumberTools.getPrecisionValue(
                        randomRightPart,
                        (int)randomMultiply,
                        maxPrecision
                    ).doubleValue()*randomMultiply,
                    maxPrecision
                );

            double checkValue =
                NumberTools.truncate(
                    (randomLeftPart*randomMultiply)+checkRightValue,
                    maxPrecision
                );

            assertEquals("\nLoop #"+i+". Test multiply double value.\nLeft "+randomLeftPart+
                ", right "+randomRightPart+", multy "+randomMultiply+
                "\nleft*mul "+(randomLeftPart*randomMultiply)+
                " right*mul "+checkRightValue+
                " result mul "+checkValue+
                "\ncheck value "+resultValue+
                "\n",
                checkValue,
                resultValue,
                0);
        }
//        System.out.println("End of test double multiply");
    }


    public static class InternalNumberToolsTruncate
    {
        public double in;
        public double out;
        public int c;
        public boolean isException = false;
        public boolean isResultNull = false;

        public InternalNumberToolsTruncate(){}

        public InternalNumberToolsTruncate(double in_, double out_, int c_, boolean isException_)
        {
            this.in = in_;
            this.out = out_;
            this.c = c_;
            this.isException = isException_;
        }
    }

    public void testTruncate() throws Throwable {
        
        InternalNumberToolsTruncate[] testDateWithMaskArray =
            {
                new InternalNumberToolsTruncate(198.123, 198.123, 4, false),
                new InternalNumberToolsTruncate(0.1234, 0.12, 2, false),
                new InternalNumberToolsTruncate(198.432, 198.4, 1, false),
                new InternalNumberToolsTruncate(198.12, 198, 0, false),
                new InternalNumberToolsTruncate(12, 0, -3, false),
                new InternalNumberToolsTruncate(198.12, 198, 0, false)
            };

        assertEquals("0", NumberTools.toString(198.12, -3));
        assertEquals("100", NumberTools.toString(198.12, -2));
        assertEquals("190", NumberTools.toString(198.12, -1));
        assertEquals("198", NumberTools.toString(198.12, 0));
        assertEquals("198.1", NumberTools.toString(198.12, 1));
        assertEquals("198.12", NumberTools.toString(198.12, 2));
        assertEquals("198.120", NumberTools.toString(198.12, 3));

        assertEquals("198", NumberTools.toString(198, 0));
        assertEquals("198.0", NumberTools.toString(198, 1));
        assertEquals("198.00", NumberTools.toString(198, 2));

        NumberTools numberTools = new NumberTools();

        for (int i=0; i<testDateWithMaskArray.length; i++) {
            double d = 0;
            try
            {
                d = NumberTools.truncate(
                    testDateWithMaskArray[i].in,
                    testDateWithMaskArray[i].c
                );

                assertEquals(true, testDateWithMaskArray[i].out==d);
            }
            catch(Throwable exc) {
                if (!testDateWithMaskArray[i].isException)
                {
                    System.out.println(
                        "array index - "+i+", in - "+
                        testDateWithMaskArray[i].in+
                        ",out - "+testDateWithMaskArray[i].out+
                        ",chars - "+testDateWithMaskArray[i].c+
                        ",result - "+d
                    );
                    throw exc;
                }
            }
        }

    }


}
