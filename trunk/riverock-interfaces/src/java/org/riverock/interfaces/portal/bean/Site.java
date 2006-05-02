package org.riverock.interfaces.portal.bean;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:24:08
 */
public interface Site {
    public Long getSiteId();

    public Long getCompanyId();

    public String getSiteName();

    public Boolean getCssDynamic();

    public String getCssFile();

    public Boolean getRegisterAllowed();

    public String getDefLanguage();

    public String getDefCountry();

    public String getDefVariant();

    public String getAdminEmail();
}
