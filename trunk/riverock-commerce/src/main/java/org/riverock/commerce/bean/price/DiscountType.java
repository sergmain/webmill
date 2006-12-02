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
package org.riverock.commerce.bean.price;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DiscountType.
 * 
 * @version $Revision$ $Date$
 */
public class DiscountType implements Serializable {

    /**
     * Field discountItemList
     */
    private List<DiscountItemType> discountItemList = new ArrayList<DiscountItemType>();

    /**
     * Field resultDiscount
     */
    private Double resultDiscount;

    /**
     * Field resultDiscountName
     */
    private String resultDiscountName;

    public List<DiscountItemType> getDiscountItemList() {
        if (discountItemList==null) {
            discountItemList = new ArrayList<DiscountItemType>();
        }
        return discountItemList;
    }

    public void setDiscountItemList(List<DiscountItemType> discountItemList) {
        this.discountItemList = discountItemList;
    }

    public Double getResultDiscount() {
        return resultDiscount;
    }

    public void setResultDiscount(Double resultDiscount) {
        this.resultDiscount = resultDiscount;
    }

    public String getResultDiscountName() {
        return resultDiscountName;
    }

    public void setResultDiscountName(String resultDiscountName) {
        this.resultDiscountName = resultDiscountName;
    }
}
