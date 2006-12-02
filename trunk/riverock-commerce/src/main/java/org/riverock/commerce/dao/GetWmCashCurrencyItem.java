package org.riverock.commerce.dao;

import org.riverock.commerce.bean.price.WmCashCurrencyItemType;
import org.riverock.sql.cache.SqlStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmCashCurrencyItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmCashCurrencyItem.class);

    public static void reinit() {
    }

    public WmCashCurrencyItemType item = null;

    public boolean isFound = false;

    public GetWmCashCurrencyItem(){}

    public static GetWmCashCurrencyItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws Exception     {
        return new GetWmCashCurrencyItem(db__, id__ );
    }

    public void copyItem(WmCashCurrencyItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmCashCurrencyItemType source, WmCashCurrencyItemType target)
    {
        if (source==null || target==null)
            return;

        target.setCurrencyId( source.getCurrencyId() );
        target.setCurrency( source.getCurrency() );
        target.setUsed( source.isUsed() );
        target.setCurrencyName( source.getCurrencyName() );
        target.setUseStandart( source.isUseStandart() );
        target.setIdStandartCurs( source.getIdStandartCurs() );
        target.setSiteId( source.getSiteId() );
        target.setPercentValue( source.getPercentValue() );
    }

    public GetWmCashCurrencyItem(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws Exception     {
        this(db_, new Long(id));
    }

    private static String sql_ = null;
    static {
         sql_ =
                     "select ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE from WM_CASH_CURRENCY where ID_CURRENCY=?";



         try {
             SqlStatement.registerSql( sql_, GetWmCashCurrencyItem.class );
         }
         catch(Exception e) {
             log.error("Exception in registerSql, sql\n"+sql_, e);
         }
    }

    public GetWmCashCurrencyItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws Exception     {
        this(db_, id, sql_);
    }

    public GetWmCashCurrencyItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws Exception     {

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
            _closeRsPs(rs, ps);
            rs = null;
            ps = null;
        }

    }

    public static WmCashCurrencyItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmCashCurrencyItemType item = new WmCashCurrencyItemType();

                 long tempLong;
                 String tempString;
                 int tempBoolean;
                 double tempDouble;
                 tempLong = rs.getLong( "ID_CURRENCY");
                 item.setCurrencyId( tempLong );
                 tempString = rs.getString( "CURRENCY" );
                 if (!rs.wasNull())
                     item.setCurrency( tempString );
                 tempBoolean = rs.getInt( "IS_USED");
                 if (!rs.wasNull())
                     item.setUsed( tempBoolean==1 );
                 else
                     item.setUsed( false );
                 tempString = rs.getString( "NAME_CURRENCY" );
                 if (!rs.wasNull())
                     item.setCurrencyName( tempString );
                 tempBoolean = rs.getInt( "IS_USE_STANDART");
                 if (!rs.wasNull())
                     item.setUseStandart( tempBoolean==1 );
                 else
                     item.setUseStandart( false );
                 tempLong = rs.getLong( "ID_STANDART_CURS");
                 if (!rs.wasNull())
                     item.setIdStandartCurs( tempLong );
                 tempLong = rs.getLong( "ID_SITE");
                 if (!rs.wasNull())
                     item.setSiteId( tempLong );
                 tempDouble = rs.getDouble( "PERCENT_VALUE");
                 if (!rs.wasNull())
                     item.setPercentValue( tempDouble );
        return item;
    }

    private static void _closeRsPs(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            }
            catch (Exception e01){
                // catch close error
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            }
            catch (Exception e02) {
                // catch close error
            }
        }
    }

}
