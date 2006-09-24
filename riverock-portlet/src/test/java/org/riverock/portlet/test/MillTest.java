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

package org.riverock.portlet.test;

import org.riverock.common.tools.NumberTools;
import junit.framework.Assert;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;


/**
 * 
 * $Author$
 *
 * $Id$
 *
 */
public class MillTest
{
    private static Logger cat = Logger.getLogger( MillTest.class );

    public MillTest()
    {
    }

    public static void main(String args[])
    {
        System.out.println("test toString() " +NumberTools.toString( 1234.34, 3));
        System.out.println("value of expresion: "+(1.0+0.2-1.0));
        BigInteger bigInt = new BigInteger("492061");
        Double dd = 1.0;

        BigDecimal big1 = new BigDecimal(198.2);
        big1.setScale(1, BigDecimal.ROUND_DOWN);
        double d1 = big1.multiply( new BigDecimal( 3 )).doubleValue();
        double resultMultiple = 198.2 * 3;

        d1 = NumberTools.multiply(7624786584.69, 584, 2);
        System.out.println( "value of multiply - "+d1);
        Assert.assertEquals( "Double multiple test\n", 4452875365458.96 ,d1 , 0);

        d1 = NumberTools.multiply(492061.099709, 467808, 6);
        System.out.println( "value of multiply - "+d1);
        Assert.assertEquals( "Double multiple test\n", 230190118932.667872, d1, 0);

        d1 = NumberTools.multiply(198.2, 3, 2);
        System.out.println( "value of multiply - "+d1);
        Assert.assertEquals( "Double multiple test", 594.6 , d1, 0);

        double price = 1234567890.78912345;
        BigDecimal big = new BigDecimal( price );
        System.out.println( "BigDecimal.scale - "+big.scale());
        System.out.println( "Max double- "+Double.MAX_VALUE  );
        System.out.println( "Price - "+price);
        System.out.println( "Price big - "+big.toString() );

        System.out.println( "exp - "+Math.exp( price));
        System.out.println( "floor - "+Math.floor( price ));
        System.out.println( "ceil - "+Math.ceil( price ));

        Assert.assertEquals( "Floor test", Math.floor( price), NumberTools.truncate(price, 0) ,0);
        Assert.assertEquals( "Test 1", NumberTools.truncate(price, 99), price, 0);

        Assert.assertEquals( "Round   4 digit", NumberTools.truncate(price, 4), 1234567890.7891, 0);
        Assert.assertEquals( "Round   0 digit", NumberTools.truncate(price, 0), 1234567890, 0);
        Assert.assertEquals( "Round  -4 digit", NumberTools.truncate(price, -4), 1234560000.0, 0);
        Assert.assertEquals( "Round  -6 digit", NumberTools.truncate(price, -6), 1234000000.0, 0);

        price = 130.35;
        Assert.assertEquals( "Round  3 digit", NumberTools.truncate(130.35, 2), 130.35, 0);

        System.out.println("Results of decimal formatting and rounding.\n");

        double d = 5457891.39728;
        int i = (int)(d + 0.5);

        System.out.println("Casting to int it is;- " + i);
        System.out.println("Using Math.round it is;- " + (Math.round(d)));
        DecimalFormat twoPlaces = new DecimalFormat("0.00");
        System.out.println("Result to 2 decimal places is;- " + twoPlaces.format(d));

        price = 1234567890.78912345;
        BigDecimal num = new BigDecimal( price );

        System.out.println("num " + num);
        System.out.println("num " + num.setScale(2, BigDecimal.ROUND_DOWN));
        System.out.println("num " + num.setScale(7, BigDecimal.ROUND_DOWN));
        System.out.println("num " + num.setScale(0, BigDecimal.ROUND_DOWN));

    }

}