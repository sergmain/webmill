/*
 * org.riverock.webmill -- Portal framework implementation
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
 * Date: Sep 16, 2003
 * Time: 10:40:14 PM
 *
 * $Id$
 */
package org.riverock.webmill.test;

import java.util.List;

import org.riverock.interfaces.schema.javax.portlet.InitParamType;
import org.riverock.interfaces.schema.javax.portlet.NameType;
import org.riverock.interfaces.schema.javax.portlet.ValueType;

public class TestRestructurePortletXml
{

    static void addInitParam(List v, String name_, String value_)
    {
        if (value_==null)
            return;

        InitParamType init = new InitParamType();
        NameType name = new NameType();
        name.setContent( name_ );
        init.setName( name );
        ValueType value = new ValueType();
        value.setContent( value_ );
        init.setValue( value );

        v.add( init );
    }


    public static void main(String args[])
        throws Exception
    {
        long mills = System.currentTimeMillis();
        org.riverock.generic.startup.StartupApplication.init();

        String tempFile = "c:\\temp\\cdata-test.xml";

/*
        InputSource inCurrSrc = new InputSource( new FileInputStream( "mill\\portlet\\portlet.xml" ));
        PortletListType portlets =
            (PortletListType) Unmarshaller.unmarshal(PortletListType.class, inCurrSrc);

        for (int i=0; i<portlets.getPortletCount(); i++)
        {
            PortletDescriptionType portlet = portlets.getPortlet(i);
            Vector v = new Vector();

            addInitParam(v, "type-portlet", portlet.getTypePortlet().toString());
            addInitParam(v, "is-url", ""+portlet.getIsUrl() );
            addInitParam(v, "name-portlet-id", portlet.getNamePortletID());
            addInitParam(v, "locale-name-package", portlet.getLocaleNamePackage());
            addInitParam(v, "name-portlet-code-string", portlet.getNamePortletCodeString());
            addInitParam(v, "is-get-instance", ""+portlet.getIsGetInstance());
            addInitParam(v, "class-name-get-list", portlet.getClassNameGetList());

            portlet.setInitParam( v );
        }

        XmlTools.writeToFile( portlets, "mill\\portlet\\portlet1.xml", "utf-8", null, false, WebmillNamespace.webmillNamespace );
*/
    }
}
