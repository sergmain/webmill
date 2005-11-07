package org.riverock.interfaces.sso.a3;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 15:49:28
 *         $Id$
 */
public interface UserInfo {
    public String getFirstName();
    public String getMiddleName();
    public String getLastName();
    public String getAddress();
    public String getEmail();

    public Double getDiscount();
    public Long getCompanyId();
    public Long getUserId();
}
