package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 20:53:12
 *         $Id$
 */
public class CompanyBean implements Serializable {
    private static final long serialVersionUID = 2043005504L;

    private String companyName = null;
    private Long companyId = null;
    private List<AuthUserExtendedInfoImpl> userBeans = new ArrayList<AuthUserExtendedInfoImpl>();

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Long companyId ) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public List<AuthUserExtendedInfoImpl> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans( List<AuthUserExtendedInfoImpl> userBeans ) {
        this.userBeans = userBeans;
    }

    public void addUserBeans( AuthUserExtendedInfoImpl userBean ) {
        userBeans.add( userBean );
    }
}
