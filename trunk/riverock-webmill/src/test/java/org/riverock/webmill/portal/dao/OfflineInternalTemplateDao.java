package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.digester.Digester;

import org.xml.sax.SAXException;

import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.bean.PortletNameBean;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 11:38:02
 */
public class OfflineInternalTemplateDao implements InternalTemplateDao{

    public List<TemplateBean> templateBeans = new ArrayList<TemplateBean>();

    public static class TemplateBeans {
        public List<TemplateBean> items = new ArrayList<TemplateBean>();

        public void addItem(TemplateBean item) {
            items.add(item);
        }

        public List<TemplateBean> getItems() {
            return items;
        }

        public void setItems(List<TemplateBean> items) {
            this.items = items;
        }
    }

    public OfflineInternalTemplateDao() throws IOException, SAXException {
        loadTemplates();
    }

    /**
     * templateData is initialized
     *
     * @param templateId template Id
     * @return template
     */
    public TemplateBean getTemplate(Long templateId) {
        for (TemplateBean templateBean : templateBeans) {
            if (templateBean.getTemplateId().equals(templateId)) {
                HibernateTemplateDaoImpl.prepareBlob(templateBean);
                return templateBean;
            }
        }
        return null;
    }

    /**
     * templateData is initialized
     *
     * @param templateName   template name
     * @param siteLanguageId site language id
     * @return template
     */
    public TemplateBean getTemplate(String templateName, Long siteLanguageId) {
        return null; 
    }

    /**
     * Attention! template data not initalized
     *
     * @param siteId       site  ID
     * @param templateName String
     * @param lang         String
     * @return org.riverock.interfaces.portal.bean.Template - Attention! template data not initialized
     */
    public TemplateBean getTemplate(Long siteId, String templateName, String lang) {
        if (siteId==null || templateName==null || lang==null) {
            return null;
        }
        for (TemplateBean templateBean : templateBeans) {
            // hack, work only with siteId 16L and locale 'en'
            if (siteId==16L && templateName.equals(templateBean.getTemplateName()) && lang.equals("en")) {
                return templateBean;
            }
        }
        return null;
    }

    public List<TemplateBean> getTemplateLanguageList(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<TemplateBean> getTemplateList(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createTemplate(TemplateBean template) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteTemplate(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteTemplateForSite(Long siteId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteTemplateForSiteLanguage(Long siteLanguageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateTemplate(TemplateBean template) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDefaultDynamic(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getDefaultDynamicTemplate(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getMaximizedTemplate(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setMaximizedTemplate(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public TemplateBean getPopupTemplate(Long siteLanguageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPopupTemplate(Long templateId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void loadTemplates() throws IOException, SAXException {
/*
 <ID_SITE_TEMPLATE>84</ID_SITE_TEMPLATE>
 <NAME_SITE_TEMPLATE>templ-mill-index2-en</NAME_SITE_TEMPLATE>
 <TEMPLATE_DATA></TEMPLATE_DATA>
 <ID_SITE_SUPPORT_LANGUAGE>13</ID_SITE_SUPPORT_LANGUAGE>
 <IS_DEFAULT_DYNAMIC>0</IS_DEFAULT_DYNAMIC>
 <VERSION>7</VERSION>
 <TEMPLATE_BLOB></TEMPLATE_BLOB>
 <TEMPLATE_ROLE></TEMPLATE_ROLE>
 <IS_MAXIMIZED_TEMPLATE>0</IS_MAXIMIZED_TEMPLATE>
 <IS_POPUP_TEMPLATE>0</IS_POPUP_TEMPLATE>
*/
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("table", TemplateBeans.class);
        digester.addObjectCreate("table/row", TemplateBean.class);
        digester.addSetNext("table/row", "addItem");
        digester.addBeanPropertySetter("table/row/ID_SITE_TEMPLATE", "templateId");
        digester.addBeanPropertySetter("table/row/NAME_SITE_TEMPLATE", "templateName");
        digester.addBeanPropertySetter("table/row/TEMPLATE_DATA", "templateData");
        digester.addBeanPropertySetter("table/row/ID_SITE_SUPPORT_LANGUAGE", "siteLanguageId");
        digester.addBeanPropertySetter("table/row/IS_DEFAULT_DYNAMIC", "defaultDynamic");
        digester.addBeanPropertySetter("table/row/VERSION", "version");
        digester.addBeanPropertySetter("table/row/TEMPLATE_ROLE", "templateRole");
        digester.addBeanPropertySetter("table/row/IS_MAXIMIZED_TEMPLATE", "maximizedTemplate");
        digester.addBeanPropertySetter("table/row/IS_POPUP_TEMPLATE", "popupTemplate");

        digester.addObjectCreate("table/row/TEMPLATE_BLOB", OfflineBlob.class);
//        digester.addCallMethod("table/row/TEMPLATE_BLOB", "setData", 0,  new Class[]{OfflineBlob.class});
        digester.addCallMethod("table/row/TEMPLATE_BLOB", "setData", 0,  new Class[]{String.class});
        digester.addSetNext("table/row/TEMPLATE_BLOB", "setTemplateBlob");

/*
        digester.addCallMethod("portlet-app/portlet/portlet-preferences/preference/value", "addValue", 0, new Class[]{String.class});
        digester.addCallMethod(
            "portlet-app/portlet/portlet-preferences/preference/read-only",
            "setReadOnly",
            0,
            new Class[] { Boolean.class });
*/

        InputStream inputStream = OfflineInternalCatalogDao.class.getResourceAsStream("/xml/site/template.xml");

        TemplateBeans items = (TemplateBeans)digester.parse(inputStream);

        this.templateBeans = items.getItems();
    }

}
