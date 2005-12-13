package org.riverock.webmill.container.core;

import org.riverock.webmill.container.schema.core.MainUserMetadataItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteMainUserMetadataItem 
{
    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger("org.riverock.webmill.container.core.DeleteMainUserMetadataItem" );

     public DeleteMainUserMetadataItem(){}

     public static Long processData(org.riverock.generic.db.DatabaseAdapter db_, MainUserMetadataItemType item)  throws javax.portlet.PortletException      {
         return new Long(process(db_, item));
     }

     public static long process(org.riverock.generic.db.DatabaseAdapter db_, MainUserMetadataItemType item)  throws javax.portlet.PortletException      {

         String sql_ =
             "delete from MAIN_USER_METADATA "+
             "where ID_MAIN_USER_METADATA=?";

         PreparedStatement ps = null;
         ResultSet rs = null;
         try
         {
             ps = db_.prepareStatement(sql_);

             ps.setObject(1, item.getIdMainUserMetadata() );

             int countInsertRecord = ps.executeUpdate();

             if (cat.isDebugEnabled())
                 cat.debug("Count of deleted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             cat.error("Item getIdMainUserMetadata(), value - "+item.getIdMainUserMetadata());
             cat.error("Item getIdUser(), value - "+item.getIdUser());
             cat.error("Item getIdSite(), value - "+item.getIdSite());
             cat.error("Item getMeta(), value - "+item.getMeta());
             cat.error("Item getStringValue(), value - "+item.getStringValue());
             cat.error("Item getIntValue(), value - "+item.getIntValue());
             cat.error("Item getDateValue(), value - "+item.getDateValue());
             cat.error("SQL "+sql_);
             cat.error("Exception insert data in db", e);
            throw new javax.portlet.PortletException( e.getMessage(), e );
        }
        finally {
            _closeRsPs(rs, ps);
            rs = null;
            ps = null;
        }

    }

    private static void _closeRsPs(ResultSet rs, PreparedStatement ps)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
                rs = null;
            }
            catch (Exception e01)
            {
            }
        }
        if (ps != null)
        {
            try
            {
                ps.close();
                ps = null;
            }
            catch (Exception e02)
            {
            }
        }
    }

}