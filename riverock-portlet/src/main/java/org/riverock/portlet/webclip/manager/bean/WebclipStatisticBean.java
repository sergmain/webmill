package org.riverock.portlet.webclip.manager.bean;

/**
 * User: SMaslyukov
 * Date: 28.05.2007
 * Time: 18:30:34
 */
public class WebclipStatisticBean {
    private long totalCount;
    private long forReloadCount;
    private long forProcessCount;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getForProcessCount() {
        return forProcessCount;
    }

    public void setForProcessCount(long forProcessCount) {
        this.forProcessCount = forProcessCount;
    }

    public long getForReloadCount() {
        return forReloadCount;
    }

    public void setForReloadCount(long forReloadCount) {
        this.forReloadCount = forReloadCount;
    }
}
