package org.riverock.portlet.manager.users;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.main.AuthSessionBean;

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

        Iterator<Company> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            Company companyBean = iterator.next();

            list.add( new SelectItem( companyBean.getId(), companyBean.getName() ) );
        }
        return list;
    }

	public List<PortalUserBean> getPortalUserList() {
		List<PortalUserBean> list = PortalUserDaoFactory.getPortalUserDao().getPortlalUserList( authSessionBean.getAuthSession() );
		if (list==null) {
			return null;
		}
		
		Iterator<PortalUserBean> iterator = list.iterator();
		List<PortalUserBean> portalUsers = new ArrayList<PortalUserBean>();
		while(iterator.hasNext()) {
			PortalUserBean portalUser = iterator.next();
			portalUsers.add( new PortalUserBeanImpl(portalUser) );
		}
		return portalUsers;
	}
}
