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
 * Author: mill
 * Date: Apr 21, 2003
 * Time: 4:02:24 PM
 *
 * $Id$
 */

package org.riverock.webmill.as.service;

import org.riverock.webmill.as.ApplicationInterface;
import org.riverock.webmill.as.server.ApplicationTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.core.GetSiteListSiteItem;
import org.riverock.webmill.schema.appl_server.ResourceRequestParameterType;
import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.webmill.schema.core.SiteListSiteItemType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.sso.a3.AuthSession;

import org.apache.log4j.Logger;

public class DoRegister implements ApplicationInterface
{
    private static Logger cat = Logger.getLogger( DoRegister.class );

    public DoRegister(){}

    public byte[] processRequest( ResourceRequestType applReq, AuthSession authSession )
        throws Exception
    {
        if (applReq==null || applReq.getParametersCount()==0)
            return null;

        String userLogin = null;
        String userPassword = null;
        for (int i=0; i<applReq.getParametersCount(); i++)
        {
            ResourceRequestParameterType param = applReq.getParameters(i);
            if ("mill.user-login".equals( param.getNameParameter()))
            {
                String stringParam = ApplicationTools.decodeParameter( param );

                if (cat.isDebugEnabled())
                    cat.debug("Parameter is "+stringParam);

                userLogin =  stringParam;
                continue;
            }
            if ("mill.user-password".equals( param.getNameParameter()))
            {
                String stringParam = ApplicationTools.decodeParameter( param );

                if (cat.isDebugEnabled())
                    cat.debug("Parameter is "+stringParam);

                userPassword = stringParam;
                continue;
            }
        }

        if (true) throw  new Exception("need rewrite");
        return XmlTools.getXml( new Object(), null );

//        StringResultType result = new StringResultType();
//        if (authSession!=null)
//            result.setResult( authSession.getSessionId() );
//
//        return XmlTools.getXml( result, null );
    }

    public static void main( String args[] )
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        long id = 1;
        DatabaseAdapter adapter = DatabaseAdapter.getInstance();
        SiteListSiteItemType resultItem = GetSiteListSiteItem.getInstance( adapter, id ).item;

        String[][] ns = new String[][]
        {
            {"webmill-core", "http://webmill.jsmithy.com/xsd/jsmithy-webmill-core.xsd"}
        };

        XmlTools.writeToFile(
            resultItem,
            WebmillConfig.getWebmillDebugDir() + "test-site_list_site-item.xml",
            "utf-8",
            null,
            ns
        );
        DatabaseAdapter.close(adapter);
    }
}