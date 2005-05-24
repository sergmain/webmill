package org.riverock.module.web.url;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 17:02:02
 *         $Id$
 */
public interface UrlProvider {
    public String getUrl(String moduleName, String actionName);
    public StringBuffer getUrlStringBuffer(String moduleName, String actionName);
}
