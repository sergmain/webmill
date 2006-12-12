package org.riverock.commerce.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 14:53:35
 * <p/>
 * $Id$
 */
@Entity
@Table(name = "wm_cash_currency_std")
@TableGenerator(
    name = "TABLE_CASH_CURRENCY_STD",
    table = "wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_cash_currency_std",
    allocationSize = 1,
    initialValue = 1
)
public class StandardCurrency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_CASH_CURRENCY_STD")
    @Column(name = "ID_STD_CURR")
    private Long standardCurrencyId;

    @Column(name = "NAME_STD_CURR")
    private String standardCurrencyName;

    @Column(name = "CONVERT_CURRENCY")
    private String standardCurrencyCode;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Transient
    private StandardCurrencyCurs currentCurs;

    @Transient
    private List<StandardCurrencyCurs> curses = new ArrayList<StandardCurrencyCurs>();

    public List<StandardCurrencyCurs> getCurses() {
        return curses;
    }

    public void setCurses(List<StandardCurrencyCurs> curses) {
        this.curses = curses;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public String getStandardCurrencyCode() {
        return standardCurrencyCode;
    }

    public void setStandardCurrencyCode(String standardCurrencyCode) {
        this.standardCurrencyCode = standardCurrencyCode;
    }

    public String getStandardCurrencyName() {
        return standardCurrencyName;
    }

    public void setStandardCurrencyName(String standardCurrencyName) {
        this.standardCurrencyName = standardCurrencyName;
    }

    public StandardCurrencyCurs getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(StandardCurrencyCurs currentCurs) {
        this.currentCurs = currentCurs;
    }
}
