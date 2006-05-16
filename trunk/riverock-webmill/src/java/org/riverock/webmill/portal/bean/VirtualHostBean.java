package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:56:55
 */
public class VirtualHostBean implements Serializable, VirtualHost {
    private static final long serialVersionUID = 3255005504L;

    private Long id = null;
    private Long siteId = null;
    private String host = null;

    public VirtualHostBean() {
    }

    public VirtualHostBean(Long id, Long siteId, String host) {
        this.id = id;
        this.siteId = siteId;
        this.host = host;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
