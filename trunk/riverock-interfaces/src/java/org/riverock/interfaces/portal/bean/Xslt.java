package org.riverock.interfaces.portal.bean;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:25:30
 */
public interface Xslt {
    public Long getId();
    public Long getSiteLanguageId();
    public String getName();
    public String getXsltData();
    public boolean isCurrent();
}

