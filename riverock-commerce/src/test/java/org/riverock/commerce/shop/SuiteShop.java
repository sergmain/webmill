/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.commerce.shop;

import junit.framework.TestSuite;

/**
 *
 * $Author: serg_main $
 *
 * $Id: SuiteShop.java 1048 2006-11-14 15:28:48Z serg_main $
 *
 */
public class SuiteShop
{
    public SuiteShop(){}

    public static TestSuite suite ( )
    {

		TestSuite suite= new TestSuite("News tests");

        suite.addTest( new TestCaseShop("testWithOracleConnection") );
//        suite.addTest( new TestCaseShop("testWithHypersonicConnection") );
//        suite.addTest( new TestCaseShop("testWithMSSQLConnection") );
//        suite.addTest( new TestCaseShop("testWithMySqlConnection") );


//        suite.addTest( new TestCaseNews("testWithIbmDB2Connection") );

        return suite;

	}


}