package org.riverock.webmill.portal.aliases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.riverock.webmill.portal.bean.PortletAliasBean;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.common.tools.StringTools;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:55:56
 */
public class PortletAliaslProviderImpl implements PortletAliaslProvider {

    private final ConcurrentMap<Long, Map<String, PortletAliasBean>> aliases = new ConcurrentHashMap<Long, Map<String, PortletAliasBean>>();

    public PortletAliaslProviderImpl() {
        InternalDaoFactory.getInternalAliasDao().addObserver(this);
    }

    public PortletAliasBean getAlias(Long siteId, String targetUrl) {
        Map<String, PortletAliasBean> aliases = this.aliases.get(siteId);

        if (aliases==null) {
            aliases = loadAliases(siteId);
            this.aliases.put(siteId, aliases);
        }
        for (Map.Entry<String, PortletAliasBean> entry : aliases.entrySet()) {
            if (targetUrl.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    public synchronized void invalidateCache() {
        aliases.clear();
    }

    private synchronized Map<String, PortletAliasBean> loadAliases(Long siteId) {
        Map<String, PortletAliasBean> aliases = this.aliases.get(siteId);

        if (aliases!=null) {
            return aliases;
        }
        aliases = new HashMap<String, PortletAliasBean>();
        List<PortletAliasBean> portletAliases = InternalDaoFactory.getInternalAliasDao().getPortletAliases(siteId);
        for (PortletAliasBean portletAlias : portletAliases) {
            portletAlias.setPortletName(InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletAlias.getPortletNameId()).getPortletName());
            TemplateBean template = InternalDaoFactory.getInternalTemplateDao().getTemplate(portletAlias.getTemplateId());
            portletAlias.setTemplateName(template.getTemplateName());
            portletAlias.setLocale(
                StringTools.getLocale(InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(template.getSiteLanguageId()).getCustomLanguage())
            );

            aliases.put(portletAlias.getShortUrl(), portletAlias);
        }
        return aliases;
    }

    public void update(Observable o, Object arg) {
        invalidateCache();
    }
}