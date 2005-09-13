package org.riverock.webmill.container.portlet;

import java.io.File;
import java.io.Serializable;

import org.riverock.webmill.container.portlet.bean.PortletApplication;

/**
 * @author smaslyukov
 *         Date: 05.08.2005
 *         Time: 20:11:35
 *         $Id$
 */
public interface PortletDefinitionProcessor {
    public PortletApplication digest( File portletFile ) throws PortletContainerException;
}
