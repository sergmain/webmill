/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.price;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.riverock.common.tools.RsetTools;

/**
 *
 *  $Id$
 *
 */

public class PriceListItemOrder extends PriceListItem
{
    public int qty;

    public PriceListItemOrder(ResultSet rs__)
            throws PriceException
    {
        super.set(rs__);
        try
        {
            qty = RsetTools.getInt(rs__, "qty", new Integer(0)).intValue();
        }
        catch (SQLException sqlException)
        {
            throw new PriceException(sqlException.getMessage());
        }
    }

}
