package org.riverock.webmill.container.core;

import org.riverock.webmill.container.schema.core.MainUserMetadataListType;
import org.riverock.sql.cache.SqlStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetMainUserMetadataWithIdUserList
{
    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger("org.riverock.webmill.container.core.GetMainUserMetadataWithIdUserList" );

     public MainUserMetadataListType item = new MainUserMetadataListType();

     public GetMainUserMetadataWithIdUserList(){}

     public boolean isFound = false;

    private static org.riverock.generic.main.CacheFactoryWithDb cache = new org.riverock.generic.main.CacheFactoryWithDb( GetMainUserMetadataWithIdUserList.class.getName() );

    public static void reinit()
    {
        cache.reinit();
    }

    public static GetMainUserMetadataWithIdUserList getInstance(org.riverock.generic.db.DatabaseAdapter db__, long id__)  throws javax.portlet.PortletException     {
        return getInstance(db__, new Long(id__) );
    }

     public static GetMainUserMetadataWithIdUserList getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws javax.portlet.PortletException      {
        try
        {
         return (GetMainUserMetadataWithIdUserList) cache.getInstanceNew(db__, id__ );
        }
        catch(Exception exc)
        {
            throw new javax.portlet.PortletException( exc.getMessage(), exc );
        }
     }

    /**
     * @deprecated use getInstance() method
     */
    public MainUserMetadataListType getData(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
        GetMainUserMetadataWithIdUserList obj = GetMainUserMetadataWithIdUserList.getInstance(db_, id);
        if (obj!=null)
            return obj.item;

        return new MainUserMetadataListType();
    }

    public GetMainUserMetadataWithIdUserList(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws javax.portlet.PortletException     {
        this(db_, new Long(id));
    }


    private static String sql_ = null;
    static
    {
         sql_ =
                     "select * "+
        "from MAIN_USER_METADATA "+
        "where ID_USER=? "+
        "order by ID_MAIN_USER_METADATA ASC";


         try
         {
             SqlStatement.registerRelateClass( GetMainUserMetadataWithIdUserList.class, GetMainUserMetadataItem.class);

         }
         catch(Exception e)
         {
             cat.error("Exception in registerRelateClass, sql\n"+sql_, e);
         }

         try
         {
             SqlStatement.registerSql( sql_, GetMainUserMetadataWithIdUserList.class );
         }
         catch(Exception e)
         {
             cat.error("Exception in registerSql, sql\n"+sql_, e);
         }
    }

    public GetMainUserMetadataWithIdUserList(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws javax.portlet.PortletException     {
        this(db_, id, sql_);
    }

    public GetMainUserMetadataWithIdUserList(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws javax.portlet.PortletException     {

        if (id==null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id.longValue());

            rs = ps.executeQuery();
            while (rs.next())
            {
                GetMainUserMetadataItem tempItem = GetMainUserMetadataItem.getInstance(db_, rs.getLong("ID_MAIN_USER_METADATA"));
                if (tempItem!=null && tempItem.item!=null)
                {
                    if (item==null)
                        item = new MainUserMetadataListType();

                    this.isFound = true;
                    item.addMainUserMetadata( tempItem.item );
                }
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
