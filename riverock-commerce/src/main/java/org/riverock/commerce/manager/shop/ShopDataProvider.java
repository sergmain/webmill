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
package org.riverock.commerce.manager.shop;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 17:03:55
 *         <p/>
 *         $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class ShopDataProvider implements Serializable {
    private static final long serialVersionUID = 3815005509L;

    public List<ShopBean> getShopList() {
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        List<ShopBean> list = CommerceDaoFactory.getShopDao().getShopList(siteId);
        if (list==null) {
            return new ArrayList<ShopBean>();
        }
        return list;
    }

}