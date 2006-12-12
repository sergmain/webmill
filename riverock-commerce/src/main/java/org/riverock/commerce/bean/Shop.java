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

import java.util.Date;
import java.io.Serializable;

import javax.persistence.TableGenerator;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.riverock.commerce.price.CurrencyPrecisionList;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:46:49
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name="wm_price_shop_list")
@TableGenerator(
    name="TABLE_PRICE_SHOP_LIST",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_shop_list",
    allocationSize = 1,
    initialValue = 1
)
public class Shop implements Serializable {
    private static final long serialVersionUID = 2625005780L;

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_SHOP_LIST")
    @Column(name="ID_SHOP")
    private Long shopId = null;

    @Column(name="ID_SITE")
    private Long siteId = null;

    @Column(name="IS_CLOSE")
    private boolean isClosed;

    @Column(name="NAME_SHOP")
    private String shopName;

    @Column(name="FOOTER_STRING")
    private String footer;

    @Column(name="HEADER_STRING")
    private String header;

    @Column(name="CODE_SHOP")
    private String shopCode = "";

    @Column(name="NAME_SHOP_FOR_PRICE_LIST")
    private String shopNameForPriceList = "";

    @Column(name="LAST_DATE_UPLOAD")
    private Date dateUpload;

    @Column(name="DATE_CALC_QUANTITY")
    private Date dateCalcQuantity;

    @Column(name="NEW_ITEM_DAYS")
    private Integer newItemDays;

    /**
     * Defautl currency for shop
     */
    @Column(name="ID_CURRENCY")
    private Long defaultCurrencyId = null;

    /**
     * Default currency for invoice
     */
    @Column(name="ID_ORDER_CURRENCY")
    private Long invoiceCurrencyId = null;

    @Column(name="IS_DEFAULT_CURRENCY")
    private boolean isDefaultCurrency = true;

    /**
     * Нужен ли интерфейс для проведения финансовых транзакций
     */
    @Column(name="IS_NEED_PROCESSING")
    private boolean isNeedProcessing = false;  

    /**
     * Нужен ли интерфейс для выписки счетов
     */
    @Column(name="IS_PROCESS_INVOICE")
    private boolean isProcessInvoice = false;

    /**
     * Нужен ли интерфейс для пересчета из одной валюты в другую
     */
    @Column(name="IS_NEED_RECALC")
    private boolean isNeedRecalc = false;

    @Column(name="DISCOUNT")
    private double discount = 0;

    @Column(name="ID_TYPE_SHOP_1")
    private Long id_type_shop_1 = null;

    @Column(name="ID_TYPE_SHOP_2")
    private Long id_type_shop_2 = null;

    @Column(name="COMMAS_COUNT")
    private int digitsAfterComma;

    @Transient
    private CurrencyPrecisionList precisionList = new CurrencyPrecisionList();

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

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        this.isClosed = closed;
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

    public Date getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(Date dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Date getDateCalcQuantity() {
        return dateCalcQuantity;
    }

    public void setDateCalcQuantity(Date dateCalcQuantity) {
        this.dateCalcQuantity = dateCalcQuantity;
    }

    public Integer getNewItemDays() {
        return newItemDays;
    }

    public void setNewItemDays(Integer newItemDays) {
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
        if( discount < 0 ) {
            this.discount = 0;
        }
        else if( discount >= 100 ) {
            this.discount = 99;
        }
        else {
            this.discount = discount;
        }
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
