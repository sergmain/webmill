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

import org.riverock.commerce.price.CurrencyPrecisionList;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:46:49
 *         <p/>
 *         $Id$
 */
public class ShopBean {

    public Long id_shop = null;
    public int is_close;
    public String name_shop;
    public String footer;
    public String header;
    public String code_shop = "";
    public String name_shop_for_price_list = "";
    public Calendar dateUpload;
    public Calendar dateCalcQuantity;
    public int newItemDays;

    // Валюта по умолчанию для отображения прайса
    public Long currencyID = null;
    // Валюта по умолчанию для отображения заказа
    public Long idOrderCurrency = null;

    public int is_default_currency = 1;

    public boolean isNeedProcessing = false;  // Нужен ли интерфейс для проведения финансовых транзакций
    public boolean isProcessInvoice = false; //  Нужен ли интерфейс для выписки счетов
    public boolean isNeedRecalc = false;//  Нужен ли интерфейс для пересчета из одной валюты в другую

    public CurrencyPrecisionList precisionList = new CurrencyPrecisionList();

    public double discount = 0;

    public Long id_type_shop_1 = null;
    public Long id_type_shop_2 = null;

    public Long getId_shop() {
        return id_shop;
    }

    public void setId_shop(Long id_shop) {
        this.id_shop = id_shop;
    }

    public int getIs_close() {
        return is_close;
    }

    public void setIs_close(int is_close) {
        this.is_close = is_close;
    }

    public String getName_shop() {
        return name_shop;
    }

    public void setName_shop(String name_shop) {
        this.name_shop = name_shop;
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

    public String getCode_shop() {
        return code_shop;
    }

    public void setCode_shop(String code_shop) {
        this.code_shop = code_shop;
    }

    public String getName_shop_for_price_list() {
        return name_shop_for_price_list;
    }

    public void setName_shop_for_price_list(String name_shop_for_price_list) {
        this.name_shop_for_price_list = name_shop_for_price_list;
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

    public Long getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Long currencyID) {
        this.currencyID = currencyID;
    }

    public Long getIdOrderCurrency() {
        return idOrderCurrency;
    }

    public void setIdOrderCurrency(Long idOrderCurrency) {
        this.idOrderCurrency = idOrderCurrency;
    }

    public int getIs_default_currency() {
        return is_default_currency;
    }

    public void setIs_default_currency(int is_default_currency) {
        this.is_default_currency = is_default_currency;
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
