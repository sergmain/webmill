package org.riverock.webmill.portal.context;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portlet.menu.MenuLanguage;

/**
 * $Id$
 */
public class IndexRequestContextProcessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(IndexRequestContextProcessor.class);

    public IndexRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

        // process current request as 'index'
        MenuLanguage menu = factoryParameter.getPortalInfo().getMenu(factoryParameter.getPredictedLocale().toString());
        if (menu==null){
            log.error( "Menu for locale: "+factoryParameter.getPredictedLocale().toString() +" not defined" );
            return null;
        }

        if (menu.getIndexMenuItem() == null) {
            log.warn("menu: " + menu);
            log.warn("locale: " + factoryParameter.getPredictedLocale().toString());
            log.warn("Menu item pointed to 'index' portlet not defined");
            return null;
        }

        Long ctxId = menu.getIndexMenuItem().getId();
        if (ctxId==null) {
            String es = "Menu item with 'index' portlet not found";
            log.error(es);
            throw new IllegalStateException(es);
        }

        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
