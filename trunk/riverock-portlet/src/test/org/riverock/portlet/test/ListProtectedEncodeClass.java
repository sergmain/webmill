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
 * Date: Feb 8, 2003
 * Time: 12:55:44 PM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import org.riverock.portlet.member.MemberFile;
import org.riverock.portlet.member.ModuleManager;
import org.riverock.portlet.schema.member.MemberArea;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.schema.member.RelateClassListType;
import org.riverock.portlet.schema.member.RelateClassType;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.config.WebmillConfig;

import java.util.Enumeration;

public class ListProtectedEncodeClass
{
    public static void main(String s[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        System.out.println("start");
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

        System.out.println("Marshal member module list to file");
        XmlTools.writeToFile(
            memberAreaFull,
            WebmillConfig.getWebmillDebugDir() + "mill-member-area.xml",
            "utf-8",
            "MemberArea"
        );

        System.out.println("Make relate class list");
        RelateClassListType cl = new RelateClassListType();
        for (int k=0; k < member.length; k++)
        {
            for (Enumeration e = member[k].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();

                if ( mod.getRelateClassCount()>0)
                {
                    for ( int i=0; i<mod.getRelateClassCount();i++)
                    {
                        RelateClassType cla = mod.getRelateClass(i);
                        boolean flag = false;
                        for (int l=0; l<cl.getRelateClassCount(); l++)
                        {
                            if (cla.getClassName().equals( cl.getRelateClass(l).getClassName()))
                            {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag)
                            cl.addRelateClass( cla );
                    }
                }
            }
        }

        System.out.println("Marshal relate class list to file");
        XmlTools.writeToFile(
            cl,
            WebmillConfig.getWebmillDebugDir() + "mill-member-relate-class-list.xml",
            "utf-8",
            "RelateClassList"
        );

        throw new Exception("need rewrite");
/*
        PortletManager.init();
        PortletFile[] portlet = PortletManager.getPortletFileArray();
        PortletClassGetListType portletClassList = new PortletClassGetListType();

        for (int p=0; p<portlet.length; p++)
        {
            for (Enumeration e = portlet[p].getPortletDescriptions(); e.hasMoreElements();)
            {
                PortletDescriptionType desc = (PortletDescriptionType)e.nextElement();
                if (desc.getClassNameGetList() != null && desc.getClassNameGetList().length()>0)
                    portletClassList.addClassName( desc.getClassNameGetList() );
            }
        }

        System.out.print("\n\n");

        for (int k=0; k< cl.getRelateClassCount(); k++)
        {
            System.out.println(".class "+
                StringTools.replaceString(cl.getRelateClass(k).getClassName(), ".", "/")+
                " public"
            );
        }

        System.out.println("");

        for (int k=0; k< portletClassList.getClassNameCount(); k++)
        {
            System.out.println(".class "+
                StringTools.replaceString(portletClassList.getClassName(k), ".", "/")+
                " public"
            );
        }
*/
    }
}
