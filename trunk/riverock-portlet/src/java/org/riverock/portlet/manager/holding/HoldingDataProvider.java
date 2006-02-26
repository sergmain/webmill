package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class HoldingDataProvider implements Serializable {
    private static final long serialVersionUID = 2043005511L;

    private AuthSessionBean authSessionBean = null;
    private HoldingSessionBean holdingSessionBean= null;

    public HoldingDataProvider() {
    }

    // getter/setter methods
    public void setHoldingSessionBean( HoldingSessionBean holdingSessionBean) {
        this.holdingSessionBean = holdingSessionBean;
    }

    public HoldingSessionBean getHoldingSessionBean() {
        return holdingSessionBean;
    }

    public HoldingBean getCurrentHolding() {
        return holdingSessionBean.getHoldingBean();
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();

        Iterator<Company> iterator = companies.iterator();
        while( iterator.hasNext() ) {
            Company companyBean = iterator.next();

            if( !isAlreadyBinded( companyBean ) ) {
                list.add( new SelectItem( companyBean.getId(), companyBean.getName() ) );
            }
        }
        return list;
    }

    private boolean isAlreadyBinded( Company companyBean ) {
        Iterator<CompanyBean> iterator = holdingSessionBean.getHoldingBean().getCompanies().iterator();
        while( iterator.hasNext() ) {
            CompanyBean company = iterator.next();
            if( company.getId().equals( companyBean.getId() ) ) {
                return true;
            }
        }
        return false;
    }

    private List<HoldingBean> holdingBeans = null;

    public List<HoldingBean> getHoldingBeans() {
        if( holdingBeans == null ) {
            holdingBeans = initHoldingBeans( authSessionBean.getAuthSession() );
        }
        return holdingBeans;
    }

    public void reinitHoldingBeans() {
        holdingBeans = initHoldingBeans( authSessionBean.getAuthSession() );
    }

    private List<HoldingBean> initHoldingBeans( AuthSession authSession ) {
        List<HoldingBean> list = new ArrayList<HoldingBean>();
	if (authSession==null) {
		return list;
	}

        List<Holding> holdings = FacesTools.getPortalDaoProvider().getPortalHoldingDao().getHoldingList();
        List<Company> companies = authSession.getCompanyList();

        Iterator<Holding> iterator = holdings.iterator();
        while( iterator.hasNext() ) {
            Holding holding = iterator.next();

            HoldingBean holdingBean = new HoldingBean( holding );

            Iterator<Long> it = holding.getCompanyIdList().iterator();
            while( it.hasNext() ) {
                Long companyId = it.next();

                Iterator<Company> cIt = companies.iterator();
                while( cIt.hasNext() ) {
                        Company company = cIt.next();

                        if (company.getId().equals( companyId )) {
                            CompanyBean bean = new CompanyBean();
                            bean.setName( company.getName() );
                            bean.setId( companyId );

                            holdingBean.addCompany( bean );
                        }
                }
            }
            list.add( holdingBean );
        }
        return list;
    }
}