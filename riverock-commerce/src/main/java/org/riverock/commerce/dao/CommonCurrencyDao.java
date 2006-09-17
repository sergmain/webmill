/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.commerce.dao;

import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:29:37
 *         <p/>
 *         $Id$
 */
public interface CommonCurrencyDao {
    CurrencyCurrentCurs getCurrentCurs(Long currencyId, Long siteId);

    CurrencyCurrentCurs getCurrentCurs(DatabaseAdapter db, Long currencyId, Long siteId);

    CurrencyCurrentCurs getStandardCurrencyCurs(Long currencyId);

    CurrencyCurrentCurs getStandardCurrencyCurs( DatabaseAdapter db, Long standardCurrencyId );
}