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



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.math.BigDecimal;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.common.tools.RsetTools;





/**

 *

 *  $Id$

 *

 */



public class Items

{

    public Long id_item;

    public int count;

    public String item;

    public BigDecimal price;

    public String currency;



    protected void finalize() throws Throwable

    {

        item = null;

        currency = null;



        super.finalize();

    }



    public Items(Long i, int c)

    {

        id_item = i;

        count = c;

    }



    public void initItem(DatabaseAdapter ora_)

            throws PriceException

    {

        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            String sql_ = "select * from PRICE_LIST where ID_ITEM = ?";



            ps = ora_.prepareStatement(sql_);



            RsetTools.setLong(ps, 1, id_item);



            rs = ps.executeQuery();

            if (rs.next())

            {

                item = RsetTools.getString(rs, "ITEM");

                price = RsetTools.getBigDecimal(rs, "PRICE");

                currency = RsetTools.getString(rs, "CURRENCY");

            }

        }

        catch (Exception e)

        {

            throw new PriceException(e.toString());

        }

        finally

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

}