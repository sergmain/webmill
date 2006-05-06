package org.riverock.interfaces.portal.bean;

/**
 * @author Sergei Maslyukov
 *         Date: 06.05.2006
 *         Time: 15:26:45
 */
public interface CatalogLanguageItem {
    public Long getCatalogLanguageId();

    public Boolean getDefault();

    public Long getSiteLanguageId();

    public String getCatalogCode();
}
