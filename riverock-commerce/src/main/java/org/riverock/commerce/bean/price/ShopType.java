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

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Class ShopType.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class ShopType implements java.io.Serializable {

    /**
     * Field shopId
     */
    private java.lang.Long shopId;

    /**
     * Field siteId
     */
    private java.lang.Long siteId;

    /**
     * Field isClose
     */
    private java.lang.Boolean isClose;

    /**
     * Field isProcessInvoice
     */
    private java.lang.Boolean isProcessInvoice;

    /**
     * Field isNeedRecalc
     */
    private java.lang.Boolean isNeedRecalc;

    /**
     * �?спользовать ли валюту по
     * умолчанию
     */
    private java.lang.Boolean isDefaultCurrency;

    /**
     * Field isNeedProcessing
     */
    private java.lang.Boolean isNeedProcessing;

    /**
     * Field precision
     */
    private java.lang.Integer precision;

    /**
     * Количество дней в течение
     * которых позиция считается новой
     */
    private java.lang.Integer newItemDays;

    /**
     * Field codeShop
     */
    private java.lang.String codeShop;

    /**
     * Дата загрузки данных об остатках
     * на складе
     */
    private java.util.Date dateUploadQuantity;

    /**
     * Field dateUploadPrice
     */
    private java.util.Date dateUploadPrice;

    /**
     * Название магазина
     */
    private java.lang.String nameShop;

    /**
     * Field header
     */
    private java.lang.String header;

    /**
     * Field footer
     */
    private java.lang.String footer;

    /**
     * Field nameShopForPriceList
     */
    private java.lang.String nameShopForPriceList;

    /**
     * Field idTypeShop1
     */
    private java.lang.Long idTypeShop1;

    /**
     * Field idTypeShop2
     */
    private java.lang.Long idTypeShop2;

    /**
     * Field idDefaultCurrency
     */
    private java.lang.Long idDefaultCurrency;

    /**
     * Field orderCurrencyId
     */
    private java.lang.Long orderCurrencyId;

    /**
     * Общая скидка для магазина
     */
    private DiscountItemType discount;

    /**
     * Точность округления для каждой
     * из валют
     */
    private CurrencyPrecisionListType precisionList;

    /**
     * Field itemsList
     */
    private List<ShopItemType> itemsList = new ArrayList<ShopItemType>();

    /**
     * Field groupsList
     */
    private List<ShopGroupType> groupsList = new ArrayList<ShopGroupType>();

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Boolean getClose() {
        return isClose;
    }

    public void setClose(Boolean close) {
        isClose = close;
    }

    public Boolean getProcessInvoice() {
        return isProcessInvoice;
    }

    public void setProcessInvoice(Boolean processInvoice) {
        isProcessInvoice = processInvoice;
    }

    public Boolean getNeedRecalc() {
        return isNeedRecalc;
    }

    public void setNeedRecalc(Boolean needRecalc) {
        isNeedRecalc = needRecalc;
    }

    public Boolean getDefaultCurrency() {
        return isDefaultCurrency;
    }

    public void setDefaultCurrency(Boolean defaultCurrency) {
        isDefaultCurrency = defaultCurrency;
    }

    public Boolean getNeedProcessing() {
        return isNeedProcessing;
    }

    public void setNeedProcessing(Boolean needProcessing) {
        isNeedProcessing = needProcessing;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Integer getNewItemDays() {
        return newItemDays;
    }

    public void setNewItemDays(Integer newItemDays) {
        this.newItemDays = newItemDays;
    }

    public String getCodeShop() {
        return codeShop;
    }

    public void setCodeShop(String codeShop) {
        this.codeShop = codeShop;
    }

    public Date getDateUploadQuantity() {
        return dateUploadQuantity;
    }

    public void setDateUploadQuantity(Date dateUploadQuantity) {
        this.dateUploadQuantity = dateUploadQuantity;
    }

    public Date getDateUploadPrice() {
        return dateUploadPrice;
    }

    public void setDateUploadPrice(Date dateUploadPrice) {
        this.dateUploadPrice = dateUploadPrice;
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getNameShopForPriceList() {
        return nameShopForPriceList;
    }

    public void setNameShopForPriceList(String nameShopForPriceList) {
        this.nameShopForPriceList = nameShopForPriceList;
    }

    public Long getIdTypeShop1() {
        return idTypeShop1;
    }

    public void setIdTypeShop1(Long idTypeShop1) {
        this.idTypeShop1 = idTypeShop1;
    }

    public Long getIdTypeShop2() {
        return idTypeShop2;
    }

    public void setIdTypeShop2(Long idTypeShop2) {
        this.idTypeShop2 = idTypeShop2;
    }

    public Long getIdDefaultCurrency() {
        return idDefaultCurrency;
    }

    public void setIdDefaultCurrency(Long idDefaultCurrency) {
        this.idDefaultCurrency = idDefaultCurrency;
    }

    public Long getOrderCurrencyId() {
        return orderCurrencyId;
    }

    public void setOrderCurrencyId(Long orderCurrencyId) {
        this.orderCurrencyId = orderCurrencyId;
    }

    public DiscountItemType getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountItemType discount) {
        this.discount = discount;
    }

    public CurrencyPrecisionListType getPrecisionList() {
        return precisionList;
    }

    public void setPrecisionList(CurrencyPrecisionListType precisionList) {
        this.precisionList = precisionList;
    }

    public List<ShopItemType> getItemsList() {
        if (itemsList==null) {
            itemsList = new ArrayList<ShopItemType>();
        }
        return itemsList;
    }

    public void setItemsList(List<ShopItemType> itemsList) {
        this.itemsList = itemsList;
    }

    public List<ShopGroupType> getGroupsList() {
        if (groupsList==null) {
            groupsList = new ArrayList<ShopGroupType>();
        }
        return groupsList;
    }

    public void setGroupsList(List<ShopGroupType> groupsList) {
        this.groupsList = groupsList;
    }
}
