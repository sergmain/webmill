/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * User: Admin
 * Date: Dec 29, 2002
 * Time: 9:12:53 PM
 *
 * $Id$
 */
package org.riverock.portlet.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;

import org.apache.log4j.Logger;

public class ImportPriceProcess
{
    private static Logger log = Logger.getLogger( ImportPriceProcess.class );

    private static void moveItemToPrice(DatabaseAdapter dbDyn, Long idSite)
        throws Exception
    {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            // крутим цикл по price_import_table
            ps = dbDyn.prepareStatement(
                "select IS_GROUP, ID, ID_MAIN, NAME, PRICE, CURRENCY, b.ID_SHOP "+
                "from PRICE_IMPORT_TABLE a, PRICE_SHOP_TABLE b "+
                "where a.SHOP_CODE=b.CODE_SHOP and b.ID_SITE=?"
            );
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();

            while (rs.next())
            {
                Long id_shop_ = RsetTools.getLong( rs, "ID_SHOP" );

                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                try
                {
                    ps1 = dbDyn.prepareStatement(
                        "select ID, ID_SHOP, ID_ITEM "+
                        "from PRICE_LIST a "+
                        "where ID=? and ID_SHOP=?"
                    );
                    RsetTools.setLong(ps1, 1, RsetTools.getLong(rs, "ID") );
                    RsetTools.setLong(ps1, 2, id_shop_ );
                    rs1 = ps1.executeQuery();

                    if (rs1.next())
                    {
                        PreparedStatement ps2 = null;
                        ResultSet rs2 = null;
                        try
                        {
                            ps2 = dbDyn.prepareStatement(
                                "update PRICE_LIST " +
                                "set IS_GROUP=?, ID_MAIN=?, PRICE=?, ITEM=?, CURRENCY=?, ABSOLETE=0 "+
                                "where ID_ITEM=?"
                            );
                            RsetTools.setLong(ps2, 1, RsetTools.getLong(rs, "IS_GROUP") );
                            RsetTools.setLong(ps2, 2, RsetTools.getLong(rs, "ID_MAIN") );
                            RsetTools.setDouble(ps2, 3, RsetTools.getDouble(rs, "PRICE") );
                            ps2.setString(4, RsetTools.getString(rs, "NAME") );
                            ps2.setString(5, RsetTools.getString(rs, "CURRENCY") );
                            RsetTools.setLong(ps2, 6, RsetTools.getLong(rs1, "ID_ITEM") );

                            ps2.executeUpdate();
                        }
                        catch(Exception e)
                        {
                            log.error("error mark for delete", e);
                            throw e;
                        }
                        finally
                        {
                            DatabaseManager.close(rs2, ps2);
                            rs2 = null;
                            ps2 = null;
                        }
                    }
                    else
                    {
                        PreparedStatement ps2 = null;
                        try
                        {
                            CustomSequenceType seq = new CustomSequenceType();
                            seq.setSequenceName("seq_price_list");
                            seq.setTableName( "PRICE_LIST");
                            seq.setColumnName( "ID_ITEM" );
                            long id = dbDyn.getSequenceNextValue( seq );

                            ps2 = dbDyn.prepareStatement(
                                "insert into PRICE_LIST "+
                                "(id_item,is_group,id,id_main,item,price,currency," +
                                "absolete,id_shop,ADD_DATE)"+
                                "values "+
                                "(?, ?, ?, ?, ?, ?, ?, 0, ?,  "+dbDyn.getNameDateBind()+" )"
                            );
                            ps2.setLong(1, id );
                            RsetTools.setLong(ps2, 2, RsetTools.getLong(rs, "IS_GROUP") );
                            RsetTools.setLong(ps2, 3, RsetTools.getLong(rs, "ID") );
                            RsetTools.setLong(ps2, 4, RsetTools.getLong(rs, "ID_MAIN") );
                            ps2.setString(5, RsetTools.getString(rs, "NAME") );
                            RsetTools.setDouble(ps2, 6, RsetTools.getDouble(rs, "PRICE") );
                            ps2.setString(7, RsetTools.getString(rs, "CURRENCY") );
                            RsetTools.setLong(ps2, 8, RsetTools.getLong(rs, "ID_SHOP") );
                            dbDyn.bindDate(ps2, 9, DateTools.getCurrentTime());
//                            ps2.setTimestamp(9, new Timestamp( System.currentTimeMillis() ) );

                            ps2.executeUpdate();
                        }
                        catch(Exception e)
                        {
                            log.error("error mark for delete", e);
                            throw e;
                        }
                        finally
                        {
                            DatabaseManager.close(ps2);
                            ps2 = null;
                        }
                    }
                }
                catch(Exception e)
                {
                    log.error("error mark for delete", e);
                    throw e;
                }
                finally
                {
                    DatabaseManager.close(rs1, ps1);
                    rs1 = null;
                    ps1 = null;
                }
            }
        }
        catch(Exception e)
        {
            log.error("error mark for delete", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }


    }

    private static void markForDelete(DatabaseAdapter dbDyn, Long idSite)
        throws Exception
    {
        PreparedStatement ps = null;
        String sql_ = null;
        try
        {
            // ”дал€ем дерево ненужных групп
            // сначала помечаем все €вные наименовани€ и группы
            if (dbDyn.getFamaly()!=DatabaseManager.MYSQL_FAMALY)
            {
                sql_ =
                    "update PRICE_IMPORT_TABLE set FOR_DELETE=1 "+
                    "where IS_TO_LOAD='NO' and SHOP_CODE in (" +
                    "select CODE_SHOP from PRICE_SHOP_TABLE where ID_SITE=? )";

                ps = dbDyn.prepareStatement( sql_ );
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            }
            else
            {
                String sqlCheck = "";
                boolean isFound = false;

                PreparedStatement ps2 = null;
                ResultSet rs2 = null;
                boolean isFirst = true;
                try
                {
                    ps2 = dbDyn.prepareStatement(
                        "select CODE_SHOP from PRICE_SHOP_TABLE where ID_SITE=?"
                    );
                    RsetTools.setLong(ps2, 1, idSite);

                    rs2 = ps2.executeQuery();

                    while( rs2.next() ) {
                        isFound = true;
                        if (isFirst)
                            isFirst = false;
                        else
                            sqlCheck += ",";

                        sqlCheck += ("'"+RsetTools.getString(rs2, "CODE_SHOP")+"'");
                    }

                }
                catch( Exception e )
                {
                    log.error("error mark for delete", e);
                    throw e;
                }
                finally
                {
                    DatabaseManager.close(rs2, ps2);
                    rs2 = null;
                    ps2 = null;
                }

                if (isFound)
                {
                    sql_ =
                        "update PRICE_IMPORT_TABLE set FOR_DELETE=1 "+
                        "where IS_TO_LOAD='NO' and SHOP_CODE in ( "+sqlCheck+" )";

                    if (log.isDebugEnabled())
                        log.debug("sql "+sql_);

                    ps = dbDyn.prepareStatement(sql_);
                    ps.executeUpdate();
                    ps.close();
                    ps = null;
                }
            }
        }
        catch(Exception e)
        {
            log.error("sql "+sql_);
            log.error("error mark for delete", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(ps);
            ps = null;
        }

        // «атем выбираем все помеченные дл€ удалени€ элементы
        // и производим удаление подчиненных
        ResultSet rs = null;
        try
        {
            ps = dbDyn.prepareStatement(
                "select a.ID "+
                "from PRICE_IMPORT_TABLE a, PRICE_IMPORT_TABLE b, PRICE_SHOP_TABLE c "+
                "where a.ID_MAIN = b.ID and b.FOR_DELETE=1 and a.FOR_DELETE=0 and "+
                "a.SHOP_CODE=c.CODE_SHOP and c.ID_SITE=?"
            );
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();

            while (rs.next())
            {
                if (dbDyn.getFamaly()!=DatabaseManager.MYSQL_FAMALY)
                {

                    PreparedStatement ps1 = null;
                    try
                    {
                        ps1 = dbDyn.prepareStatement(
                            "update PRICE_IMPORT_TABLE set FOR_DELETE=1 where ID_MAIN in "+
                            "(select ID from PRICE_IMPORT_TABLE z1, PRICE_SHOP_TABLE x1 "+
                            "where z1.FOR_DELETE = 1 and z1.SHOP_CODE = x1.CODE_SHOP and x1.ID_SITE = ?)"+
                            "and FOR_DELETE=0 and SHOP_CODE in "+
                            "(select CODE_SHOP from PRICE_SHOP_TABLE where ID_SITE=?)"
                        );
                        RsetTools.setLong(ps1, 1, idSite );
                        ps1.executeUpdate();
                    }
                    finally
                    {
                        DatabaseManager.close(ps1);
                        ps1 = null;
                    }
                }
                else
                {

                }
            }

        }
        catch(Exception e)
        {
            log.error("error mark for delete", e);
            throw e;
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

    }

    public static void process( DatabaseAdapter dbDyn, Long idSite )
        throws Exception
    {

        try
        {
            dbDyn.createStatement().executeUpdate(
                "update PRICE_IMPORT_TABLE set SHOP_CODE = UPPER(SHOP_CODE)"
            );

        // delete tree of unused groups
        // 1st mark all concrete items and groups
            markForDelete( dbDyn, idSite );
/*
            UPDATE PRICE_IMPORT_TABLE
            SET
                for_delete = 1
            WHERE
                is_to_load = 'NO' and
                shop_code in
                    (   select shop_code
                        from price_shop_table
                        where ID_SITE = siteID
                    );

            // «атем выбираем все помеченные дл€ удалени€ элементы
            // и производим удаление подчиненных
            open imp_data(siteID);
            loop
                fetch imp_data into imp_rec;
                exit when imp_data%NOTFOUND;

                UPDATE price_import_table
                SET
                    for_delete = 1
                where
                    id_main in (
                        select  id
                        from    price_import_table z1, price_shop_table x1
                        where   z1.for_delete = 1 and
                                z1.shop_code = x1.code_shop and
                                x1.ID_SITE = siteID
                    ) and for_delete = 0 and
                    shop_code in
                    (   select shop_code
                        from price_shop_table
                        where ID_SITE = siteID
                    );

            end loop;
*/

            PreparedStatement ps = null;
            try
            {
                // удал€ем все что пометили
                ps = dbDyn.prepareStatement(
                    "delete FROM PRICE_IMPORT_TABLE where FOR_DELETE=1 and "+
                    "SHOP_CODE in (select CODE_SHOP from PRICE_SHOP_TABLE where ID_SITE=?)"
                );
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                log.error("error mark for delete", e);
                throw e;
            }
            finally
            {
                DatabaseManager.close(ps);
                ps = null;
            }
/*
            DELETE FROM price_import_table
            WHERE for_delete = 1 and
                  shop_code in
                  (   select shop_code
                      from price_shop_table
                      where idSite = siteID
                  );
*/

            try
            {
                // ѕомечаем записи, загружаемые первый раз
                ps = dbDyn.prepareStatement(

                    "update PRICE_IMPORT_TABLE set IS_NEW=1 where ID not in "+
                    "(select z1.ID from PRICE_LIST z1, PRICE_SHOP_TABLE x1 "+
                    "where SHOP_CODE = x1.CODE_SHOP and " +
                    "z1.ID_SHOP = x1.ID_SHOP and x1.ID_SITE=?)"

/*
                    "update PRICE_IMPORT_TABLE set IS_NEW=1 where ID not in "+
                    "(select z1.ID from PRICE_LIST z1 " +
                    "where z1.ID_SHOP in " +
                    "(select " +
                    ", PRICE_SHOP_TABLE x1 "+
                    "where a.SHOP_CODE = x1.CODE_SHOP and " +
                    "z1.ID_SHOP = x1.ID_SHOP and x1.ID_SITE=?) and " +

                    "SHOP_CODE in "+
                    "(select x1.CODE_SHOP from PRICE_SHOP_TABLE x1 "+
                    "where x1.CODE_SHOP and x1.ID_SITE=?)"
*/
                );
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                log.error("error mark for delete", e);
                throw e;
            }
            finally
            {
                DatabaseManager.close(ps);
                ps = null;
            }
/*
    update price_import_table a
    set is_new = 1
    where not exists (
        select null
        from    price_list z1, price_shop_table x1
        where   a.id = z1.id and
                a.shop_code = x1.code_shop and
                z1.id_shop = x1.id_shop and
                x1.idSite = siteID
    );

*/

            try
            {
                // во всех прайс-листах, которые закачиваем, устанавливаем атрибут '”старел'
                ps = dbDyn.prepareStatement(
                    "update PRICE_LIST set ABSOLETE=1 where ID_SHOP in "+
                    "(select z1.ID_SHOP from PRICE_SHOP_TABLE z1, PRICE_IMPORT_TABLE x1 "+
                    "where z1.ID_SITE=? and z1.CODE_SHOP=x1.SHOP_CODE)"
                );
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                log.error( "error mark for delete", e );
                throw e;
            }
            finally
            {
                DatabaseManager.close(ps);
                ps = null;
            }
/*
    UPDATE price_list a
    SET    absolete = 1
    where  exists
              ( select  null
                from    price_shop_table z1, price_import_table x1
                where   z1.idSite = siteID and
                        z1.code_shop=x1.shop_code and
                        z1.id_shop=a.id_shop
              );

--    id_shop = shopID;
--    in ( select id_shop from price_import_table );
*/



            moveItemToPrice( dbDyn, idSite );
/*
            open  upd_data( siteID );
            loop
        // крутим цикл по price_import_table
                fetch upd_data into upd_rec;
                exit when upd_data%NOTFOUND;

                id_shop_ := upd_rec.id_shop;

                open  found_data(upd_rec.ID, id_shop_ );
                fetch found_data into found_rec;
                if (found_data%FOUND) then

                    UPDATE price_list
                    SET
                        is_group = upd_rec.is_group,
                        id_main = upd_rec.id_main,
                        price = upd_rec.price,
                        item = upd_rec.name,
                        currency = upd_rec.currency,
                        absolete = 0
                    WHERE
                        rowid = found_rec.row_id;
        --            WHERE
        --                id_item = found_rec.id_item;
                else

                     INSERT INTO price_list
                     (  id_item, is_group, id, id_main, item, price, currency,
                        absolete, id_shop)
                     VALUES
                     (  seq_price_list.nextval, upd_rec.is_group, upd_rec.id, upd_rec.id_main,
                        upd_rec.name, upd_rec.price, upd_rec.currency, 0,
                        upd_rec.id_shop
                     );

                end if;
            end loop;
*/

            try
            {
                ps = dbDyn.prepareStatement(
                    "update PRICE_SHOP_TABLE " +
                    "set LAST_DATE_UPLOAD="+dbDyn.getNameDateBind()+" " +
                    "where ID_SITE=? and CODE_SHOP in " +
                    "(select distinct x1.SHOP_CODE from PRICE_IMPORT_TABLE x1 )"

//                    "update PRICE_LIST set ABSOLETE=1 where ID_SHOP in "+
//                    "(select z1.ID_SHOP from PRICE_SHOP_TABLE z1, PRICE_IMPORT_TABLE x1 "+
//                    "where z1.ID_SITE=? and z1.CODE_SHOP=x1.SHOP_CODE)"
                );
                dbDyn.bindDate(ps, 1, DateTools.getCurrentTime());
                RsetTools.setLong(ps, 2, idSite);
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                log.error("", e);
                throw e;
            }
            finally
            {
                DatabaseManager.close(ps);
                ps = null;
            }


/*
// ѕоле ADD_DATE устанавливаетс€ в момент занесени€ наименовани€
            try
            {
                ps = dbDyn.prepareStatement(
                    "update PRICE_LIST "+
                    "set ADD_DATE=? "+
                    "where ID_ITEM in ( "+
                    "select null "+
                    "from price_import_table z1, price_shop_table x1 "+
                    "where z1.shop_code=x1.code_shop and "+
                    "x1.idSite=? and "+
                    "a.id = z1.id and "+
                    "a.id_shop = x1.id_shop and "+
                    "z1.is_new = 1 "+
                    ")"
                );
                ps.setTimestamp(1, new Timestamp( System.currentTimeMillis() ) );
                RsetTools.setLong(ps, 2, idSite);
                ps.executeUpdate();
            }
            catch(Exception e)
            {
                log.error("", e);
                throw e;
            }
            finally
            {
                RsetTools.closePs(ps);
                ps = null;
            }

            update price_list a
            set add_date = sysdate
            where exists (
                select null
                from price_import_table z1, price_shop_table x1
                where   z1.shop_code=x1.code_shop and
                        x1.idSite = siteID and
                        a.id = z1.id and
                        a.id_shop = x1.id_shop and
                        z1.is_new = 1
            );
*/

        //    delete from price_import_table where id_shop=shopID;
        // «начени€ обновлены

        }
        catch( Exception e ) {
            log.error("Error inport price-list",e);
            throw e;
        }
    }

}
