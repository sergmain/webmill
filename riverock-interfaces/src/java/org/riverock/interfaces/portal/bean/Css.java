package org.riverock.interfaces.portal.bean;

import java.util.Date;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 20:37:18
 */
public interface Css {
    public Long getCssId();
    public Long getSiteId();
    public String getCss();
    public String getCssComment();
    public Date getDate();
    public boolean isCurrent();
}
