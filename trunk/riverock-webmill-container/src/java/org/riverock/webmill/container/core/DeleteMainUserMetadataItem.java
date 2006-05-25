//Todo add copyright
package org.riverock.webmill.container.core;

public class DeleteMainUserMetadataItem {
/*
    private static Logger log = Logger.getLogger( DeleteMainUserMetadataItem.class );

     public DeleteMainUserMetadataItem(){}

     public static long process(DatabaseAdapter db_, MainUserMetadataItemType item)  throws javax.portlet.PortletException      {

         String sql_ =
             "delete from WM_LIST_USER_METADATA "+
             "where ID_MAIN_USER_METADATA=?";

         PreparedStatement ps = null;
         ResultSet rs = null;
         try
         {
             ps = db_.prepareStatement(sql_);

             ps.setObject(1, item.getIdMainUserMetadata() );

             int countInsertRecord = ps.executeUpdate();

             if (log.isDebugEnabled())
                 log.debug("Count of deleted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             log.error("Item getIdMainUserMetadata(), value - "+item.getIdMainUserMetadata());
             log.error("Item getIdUser(), value - "+item.getIdUser());
             log.error("Item getIdSite(), value - "+item.getIdSite());
             log.error("Item getMeta(), value - "+item.getMeta());
             log.error("Item getStringValue(), value - "+item.getStringValue());
             log.error("Item getIntValue(), value - "+item.getIntValue());
             log.error("Item getDateValue(), value - "+item.getDateValue());
             log.error("SQL "+sql_);
             log.error("Exception insert data in db", e);
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
