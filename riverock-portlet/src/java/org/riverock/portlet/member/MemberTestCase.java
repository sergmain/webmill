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

 * User: Admin

 * Date: Dec 14, 2002

 * Time: 2:22:59 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import junit.framework.TestCase;

import org.riverock.portlet.schema.member.*;



import java.util.Enumeration;



public class MemberTestCase extends TestCase

{

    public MemberTestCase(String testName)

    {

        super(testName);

    }



    // имена первичных ключей должны совпадать с именами в QueryArea если они там указаны

    public void testPrimaryKeyFieldName()

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();

        ModuleManager.init();



        MemberFile[] member = ModuleManager.getMemberFileArray();



        MemberArea memberAreaFull = new MemberArea();



        System.out.println("make member module list");

        for (int k=0; k < member.length; k++)

        {

            for (Enumeration e = member[k].getMemberModules(); e.hasMoreElements();)

            {

                ModuleType mod = (ModuleType)e.nextElement();

                memberAreaFull.addModule( mod );

            }

        }



        System.out.println("Make relate class list");

        for( int j=0; j<memberAreaFull.getModuleCount(); j++)

        {

            ModuleType mod = memberAreaFull.getModule(j);

            for ( int i=0; i<mod.getContentCount();i++)

            {

                ContentType content = mod.getContent(i);

                QueryAreaType qa = content.getQueryArea();



                String pk = qa.getPrimaryKey();

                assertFalse("\nPrimary key in module '"+mod.getName()+"', content '" +

                    content.getAction().toString()+

                    "' not specified", (pk==null || pk.length()==0));



                for (int k=0; k<qa.getFieldsCount(); k++)

                {

                    FieldsType field = qa.getFields(k);

                    if (pk.equalsIgnoreCase( field.getName()))

                    {

                        assertFalse("\nPrimary key in module '"+mod.getName()+

                            "', content '" + content.getAction().toString()+

                            "' not equal to name of field in fields list",

                            (!pk.equals( field.getName()))

                        );

                    }

                }

            }

        }

    }

}