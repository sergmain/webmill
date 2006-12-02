package org.riverock.commerce.bean;

import java.io.Serializable;

import org.riverock.commerce.bean.price.CurrencyCurrentCursType;

/**
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 14:53:35
 * <p/>
 * $Id$
 */
public class StandardCurrency implements Serializable {

  private Long standardCurrencyId;

  private String currencyCode;

  private String currencyName;

  private CurrencyCurrentCursType currentCurs;

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public CurrencyCurrentCursType getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(CurrencyCurrentCursType currentCurs) {
        this.currentCurs = currentCurs;
    }
}
