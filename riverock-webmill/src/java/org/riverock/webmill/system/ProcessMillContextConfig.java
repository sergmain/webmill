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
 * Date: Jan 16, 2003
 * Time: 10:29:18 AM
 *
 * $Id$
 */

package org.riverock.webmill.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.riverock.webmill.schema.tomcat4.ContextType;
import org.riverock.webmill.schema.tomcat4.ContextTypeItem;
import org.riverock.webmill.schema.tomcat4.EnvironmentType;
import org.riverock.webmill.schema.tomcat4.types.EnvironmentTypeTypeType;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public class ProcessMillContextConfig
{
    private static Logger cat = Logger.getLogger("org.riverock.webmill.system.ProcessMillContextConfig");

    public ProcessMillContextConfig()
    {
    }

    private static EnvironmentType getEnv(String name, String value, String type, boolean override, boolean naming)
    {
        EnvironmentType env = new EnvironmentType();
        env.setName( name ); //mill/LogPath
        env.setValue( value );
        env.setType( EnvironmentTypeTypeType.valueOf( type ) );
        env.setOverride( new Boolean(override) );
        env.setNaming( new Boolean(naming) );
        return env;
    }

    public static void main(String args[])
        throws Exception
    {
        String encoding = "UTF-8";

        if (args.length <5)
            throw new Exception("Not enough arguments");

        String inFile = args[0];
        String outFile = args[1];
        String logPath = args[2];
        String logConfigFile = args[3];
        String configFile = args[4];

        InputSource inCurrSrc = new InputSource( new FileInputStream( inFile ));
        ContextType obj = (ContextType)Unmarshaller.unmarshal(ContextType.class, inCurrSrc);

        obj.setContextTypeItem( new ArrayList(0) );
        ArrayList v = new ArrayList(3);

        //Todo После того как когфиг файлы быди разделены
        //Todo необходимо изменить создание контекста
        if (true) throw new Exception("Not implemented in version 4.x.x ");
//        v.add( getEnv( Constants.JNDI_MILL_LOG_PATH, "/"+logPath, "java.lang.String", true, true) );
//        v.add( getEnv( Constants.JNDI_MILL_LOG_CONFIG_FILE, "/"+logConfigFile, "java.lang.String", true, true) );
//        v.add( getEnv( Constants.JNDI_MILL_CONFIG_FILE, "/"+configFile, "java.lang.String", true, true) );

        ContextTypeItem item = new ContextTypeItem();
        item.setEnvironment(v);
        ArrayList ctx = new ArrayList();
        ctx.add (item);
        obj.setContextTypeItem( ctx );
        obj.setDocBase("../../../mill");

        FileOutputStream fos = new FileOutputStream( outFile );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setRootElement( "Context" );
        marsh.setMarshalAsDocument( true );
        marsh.setEncoding( encoding );
        marsh.marshal( obj );
    }
}
