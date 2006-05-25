package org.riverock.interfaces.generic;

/**
 * @author Sergei Maslyukov
 *         Date: 25.05.2006
 *         Time: 15:55:35
 */
/**
 * project: pluto, license: Apache2
 * The internal render request interface extends the internal portlet request
 * interface and provides some render-specific methods.
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @since 2006-02-17
 */
public interface InternalRequest {
    public void setIncluded(boolean included);
    public boolean isIncluded();
    public void setIncludedQueryString(String queryString);
}
