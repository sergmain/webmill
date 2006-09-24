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
package org.riverock.portlet.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.TransferFile;


public class TransferFileTest {
    private static Logger log = Logger.getLogger( TransferFileTest.class );

    public void procee( HttpServletRequest request ) throws Exception {

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();
//            AuthSession auth_ = AuthSession.check( request, response );
//            if( auth_ == null )
//                return;

//            if( auth_.getRight( "SYSADMIN", "UPLOAD_FILE_X509", "A" ) ) {

                try {
                    TransferFile.processData( request );
                }
                catch( Exception e ) {
                    log.error( "Error upload signed file.", e );
                    throw e;
                }
//            }

        }
        finally {
            DatabaseManager.close( dbDyn );
            dbDyn = null;
        }
    }
}