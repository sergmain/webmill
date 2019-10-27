package org.riverock.portlet.manager.site.bean;

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * User: SMaslyukov
 * Date: 20.06.2007
 * Time: 14:43:38
 */
public class VirtualHostBean implements VirtualHost {
    private Long id = null;

    private Long siteId = null;

    private String host = null;

    private boolean isDefaultHost = false;

    public VirtualHostBean(VirtualHost host) {
        this.host = host.getHost();
        this.id = host.getId();
        isDefaultHost = host.isDefaultHost();
        this.siteId = host.getSiteId();
    }

    public VirtualHostBean(String host, Long id, boolean defaultHost, Long siteId) {
        this.host = host;
        this.id = id;
        isDefaultHost = defaultHost;
        this.siteId = siteId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDefaultHost() {
        return isDefaultHost;
    }

    public void setDefaultHost(boolean defaultHost) {
        isDefaultHost = defaultHost;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String toString() {
        return "[siteId:"+siteId+";id:"+id+";is:"+isDefaultHost+";host:"+host+"]";
    }
}
