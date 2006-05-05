package org.riverock.interfaces.portal.bean;

import java.util.List;

/**
 * @author Sergei Maslyukov
 *         Date: 05.05.2006
 *         Time: 20:56:29
 */
public interface CatalogItem {
    public Long getCatalogId();

    public Long getTopCatalogId();

    public Long getPortletId();

    public Long getContextId();

    public Boolean getUseProperties();

    public Long getTemplateId();

    public Long getCatalogLanguageId();

    public Integer getOrderField();

    public String getStorage();

    public String getKeyMessage();

    public String getUrl();

    public String getTitle();

    public String getAuthor();

    public String getKeyword();

    public String getMetadata();

    public String getPortletRole();

    public List<CatalogItem> getSubCatalogItemList();
}
