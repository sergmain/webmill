/*
 * org.riverock.sso -- Single Sign On implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.sso.test;

import org.riverock.common.tools.NumberTools;
import junit.framework.Assert;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * 
 * $Author$
 *
 * $Id$
 *
 */
public class MillTest
{
    private static Logger cat = Logger.getLogger("org.riverock.test.MillTest" );

    public MillTest()
    {
    }

    public static void main(String args[])
    {
        System.out.println("test toString() " +NumberTools.toString( 1234.34, 3));
        System.out.println("value of expresion: "+(1.0+0.2-1.0));
        BigInteger bigInt = new BigInteger("492061");
        Double dd = new Double(1);

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