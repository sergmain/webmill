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



package org.riverock.portlet.price;



import java.sql.SQLException;

import java.util.Vector;



import org.riverock.generic.db.DatabaseAdapter;



public class ImportStorageOrders

{



// DatabaseAdapter must be DYNAMIC connect

    public static void process(DatabaseAdapter db_, String file, long id_shop)

            throws PriceException

    {



        DatabaseAdapter dbDyn = null;

        try

        {

            if (!db_.isDynamic())

                dbDyn = DatabaseAdapter.getInstance(true);

            else

                dbDyn = db_;



            if (id_shop == -1)

                throw new PriceException("Не указан код магазина. Код ошибки #10.02");



            dbDyn.conn.setAutoCommit(false);



            Vector items = ProcessStorageOrders.parseStorageOrdersFile(file);



            if (items == null)

                throw new PriceException("Ошибка разбора файла импорма. Код ошибки #10.03");



            StorageOrder order;

            for (int i = 0; i < items.size(); i++)

            {

                order = (StorageOrder) items.elementAt(i);

                order.process(dbDyn, id_shop);

            }

            dbDyn.commit();

        }

        catch (Exception e)

        {

            try

            {

                dbDyn.rollback();

            }

            catch (Exception e11)

            {

            }

            throw (PriceException)e;

        }

        finally

        {

            if (!db_.isDynamic())

            {

                DatabaseAdapter.close(dbDyn);

                dbDyn = null;

            }

        }

    }



}





