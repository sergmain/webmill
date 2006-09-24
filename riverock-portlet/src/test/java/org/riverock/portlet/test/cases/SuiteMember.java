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