/*
 * org.riverock.portlet -- Portlet Library
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

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.portlet.test.cases;

import junit.framework.TestSuite;
import org.apache.log4j.Logger;

public class SuiteMember
{
    private static Logger cat = Logger.getLogger("org.riverock.portlet.test.MillTest" );

    public SuiteMember(){}

    public static TestSuite suite ( )
    {

		TestSuite suite= new TestSuite("Member tests");

        suite.addTest( new TestCaseMember("testRestrict") );
        suite.addTest( new TestCaseMember("testRestrictFields") );
        suite.addTest( new TestCaseMember("testPrimaryKeyInsertIsShow") );
        suite.addTest( new TestCaseMember("testPrimaryKeyIsEdit") );
        suite.addTest( new TestCaseMember("testLookup") );
        suite.addTest( new TestCaseMember("testMemberFields") );
        suite.addTest( new TestCaseMember("testMemberTypeFields") );
        suite.addTest( new TestCaseMember("testPrimaryKeyFieldName") );
        suite.addTest( new TestCaseMember("testMemberTemplateQuery") );
        suite.addTest( new TestCaseMember("testMemberRelateClassExists") );

        return suite;

	}


}