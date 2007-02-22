package org.riverock.webmill.admin.action;

import org.riverock.webmill.admin.CompanySessionBean;
import org.riverock.webmill.admin.dao.DaoFactory;
import org.riverock.webmill.admin.bean.CompanyBean;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 22.02.2007
 * Time: 15:09:07
 */
public class DbAction  implements Serializable {
    private static final long serialVersionUID = 2055005501L;

    private CompanySessionBean companySessionBean = null;

    public DbAction() {
    }

    public CompanySessionBean getCompanySessionBean() {
        return companySessionBean;
    }

    public void setCompanySessionBean(CompanySessionBean companySessionBean) {
        this.companySessionBean = companySessionBean;
    }

    public String addCompany() {
        companySessionBean.setCompany( new CompanyBean() );

        return "company-add";
    }

    public String processAddCompany() {
        Long companyId = DaoFactory.getWebmillAdminDao().processAddCompany( companySessionBean.getCompany() );
        companySessionBean.setCurrentCompanyId( companyId );
        loadCurrentCompany();

        return "company";
    }

    public String cancelAddCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processEditCompany() {
        DaoFactory.getWebmillAdminDao().processSaveCompany(companySessionBean.getCompany() );

        return "company";
    }

    public String cancelEditCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processDeleteCompany() {
        DaoFactory.getWebmillAdminDao().processDeleteCompany(companySessionBean.getCompany());
        companySessionBean.setCompany( null );
        return "company";
    }

    public String selectCompany() {
        loadCurrentCompany();
        return "company";
    }

    private void loadCurrentCompany() {
        CompanyBean bean = DaoFactory.getWebmillAdminDao().getCompany( companySessionBean.getCurrentCompanyId() );
        companySessionBean.setCompany( bean );
    }
}

