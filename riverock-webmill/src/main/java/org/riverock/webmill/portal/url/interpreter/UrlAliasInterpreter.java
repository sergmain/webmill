package org.riverock.webmill.portal.url.interpreter;

import org.riverock.webmill.portal.aliases.UrlProviderFactory;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2007
 * Time: 22:08:31
 * $Id$
 */
public class UrlAliasInterpreter implements UrlInterpreter{
    
    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {
        String alias = UrlProviderFactory.getUrlProvider().getAlias(factoryParameter.getSiteId(), factoryParameter.getPathInfo());
        if (alias!=null) {
            factoryParameter.setPathInfo(alias);
        }
        // always return null for continue processing
        return null;
    }
}
