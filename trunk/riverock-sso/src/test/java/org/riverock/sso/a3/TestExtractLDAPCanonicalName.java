/*
 * org.riverock.sso - Single Sign On implementation
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
package org.riverock.sso.a3;

import junit.framework.TestCase;

/**
 * User: Admin
 * Date: Sep 9, 2003
 * Time: 8:39:10 PM
 *
 * $Id$
 */
public class TestExtractLDAPCanonicalName extends TestCase
{

    public TestExtractLDAPCanonicalName(){}

    public TestExtractLDAPCanonicalName(String testName)
    {
        super(testName);
    }

    static class ItemCN
    {
        String cn = null;
        String result = null;

        public ItemCN( String cn_, String v_)
        {
            this.cn = cn_;
            this.result = v_;
        }
    }

    private void testExctractCanonicalName(ItemCN tl)
        throws Exception
    {
        System.out.println("CN - "+tl.cn);
        String r = LDAPAuthProvider.parseCanonicalName(tl.cn);
        if (r==null)
        {
            assertFalse(
                "\nError parser CN  '"+tl.cn+"'\n" +
                "Expected null, but result is '"+tl.result+"'",
                tl.result!=null
                );
            return;
        }

        assertTrue(
            "\nError parser CN  '"+tl.cn+"'\n" +
            "Expected '"+tl.result+"', but result is '"+r+"'",
            r.equals( tl.result )
        );
    }

    public void testExctractCanonicalName()
        throws Exception
    {
        ItemCN[] items =
            {
                new ItemCN("CN=webmill.root,CN=Users,DC=askmore", "webmill.root"),
                new ItemCN("CN=TestAdmin,CN=Users,DC=askmore", "TestAdmin"),
                new ItemCN("CN=Proba 1,CN=Users,DC=askmore", "Proba 1"),
                new ItemCN("CN=Proba 1", "Proba 1"),
                new ItemCN("Proba 1", null),
                new ItemCN("Proba 1,CN=Users,DC=askmore", null)

            };

        for (final ItemCN newVar : items) {
            testExctractCanonicalName(newVar);
        }
    }
}
