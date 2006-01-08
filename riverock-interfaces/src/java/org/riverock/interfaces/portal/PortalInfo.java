package org.riverock.interfaces.portal;

import java.util.Locale;
import java.util.Map;

import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portal.template.PortalTemplateManager;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 21:04:22
 *         $Id$
 */
public interface PortalInfo {
    public Long getSiteId();
    public Long getCompanyId();
    public Long getSupportLanguageId( Locale locale );
    public MenuLanguage getMenu(String locale);
    public XsltTransformerManager getXsltTransformerManager();
    public Map<String, String> getMetadata();
    public Locale getDefaultLocale();
    public PortalTemplateManager getPortalTemplateManager();
}

