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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: Admin
 * Date: Dec 14, 2002
 * Time: 1:43:45 PM
 *
 * $Id$
 */
public class NumberTools
{
    /**
     * Изменяет значение количества цифр после запятой.
     * значение countChar больше нуля показывает сколько цифр после запятой показывать.
     * Значение countChar равное нулю производит отбрасывание дробной части.
     * Значение countChar меньше нуля округляет цену до количества знаков до запятой.
     *
     * Например:
     * знаков до запятой: 2, цена  198.357, результат 198.35
     * знаков до запятой: 0, цена  198.357, результат 198.0
     * знаков до запятой: -1, цена  198.357, результат 198.35
     * знаков до запятой: -2, цена  198.357, результат 100.0
     * знаков до запятой: -3, цена  198.357, результат 0.0
     *
     * @param d - double. Значение для округления
     * @param countChar - int. Количество цифр после запятой
     * @return - double. Новое значение
     */
    public static double truncate(double d, int countChar) {
        // Окургление влево от запятой
        if (countChar < 0)
        {
            String s = (new BigDecimal(d)).toString().trim();
            int pos = s.indexOf('.');
            int truncPos = Math.abs(countChar);

            if (pos <= truncPos)
                return 0;

            return new Double(
                StringTools.appendString(
                    s.substring(0, pos - truncPos), '0', pos, false
                )
            );
        }
        else if (countChar > 0)
        // Окургление вправо от запятой
        {
            return new BigDecimal(d).add( movePrecisionLeft(1, countChar+1)
            ).setScale(countChar, BigDecimal.ROUND_DOWN).doubleValue();
        }
        return (double) ((long) d);
    }

    public static BigDecimal movePrecisionLeft(double value,  int precision )
    {
        BigDecimal big = new BigDecimal(value);
        big = big.setScale( 20, BigDecimal.ROUND_DOWN);
        big = big.movePointLeft(precision);
        return big;
    }

/*
    public static BigInteger getBigInteger( double value, int precision)
    {
(new BigDecimal( new BigInteger("1234567890123456789012345678901234567890"), 30)).unscaledValue().toString()
(new BigDecimal( new BigInteger("1234567890123456789012345678901234567890"), 30)).scale()
        double newDouble = 12345678.1234567689 + (new BigDecimal( new BigInteger("1"), precision+1) ).doubleValue();
        new Double(12345678.1234567689).toString()
        String doubleString = ""+value;
        if ()
    }
*/

    public static BigDecimal movePrecisionRight(double value,  int precision )
    {
        return new BigDecimal(value).movePointRight(precision);
    }

    public static BigDecimal getPrecisionValue( double val, int mul, int precision)
    {
//new BigDecimal( new BigInteger("492061099709"), 6).add(new BigDecimal( new BigInteger("1"), 12)).toString()

        int newScale = precision+1+(""+mul).length();
        BigDecimal big = new BigDecimal(val);
        big = big.add( new BigDecimal( new BigInteger("1"), newScale) );
//        big = big.add( movePrecisionLeft(1, newScale) );
//        int i = 0;
        return big;
    }

    public static double multiply( double val, int mul, int precision)
    {
        if (precision <= 0)
            return (double)((long)val)*mul;

        BigDecimal big = getPrecisionValue(val, mul, precision);

        double doubleValue = NumberTools.truncate(big.multiply(new BigDecimal(mul)).doubleValue(), precision);

        return doubleValue;
    }

    public static String toString( double value, int countCharAfterComma) {

        double d = truncate(value, countCharAfterComma);
        if (countCharAfterComma<=0) {
            return ""+((long)d);
        }
        String str = ""+d;

        int ptr = str.indexOf('.');

        return StringTools.appendString(str, '0', str.length()+(countCharAfterComma - (str.length()-ptr-1)), false);
    }

}
