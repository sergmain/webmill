package org.riverock.webmill.portal;

/**
 * User: SMaslyukov
 * Date: 03.05.2007
 * Time: 18:59:06
 */
public class PortalTransformationParameters {
    private String title;
    private String keyword;
    private String author;
    private String portalContextPath;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortalContextPath() {
        return portalContextPath;
    }

    public void setPortalContextPath(String portalContextPath) {
        this.portalContextPath = portalContextPath;
    }
}
