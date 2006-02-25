package org.riverock.interfaces.sso.a3;

/**
 * @author SergeMaslyukov
 *         Date: 24.01.2006
 *         Time: 12:57:00
 *         $Id$
 */
public interface AuthInfo {
    public Long getAuthUserId();
    public Long getUserId();
    public Long getCompanyId();
    public Long getHoldingId();

    public String getUserLogin();
    public String getUserPassword();

    public boolean isCompany();
    public boolean isHolding();
    public boolean isRoot();
}