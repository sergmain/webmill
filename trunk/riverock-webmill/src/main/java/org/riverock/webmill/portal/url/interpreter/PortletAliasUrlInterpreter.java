package org.riverock.webmill.portal.url.interpreter;

import org.riverock.webmill.portal.aliases.UrlProviderFactory;
import org.riverock.webmill.portal.bean.PortletAliasBean;

/**
 * User: SMaslyukov
 * Date: 20.08.2007
 * Time: 15:35:20
 */
public class PortletAliasUrlInterpreter implements UrlInterpreter {
    
    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {
        PortletAliasBean alias = UrlProviderFactory.getPortletAliaslProvider().getAlias(factoryParameter.getSiteId(), factoryParameter.getPathInfo());
        if (alias!=null) {
            StringBuilder pathInfo = CtxUrlInterpreter.encodeUrl(
                factoryParameter.getPortalContextPath(), alias.getPortletName(), alias.getTemplateName(), alias.getLocale(), false, null, null
            );
            factoryParameter.setPathInfo(
                pathInfo.toString()
            );
        }
        // always return null for continue processing
        return null;
    }
}
