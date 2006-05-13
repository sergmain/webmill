package org.riverock.webmill.portal.namespace;

import org.riverock.webmill.portal.PortalConstants;

/**
 * @author SergeMaslyukov
 *         Date: 08.03.2006
 *         Time: 2:29:26
 *         $Id$
 */
public class NamespaceImpl implements Namespace{
    private String namespace;

    public NamespaceImpl(String namespace) {
        this.namespace = namespace;
    }
    
    public String getNamespace() {
        return namespace;
    }
}
