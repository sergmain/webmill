/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.member.currency;

import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.member.ClassQueryItem;
import org.riverock.portlet.member.BaseClassQuery;
import org.riverock.portlet.member.MemberQueryParameter;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: Jan 5, 2003
 * Time: 2:50:24 PM
 *
 * $Id$
 */
public class CurrencyStdCursClassQuery extends BaseClassQuery {
    private static Logger log = Logger.getLogger( CurrencyStdCursClassQuery.class );

    // ID_CURRENCY
    private Long idCurrency = null;

    public void setIdCurrency( Long param ) {
        idCurrency = param;

        if( log.isDebugEnabled() )
            log.debug( "idCurrency - " + idCurrency );
    }

    /**
     * ¬озвращает текущее значение дл€ отображени€ на веб-странице
     *
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle ) throws Exception {
/*
        Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        final CurrencyManager currencyManager = CurrencyManager.getInstance( siteId );
        CustomCurrencyItemType item = CurrencyService.getCurrencyItem(
            currencyManager.getCurrencyList(), idCurrency );
        StandardCurrencyItemType stdItem = CurrencyService.getStandardCurrencyItem(
            currencyManager.getCurrencyList(), item.getIdStandardCurrency() );

        if( stdItem == null || stdItem.getCurrentCurs() == null )
            return "";

        return "" + stdItem.getCurrentCurs().getCurs();
*/
        return null;
    }

    /**
     * ¬озвращает список возможных значений дл€ построени€ <select> элемента
     *
     * @return Vector of org.riverock.member.ClassQueryItem
     */
    public List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception {
        throw new Exception( "Not implemented" );
    }

    MemberQueryParameter param = null;

    public void setQueryParameter( MemberQueryParameter parameter ) throws Exception {
        this.param = parameter;
    }
}
