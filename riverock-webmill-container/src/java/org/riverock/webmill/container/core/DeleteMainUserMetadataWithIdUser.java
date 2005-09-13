package org.riverock.webmill.container.core;

import org.riverock.webmill.container.schema.core.MainUserMetadataItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteMainUserMetadataWithIdUser 
{
    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger("org.riverock.webmill.container.core.DeleteMainUserMetadataWithIdUser" );

    public DeleteMainUserMetadataWithIdUser(){}

    public static Long processData(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
        return new Long(process(db_, new Long(id)));
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
        return process(db_, new Long(id));
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws javax.portlet.PortletException     {

        if (id==null) 
             throw new IllegalArgumentException("Value of id can not be null");

        String sql_ =
            "delete from MAIN_USER_METADATA "+
            "where ID_USER=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

            ps.setLong(1, id.longValue() );

            int countInsertRecord = ps.executeUpdate();

            if (cat.isDebugEnabled())
                 cat.debug("Count of deleted records - "+countInsertRecord);

            return countInsertRecord;

        }
        catch (Exception e)
        {
            cat.error("Error delete from db", e);
            throw new javax.portlet.PortletException( e.getMessage(), e );
         }
         finally
         {
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
