package org.riverock.commerce.dao;

import org.riverock.commerce.bean.price.WmPriceListItemType;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmPriceListItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmPriceListItem.class);

    public static void reinit() {
    }

    public WmPriceListItemType item = null;

    public boolean isFound = false;

    public GetWmPriceListItem(){}

    public static GetWmPriceListItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws Exception     {
        return new GetWmPriceListItem(db__, id__ );
    }

    public void copyItem(WmPriceListItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmPriceListItemType source, WmPriceListItemType target)
    {
        if (source==null || target==null)
            return;

        target.setItemId( source.getItemId() );
        target.setShopId( source.getShopId() );
        target.setGroup( source.isGroup() );
        target.setId( source.getId() );
        target.setIdMain( source.getIdMain() );
        target.setItem( source.getItem() );
        target.setAbsolete( source.getAbsolete() );
        target.setCurrency( source.getCurrency() );
        target.setQuantity( source.getQuantity() );
        target.setAddDate( source.getAddDate() );
        target.setSpecial( source.isSpecial() );
        target.setManual( source.isManual() );
        target.setIdStorageStatus( source.getIdStorageStatus() );
        target.setPrice( source.getPrice() );
    }

    public GetWmPriceListItem(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws Exception     {
        this(db_, new Long(id));
    }

    private static String sql_ = null;
    static {
         sql_ =
             "select ID_ITEM, ID_SHOP, IS_GROUP, ID, ID_MAIN, ITEM, ABSOLETE, CURRENCY, QUANTITY, ADD_DATE, IS_SPECIAL, " +
             "       IS_MANUAL, ID_STORAGE_STATUS, PRICE " +
             "from   WM_PRICE_LIST where ID_ITEM=?";



         try {
             SqlStatement.registerSql( sql_, GetWmPriceListItem.class );
         }
         catch(Exception e) {
             log.error("Exception in registerSql, sql\n"+sql_, e);
         }
    }

    public GetWmPriceListItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws Exception     {
        this(db_, id, sql_);
    }

    public GetWmPriceListItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws Exception     {

        if (id==null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                item = fillBean(rs);
                isFound = true;
            }

        }
        catch (Exception e) {
            log.error("Exception create object", e);
            throw e;
        }
        catch (Error err) {
            log.error("Error create object", err);
            throw err;
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

    }

    public static WmPriceListItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmPriceListItemType item = new WmPriceListItemType();

        long tempLong;
        int tempBoolean;
        String tempString;
        int tempInt;
        java.sql.Timestamp tempTimestamp = null;

        tempLong = rs.getLong( "ID_ITEM");
        item.setItemId( tempLong );
        tempLong = rs.getLong( "ID_SHOP");
        if (!rs.wasNull())
            item.setShopId( tempLong );
        tempBoolean = rs.getInt( "IS_GROUP");
        if (!rs.wasNull())
            item.setGroup( tempBoolean==1 );
        else
            item.setGroup( false );
        tempLong = rs.getLong( "ID");
        if (!rs.wasNull())
            item.setId( tempLong );
        tempLong = rs.getLong( "ID_MAIN");
        item.setIdMain( tempLong );
        tempString = rs.getString( "ITEM" );
        if (!rs.wasNull())
            item.setItem( tempString );
        tempInt = rs.getInt( "ABSOLETE");
        item.setAbsolete( tempInt );
        tempString = rs.getString( "CURRENCY" );
        if (!rs.wasNull())
            item.setCurrency( tempString );
        tempLong = rs.getLong( "QUANTITY");
        if (!rs.wasNull())
            item.setQuantity( tempLong );
        tempTimestamp = rs.getTimestamp( "ADD_DATE" );
        if (!rs.wasNull())
            item.setAddDate( tempTimestamp );
        tempBoolean = rs.getInt( "IS_SPECIAL");
        if (!rs.wasNull())
            item.setSpecial( tempBoolean==1 );
        else
            item.setSpecial( false );
        tempBoolean = rs.getInt( "IS_MANUAL");
        if (!rs.wasNull())
            item.setManual( tempBoolean==1 );
        else
            item.setManual( false );
        tempLong = rs.getLong( "ID_STORAGE_STATUS");
        item.setIdStorageStatus( tempLong );

        item.setPrice( RsetTools.getBigDecimal(rs, "PRICE") );

        return item;
    }
}
