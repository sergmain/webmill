package org.riverock.interfaces.portal.template;

/**
 * @author SergeMaslyukov
 *         Date: 01.01.2006
 *         Time: 9:12:11
 *         $Id$
 */
public interface PortalTemplateManager {
    public PortalTemplate getTemplate( final String templateName, final String localeName );
    public PortalTemplate getTemplate( final long templateId );
}
