package org.riverock.sso.bean;

import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 15:49:45
 *         $Id$
 */
public class AuthParameterBeanImpl implements AuthParameterBean {
    private String name = null;
    private String value = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
