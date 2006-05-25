package org.riverock.webmill.container.core;

public class GetMainUserMetadataItem
{
/*
    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger("org.riverock.webmill.container.core.GetMainUserMetadataItem" );

    private static org.riverock.generic.main.CacheFactoryWithDb cache = new org.riverock.generic.main.CacheFactoryWithDb( GetMainUserMetadataItem.class.getName() );

    public static void reinit()
    {
        cache.reinit();
    }

    public MainUserMetadataItemType item = null;

    public boolean isFound = false;

    public GetMainUserMetadataItem(){}

    public static GetMainUserMetadataItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, long id__)  throws javax.portlet.PortletException     {
        return getInstance(db__, new Long(id__) );
    }

     public static GetMainUserMetadataItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws javax.portlet.PortletException      {
        try
        {
         return (GetMainUserMetadataItem) cache.getInstanceNew(db__, id__ );
        }
        catch(Exception exc)
        {
            throw new javax.portlet.PortletException( exc.getMessage(), exc );
        }
     }

    public MainUserMetadataItemType getData(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
         GetMainUserMetadataItem obj = GetMainUserMetadataItem.getInstance(db_, id);
        if (obj!=null)
            return obj.item;

        return new MainUserMetadataItemType();
    }

     public void copyItem(MainUserMetadataItemType target)
     {
         copyItem(this.item, target);
     }

     public static void copyItem(MainUserMetadataItemType source, MainUserMetadataItemType target)
     {
         if (source==null || target==null)
             return;

         target.setIdMainUserMetadata( source.getIdMainUserMetadata() );
         target.setIdUser( source.getIdUser() );
         target.setIdSite( source.getIdSite() );
         target.setMeta( source.getMeta() );
         target.setStringValue( source.getStringValue() );
         target.setIntValue( source.getIntValue() );
         target.setDateValue( source.getDateValue() );
     }

    public GetMainUserMetadataItem(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
        this(db_, new Long(id));
    }

    private static String sql_ = null;
    static
    {
         sql_ =
                     "select * from MAIN_USER_METADATA where ID_MAIN_USER_METADATA=?";



         try
         {
             SqlStatement.registerSql( sql_, GetMainUserMetadataItem.class );
         }
         catch(Exception e)
         {
             cat.error("Exception in registerSql, sql\n"+sql_, e);
         }
    }

    public GetMainUserMetadataItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws javax.portlet.PortletException     {
        this(db_, id, sql_);
    }

    public GetMainUserMetadataItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws javax.portlet.PortletException     {

        if (id==null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id.longValue());

            rs = ps.executeQuery();
            if (rs.next())
            {
                item = fillBean(rs);
                isFound = true;
            }

        }
        catch (Exception e)
        {
            cat.error("Exception create object", e);
            throw new javax.portlet.PortletException( e.getMessage(), e );
        }
        catch (Error err)
        {
            cat.error("Error create object", err);
            throw err;
        }
        finally
        {
            _closeRsPs(rs, ps);
            rs = null;
            ps = null;
        }

    }

    public static MainUserMetadataItemType fillBean(ResultSet rs)
        throws java.sql.SQLException
    {
        MainUserMetadataItemType item = new MainUserMetadataItemType();

                 long tempLong;
                 String tempString = null;
                 java.sql.Timestamp tempTimestamp = null;
                 tempLong = rs.getLong( "ID_MAIN_USER_METADATA");
                 if (!rs.wasNull())
                     item.setIdMainUserMetadata( new Long(tempLong) );
                 tempLong = rs.getLong( "ID_USER");
                 if (!rs.wasNull())
                     item.setIdUser( new Long(tempLong) );
                 tempLong = rs.getLong( "ID_SITE");
                 if (!rs.wasNull())
                     item.setIdSite( new Long(tempLong) );
                 tempString = rs.getString( "META" );
                 if (!rs.wasNull())
                     item.setMeta( tempString );
                 tempString = rs.getString( "STRING_VALUE" );
                 if (!rs.wasNull())
                     item.setStringValue( tempString );
                 tempLong = rs.getLong( "INT_VALUE");
                 if (!rs.wasNull())
                     item.setIntValue( new Long(tempLong) );
                 tempTimestamp = rs.getTimestamp( "DATE_VALUE" );
                 if (!rs.wasNull())
                     item.setDateValue( tempTimestamp );
        return item;
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

*/
}
