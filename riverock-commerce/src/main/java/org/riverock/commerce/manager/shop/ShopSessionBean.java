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
package org.riverock.commerce.manager.shop;

import java.io.Serializable;

import org.riverock.commerce.bean.Shop;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 17:04:07
 *         <p/>
 *         $Id$
 */
public class ShopSessionBean implements Serializable {
    private static final long serialVersionUID = 3817005504L;

    private ShopExtendedBean shopExtendedBean = null;
    private Long currentShopId = null;
    private Shop shop = null;

    public ShopSessionBean() {
    }

    public ShopExtendedBean getShopExtendedBean() {
        return shopExtendedBean;
    }

    public void setShopExtendedBean(ShopExtendedBean shopExtendedBean) {
        this.shopExtendedBean = shopExtendedBean;
    }

    public Long getCurrentShopId() {
        return currentShopId;
    }

    public void setCurrentShopId(Long currentShopId) {
        this.currentShopId = currentShopId;
    }

    public Shop getShopBean() {
        return shop;
    }

    public void setShopBean(Shop shop) {
        this.shop = shop;
    }
}

