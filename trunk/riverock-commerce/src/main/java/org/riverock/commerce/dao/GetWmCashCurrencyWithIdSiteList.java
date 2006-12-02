package org.riverock.commerce.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.commerce.bean.price.WmCashCurrencyListType;
import org.riverock.sql.cache.SqlStatement;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmCashCurrencyWithIdSiteList implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmCashCurrencyWithIdSiteList.class);

     public WmCashCurrencyListType item = new WmCashCurrencyListType();

     public GetWmCashCurrencyWithIdSiteList(){}

     public boolean isFound = false;

    public static void reinit()
    {
    }

    public static GetWmCashCurrencyWithIdSiteList getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws Exception     {
        return new GetWmCashCurrencyWithIdSiteList(db__, id__ );
    }

    public GetWmCashCurrencyWithIdSiteList(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws Exception     {
        this(db_, new Long(id));
    }


    private static String sql_ = null;
    static {
         sql_ =
                     "select ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE "+
        "from WM_CASH_CURRENCY "+
        "where ID_SITE=? "+
        "order by ID_CURRENCY ASC";


         try {
             SqlStatement.registerRelateClass( GetWmCashCurrencyWithIdSiteList.class, GetWmCashCurrencyItem.class);

         }
         catch(Exception e) {
             log.error("Exception in registerRelateClass, sql\n"+sql_, e);
         }

         try {
             SqlStatement.registerSql( sql_, GetWmCashCurrencyWithIdSiteList.class );
         }
         catch(Exception e) {
             log.error("Exception in registerSql, sql\n"+sql_, e);
         }
    }

    public GetWmCashCurrencyWithIdSiteList(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws Exception     {
        this(db_, id, sql_);
    }

    public GetWmCashCurrencyWithIdSiteList(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws Exception     {

        if (id==null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                GetWmCashCurrencyItem tempItem = GetWmCashCurrencyItem.getInstance(db_, rs.getLong("ID_CURRENCY"));
                if (tempItem!=null && tempItem.item!=null) {
                    if (item==null)
                        item = new WmCashCurrencyListType();

                    this.isFound = true;
                    item.getWmCashCurrencyList().add( tempItem.item );
                }
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
