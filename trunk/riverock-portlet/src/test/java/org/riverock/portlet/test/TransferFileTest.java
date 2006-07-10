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