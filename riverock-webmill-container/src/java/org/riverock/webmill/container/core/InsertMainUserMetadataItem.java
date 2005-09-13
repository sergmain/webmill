package org.riverock.webmill.container.core;

import org.riverock.webmill.container.schema.core.MainUserMetadataItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

public class InsertMainUserMetadataItem 
{
    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger("org.riverock.webmill.container.core.InsertMainUserMetadataItem" );

    public InsertMainUserMetadataItem(){}

    public static Long processData(org.riverock.generic.db.DatabaseAdapter db_, MainUserMetadataItemType item)  throws javax.portlet.PortletException     {
        return new Long(process(db_, item));
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, MainUserMetadataItemType item)  throws javax.portlet.PortletException     {
        String sql_ =
            "insert into MAIN_USER_METADATA"+
             "(ID_MAIN_USER_METADATA, ID_USER, ID_SITE, META, STRING_VALUE, "+
             "INT_VALUE, DATE_VALUE)"+
            "values"+
            "( ?,  ?,  ?,  ?,  ?,  ?,  "+ db_.getNameDateBind()+")";

        return process(db_, item, sql_);
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, MainUserMetadataItemType item, String sql_)  throws javax.portlet.PortletException     {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

             ps.setObject(1, item.getIdMainUserMetadata() );
             ps.setObject(2, item.getIdUser() );
             ps.setObject(3, item.getIdSite() );
             ps.setString(4, item.getMeta() );
             if (item.getStringValue()!=null)
                 ps.setString(5, item.getStringValue() );
             else
                 ps.setNull(5, Types.VARCHAR);

             if (item.getIntValue() != null )
                 ps.setObject(6, item.getIntValue() );
             else
                 ps.setNull(6, Types.INTEGER);

             if (item.getDateValue()!=null)
                 db_.bindDate(ps, 7, new java.sql.Timestamp( item.getDateValue().getTime()) );
             else
                 ps.setNull(7, Types.DATE);


             int countInsertRecord = ps.executeUpdate();

             if (cat.isDebugEnabled())
                 cat.debug("Count of inserted records - "+countInsertRecord);

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
