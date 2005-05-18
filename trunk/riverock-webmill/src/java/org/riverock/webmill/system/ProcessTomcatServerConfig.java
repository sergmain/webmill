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

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public class ProcessTomcatServerConfig
{
    private static Logger cat = Logger.getLogger("org.riverock.webmill.system.ProcessTomcatServerConfig");

    public ProcessTomcatServerConfig()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        String encoding = "UTF-8";
/*
        if (args.length <3)
            throw new Exception("Not enough arguments");

        String inFile = args[0];
        String outFile = args[1];
        String appsDir = args[2];

        InputSource inCurrSrc = new InputSource( new FileInputStream( inFile ));
        ServerType obj = (ServerType)Unmarshaller.unmarshal(ServerType.class, inCurrSrc);
        obj.setGlobalNamingResources( null );

        EngineType engine = obj.getService(0).getEngine();

        engine.setDefaultHost("localhost");
        engine.setDebug( new Integer(0) );
        engine.setLogger( null );
        engine.setRealm( null );
        engine.setHost( new ArrayList(0) );

        engine.addHost( createHost("localhost") );
        engine.addHost( createHost("me.askmore") );
        engine.addHost( createHost("petstore") );

        FileOutputStream fos = new FileOutputStream( outFile );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setRootElement( "Server" );
        marsh.setMarshalAsDocument( true );
        marsh.setEncoding( encoding );
        marsh.marshal( obj );
*/
    }
/*
    private static HostType createHost( String nameHost )
    {
        HostType host = new HostType();
        host.setDebug( new Integer(0) );
        host.setName( nameHost );
        host.setAppBase(  "webapps\\"+nameHost );

        ContextType ctx = new ContextType();
        ctx.setPath("");
        ctx.setDocBase("data");
        ctx.setDebug(new Integer(0));
        ctx.setReloadable(Boolean.TRUE);
        ctx.setUseNaming(Boolean.FALSE);
        ctx.setCrossContext(Boolean.FALSE);

        HostTypeSequence ctxSeq = new HostTypeSequence();
        ctxSeq.setContext( ctx );
        host.setHostTypeSequence( ctxSeq );

        return host;
    }
*/    
}
