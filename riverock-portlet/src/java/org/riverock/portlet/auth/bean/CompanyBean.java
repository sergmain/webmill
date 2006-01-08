package org.riverock.portlet.auth.bean;

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
    private List<UserBean> userBeans = new ArrayList<UserBean>();

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

    public List<UserBean> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans( List<UserBean> userBeans ) {
        this.userBeans = userBeans;
    }

    public void addUserBeans( UserBean userBean ) {
        userBeans.add( userBean );
    }
}
