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



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Marshaller;

import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



import org.riverock.generic.schema.config.DatabaseConnectionType;



public class ProcessMillConfig

{

    private static Logger cat = Logger.getLogger("org.riverock.webmill.system.ProcessMillConfig");



    public ProcessMillConfig()

    {

    }



    public static void main(String args[])

        throws Exception

    {

        String encoding = "UTF-8";

        String hypersonic = "HSQLDB";



        if (args.length <3)

            throw new Exception("Not enough arguments");



        String inFile = args[0];

        String outFile = args[1];

        String tempPath = args[2];



        //Todo После того как когфиг файлы быди разделены

        //Todo необходимо изменить создание контекста

        if (true) throw new Exception("Not implemented in version 4.x.x ");

/*

        InputSource inCurrSrc = new InputSource( new FileInputStream( inFile ));

        WebmillConfig config = (WebmillConfig)Unmarshaller.unmarshal(WebmillConfig.class, inCurrSrc);



        config.removeAllDatabaseConnection();

        DatabaseConnectionType conn = new DatabaseConnectionType();

        conn.setConnectionClass( org.riverock.db.HSQLconnect.class.getName() );

        conn.setConnectString( "jdbc:hsqldb:hsql://localhost" );

        conn.setDatabaseCharset( "UTF8" );

        conn.setIsAutoCommit( false );

        conn.setIsConvertDatabaseString( false );

        conn.setName( hypersonic );

        conn.setPassword( "" );

        conn.setUsername( "sa" );

        config.addDatabaseConnection( conn );

        config.setDefaultConnectionName( hypersonic );



        config.getMillEngineKey().setPath("/");

        config.getMillEngineKey().setPassword("123");



        config.setMillTempDir( "/"+tempPath );



        FileOutputStream fos = new FileOutputStream( outFile );

        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));

        marsh.setMarshalAsDocument( true );

        marsh.setEncoding( encoding );

        marsh.marshal( config );

*/

    }

}

