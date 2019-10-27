package org.riverock.portlet.webclip;

/**
 * User: SMaslyukov
 * Date: 11.07.2007
 * Time: 12:21:19
 */
public class WebclipBeanExtended {
    private WebclipBean webclip=null;
    private String url;
    private String href;
    private String prefix;
    private String status=null;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebclipBean getWebclip() {
        return webclip;
    }

    public void setWebclip(WebclipBean webclip) {
        this.webclip = webclip;
    }
}
