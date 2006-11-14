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
package org.riverock.commerce.bean;

import java.util.Calendar;
import java.io.Serializable;

import org.riverock.commerce.price.CurrencyPrecisionList;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:46:49
 *         <p/>
 *         $Id$
 */
public class ShopBean implements Serializable {
    private static final long serialVersionUID = 2625005780L;

    private Long shopId = null;
    private Long siteId = null;
    private boolean isOpened;
    private String shopName;
    private String footer;
    private String header;
    private String shopCode = "";
    private String shopNameForPriceList = "";
    private Calendar dateUpload;
    private Calendar dateCalcQuantity;
    private int newItemDays;

    // Валюта по умолчанию для отображения прайса
    private Long defaultCurrencyId = null;
    // Валюта по умолчанию для отображения заказа
    private Long invoiceCurrencyId = null;

    private boolean isDefaultCurrency = true;

    private boolean isNeedProcessing = false;  // Нужен ли интерфейс для проведения финансовых транзакций
    private boolean isProcessInvoice = false; //  Нужен ли интерфейс для выписки счетов
    private boolean isNeedRecalc = false; //  Нужен ли интерфейс для пересчета из одной валюты в другую

    private CurrencyPrecisionList precisionList = new CurrencyPrecisionList();

    private double discount = 0;

    private Long id_type_shop_1 = null;
    private Long id_type_shop_2 = null;
    private int digitsAfterComma;

    public int getDigitsAfterComma() {
        return digitsAfterComma;
    }

    public void setDigitsAfterComma(int digitsAfterComma) {
        this.digitsAfterComma = digitsAfterComma;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long id_shop) {
        this.shopId = id_shop;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getShopNameForPriceList() {
        return shopNameForPriceList;
    }

    public void setShopNameForPriceList(String shopNameForPriceList) {
        this.shopNameForPriceList = shopNameForPriceList;
    }

    public Calendar getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Calendar dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Calendar getDateCalcQuantity() {
        return dateCalcQuantity;
    }

    public void setDateCalcQuantity(Calendar dateCalcQuantity) {
        this.dateCalcQuantity = dateCalcQuantity;
    }

    public int getNewItemDays() {
        return newItemDays;
    }

    public void setNewItemDays(int newItemDays) {
        this.newItemDays = newItemDays;
    }

    public Long getDefaultCurrencyId() {
        return defaultCurrencyId;
    }

    public void setDefaultCurrencyId(Long defaultCurrencyId) {
        this.defaultCurrencyId = defaultCurrencyId;
    }

    public Long getInvoiceCurrencyId() {
        return invoiceCurrencyId;
    }

    public void setInvoiceCurrencyId(Long invoiceCurrencyId) {
        this.invoiceCurrencyId = invoiceCurrencyId;
    }

    public boolean isDefaultCurrency() {
        return isDefaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.isDefaultCurrency = defaultCurrency;
    }

    public boolean isNeedProcessing() {
        return isNeedProcessing;
    }

    public void setNeedProcessing(boolean needProcessing) {
        isNeedProcessing = needProcessing;
    }

    public boolean isProcessInvoice() {
        return isProcessInvoice;
    }

    public void setProcessInvoice(boolean processInvoice) {
        isProcessInvoice = processInvoice;
    }

    public boolean isNeedRecalc() {
        return isNeedRecalc;
    }

    public void setNeedRecalc(boolean needRecalc) {
        isNeedRecalc = needRecalc;
    }

    public CurrencyPrecisionList getPrecisionList() {
        return precisionList;
    }

    public void setPrecisionList(CurrencyPrecisionList precisionList) {
        this.precisionList = precisionList;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Long getId_type_shop_1() {
        return id_type_shop_1;
    }

    public void setId_type_shop_1(Long id_type_shop_1) {
        this.id_type_shop_1 = id_type_shop_1;
    }

    public Long getId_type_shop_2() {
        return id_type_shop_2;
    }

    public void setId_type_shop_2(Long id_type_shop_2) {
        this.id_type_shop_2 = id_type_shop_2;
    }
}
