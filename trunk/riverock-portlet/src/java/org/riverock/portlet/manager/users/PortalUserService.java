package org.riverock.portlet.manager.users;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserService implements Serializable {
    private static final long serialVersionUID = 2055006515L;

    private AuthSessionBean authSessionBean = null;

    public PortalUserService() {
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();

        for (Company companyBean : companies) {
            list.add(new SelectItem(companyBean.getId(), companyBean.getName()));
        }
        return list;
    }

    public List<User> getPortalUserList() {
        List<User> list = FacesTools.getPortalDaoProvider().getPortalUserDao().getUserList();
        if (list==null) {
            return null;
        }

        Iterator<User> iterator = list.iterator();
        List<User> portalUsers = new ArrayList<User>();
        while(iterator.hasNext()) {
            User portalUser = iterator.next();
            portalUsers.add( new PortalUserBeanImpl(portalUser) );
        }
        return portalUsers;
    }
}
