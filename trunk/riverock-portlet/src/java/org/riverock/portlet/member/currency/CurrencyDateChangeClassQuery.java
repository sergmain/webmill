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
package org.riverock.portlet.member.currency;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.utils.DateUtils;
import org.riverock.portlet.member.BaseClassQuery;
import org.riverock.portlet.member.MemberQueryParameter;
import org.riverock.portlet.price.CurrencyManager;
import org.riverock.portlet.price.CurrencyService;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: Jan 5, 2003
 * Time: 2:50:24 PM
 *
 * $Id$
 */
public class CurrencyDateChangeClassQuery extends BaseClassQuery
{
    private static Logger log = Logger.getLogger( CurrencyDateChangeClassQuery.class );

    // ID_CURRENCY
    private Long idCurrency = null;

    public void setIdCurrency(Long param)
    {
        idCurrency = param;

        if (log.isDebugEnabled())
            log.debug("idCurrency - "+idCurrency);
    }

    /**
     * ¬озвращает текущее значение дл€ отображени€ на веб-странице
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();

            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            CustomCurrencyItemType item = CurrencyService.getCurrencyItem(
                CurrencyManager.getInstance(db_, siteId ).getCurrencyList() , idCurrency
            );

            if (item==null || item.getCurrentCurs()==null)
                return "";

            return ""+DateUtils.getStringDate(
                    item.getCurrentCurs().getDateChange(), "dd.MM.yyyy HH:mm:ss", Locale.ENGLISH
            );
        }
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }

    /**
     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента
     * @return Vector of org.riverock.member.ClassQueryItem
     */
    public List getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception
    {
        throw new Exception("Not implemented");
    }

    MemberQueryParameter param = null;

    public void setQueryParameter(MemberQueryParameter parameter) throws Exception
    {
        this.param = parameter;
    }
}
