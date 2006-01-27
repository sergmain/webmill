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

/**
 * User: Admin
 * Date: Sep 9, 2003
 * Time: 8:39:10 PM
 *
 * $Id$
 */
package org.riverock.sso.a3;

import junit.framework.TestCase;

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
