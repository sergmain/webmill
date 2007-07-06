package org.riverock.portlet.webclip;

/**
 * User: SMaslyukov
 * Date: 06.07.2007
 * Time: 14:12:59
 */
public interface WebclipUrlChecker {
    public boolean isExist(Long siteLanguageId, String url);
}
