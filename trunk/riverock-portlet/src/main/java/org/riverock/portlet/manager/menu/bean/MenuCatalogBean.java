package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 *         Date: 16.06.2006
 *         Time: 20:45:54
 */
public class MenuCatalogBean implements Serializable, CatalogLanguageItem {
    private static final long serialVersionUID = 1056105507L;

    private Long catalogLanguageId;
    private Boolean isDefault = false;
    private Long siteLanguageId;
    private String catalogCode = null;

    public MenuCatalogBean(){}

    public MenuCatalogBean(CatalogLanguageItem item){
        this.catalogLanguageId=item.getCatalogLanguageId();
        this.isDefault=item.getDefault();
        this.siteLanguageId=item.getSiteLanguageId();
        this.catalogCode=item.getCatalogCode();
    }

    public Long getCatalogLanguageId() {
        return catalogLanguageId;
    }

    public void setCatalogLanguageId(Long catalogLanguageId) {
        this.catalogLanguageId = catalogLanguageId;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = FacesTools.convertParameter(catalogCode);
    }
}
