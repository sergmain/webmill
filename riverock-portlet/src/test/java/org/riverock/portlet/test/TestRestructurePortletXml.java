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
 * User: Admin
 * Date: Sep 16, 2003
 * Time: 10:40:14 PM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import java.util.List;

public class TestRestructurePortletXml
{

    static void addInitParam(List v, String name_, String value_)
    {
        if (value_==null)
            return;

/*
        InitParamType init = new InitParamType();
        NameType name = new NameType();
        name.setContent( name_ );
        init.setName( name );
        ValueType value = new ValueType();
        value.setContent( value_ );
        init.setValue( value );

        v.add( init );
*/
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
