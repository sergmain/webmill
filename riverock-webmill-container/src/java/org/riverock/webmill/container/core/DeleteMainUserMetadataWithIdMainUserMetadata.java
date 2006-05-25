package org.riverock.webmill.container.core;


public class DeleteMainUserMetadataWithIdMainUserMetadata {
/*
    private static Logger log = Logger.getLogger(DeleteMainUserMetadataWithIdMainUserMetadata.class );

    public DeleteMainUserMetadataWithIdMainUserMetadata(){}

    public static long process(DatabaseAdapter db_, Long id)  throws javax.portlet.PortletException     {

        if (id==null) 
             throw new IllegalArgumentException("Value of id can not be null");

        String sql_ =
            "delete from MAIN_USER_METADATA "+
            "where ID_MAIN_USER_METADATA=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

            ps.setLong(1, id );

            int countDeletedtRecord = ps.executeUpdate();

            if (log.isDebugEnabled())
                 log.debug("Count of deleted records - "+countDeletedtRecord);

            return countDeletedtRecord;

        }
        catch (Exception e) {
             log.error("Error delete from db", e);
            throw new javax.portlet.PortletException( e.getMessage(), e );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
*/
}
