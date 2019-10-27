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
package org.riverock.commerce.manager.currency;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.bean.Currency;
import org.riverock.interfaces.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:16:48
 *         <p/>
 *         $Id$
 */
public class CurrencyDataProvider implements Serializable {
    private static final long serialVersionUID = 5595005509L;

    public List<Currency> getCurrencyList() {
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        List<Currency> list = CommerceDaoFactory.getCurrencyDao().getCurrencyList(siteId);
        if (list==null) {
            return new ArrayList<Currency>();
        }
        return list;
    }

}
