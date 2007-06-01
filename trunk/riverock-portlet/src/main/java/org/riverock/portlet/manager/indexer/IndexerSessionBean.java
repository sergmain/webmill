package org.riverock.portlet.manager.indexer;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 21:03:35
 */
public class IndexerSessionBean implements Serializable {
    private static final long serialVersionUID = 9957005500L;

    private Long portletIndexerId;

    public IndexerSessionBean() {
    }

    public Long getPortletIndexerId() {
        return portletIndexerId;
    }

    public void setPortletIndexerId(Long portletIndexerId) {
        this.portletIndexerId = portletIndexerId;
    }
}

