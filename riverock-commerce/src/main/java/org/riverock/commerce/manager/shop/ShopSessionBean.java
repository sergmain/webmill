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

import org.riverock.commerce.bean.ShopBean;

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
    private ShopBean shopBean = null;

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

    public ShopBean getShopBean() {
        return shopBean;
    }

    public void setShopBean(ShopBean shopBean) {
        this.shopBean = shopBean;
    }
}

