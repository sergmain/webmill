package org.riverock.portlet.manager.site.action;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.manager.site.DataProvider;
import org.riverock.portlet.manager.site.SiteSessionBean;
import org.riverock.portlet.manager.site.bean.TemplateBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:14:59
 *
 *
 */
public class TemplateAction implements Serializable {
    private final static Logger log = Logger.getLogger( TemplateAction.class );
    private static final long serialVersionUID = 2057005511L;

    private SiteSessionBean siteSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private DataProvider dataProvider = null;

    public TemplateAction() {
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    // getter/setter methods
    public void setSiteSessionBean( SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectTemplate(ActionEvent event) {
        TemplateAction.log.info( "Select template action." );
        loadCurrentObject();

        return "site";
    }

// Add actions
    public String addTemplateAction() {
        TemplateAction.log.info( "Add template action." );

        TemplateBean templateBean = new TemplateBean();
        templateBean.setSiteLanguageId(siteSessionBean.getId());
        setSessionObject(templateBean);

        return "template-add";
    }

    public String processAddTemplateAction() {
        TemplateAction.log.info( "Procss add template action." );

        if( getSessionObject() !=null ) {
            Long templateId = FacesTools.getPortalDaoProvider().getPortalTemplateDao().createTemplate(
                getSessionObject()
            );
            setSessionObject(null);
            siteSessionBean.setId(templateId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelAddTemplateAction() {
        TemplateAction.log.info( "Cancel add template action." );

        setSessionObject(null);
        cleadDataProviderObject();

        return "site";
    }

// Edit actions
    public String editTemplateAction() {
        TemplateAction.log.info( "Edit holding action." );

        return "tenmplate-edit";
    }

    public String processEditTemplateAction() {
        TemplateAction.log.info( "Save changes template action." );

        if( getSessionObject() !=null ) {
            FacesTools.getPortalDaoProvider().getPortalTemplateDao().updateTemplate(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "site";
    }

    public String cancelEditTemplateAction() {
        TemplateAction.log.info( "Cancel edit template action." );

        return "site";
    }

// Delete actions
    public String deleteTemplateAction() {
        TemplateAction.log.info( "delete template action." );

        setSessionObject( new TemplateBean(dataProvider.getTemplate()) );

        return "template-delete";
    }

    public String cancelDeleteTemplateAction() {
        TemplateAction.log.info( "Cancel delete holding action." );

        return "site";
    }

    public String processDeleteTemplateAction() {
        TemplateAction.log.info( "Process delete template action." );

        if( getSessionObject() != null ) {
            FacesTools.getPortalDaoProvider().getPortalTemplateDao().deleteTemplate(getSessionObject().getTemplateId());
            setSessionObject(null);
            siteSessionBean.setId(null);
            siteSessionBean.setObjectType(SiteSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "site";
    }

    private void setSessionObject(TemplateBean bean) {
        siteSessionBean.setTemplate( bean );
    }

    private void loadCurrentObject() {
        siteSessionBean.setTemplate( new TemplateBean(dataProvider.getTemplate()) );
    }

    private void cleadDataProviderObject() {
        dataProvider.clearTemplate();
    }

    private TemplateBean getSessionObject() {
        return siteSessionBean.getTemplate();
    }
}
