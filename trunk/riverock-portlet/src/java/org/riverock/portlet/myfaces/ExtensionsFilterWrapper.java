package org.riverock.portlet.myfaces;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.apache.myfaces.webapp.filter.ExtensionsFilter;

/**
 * @author Sergei Maslyukov
 *         Date: 10.05.2006
 *         Time: 14:26:19
 */
public class ExtensionsFilterWrapper extends ExtensionsFilter {
    private final static Logger log = Logger.getLogger( ExtensionsFilterWrapper.class );

    public ExtensionsFilterWrapper() {
        super();
        if (log.isDebugEnabled()) {
            log.debug("Create instance of ExtensionsFilterWrapper()");
        }
    }

    public void init(javax.servlet.FilterConfig filterConfig) {
        if (log.isDebugEnabled()) {
            log.debug("ExtensionsFilterWrapper().init()");
        }
        super.init(filterConfig);
    }

    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws java.io.IOException, javax.servlet.ServletException {
        if (log.isDebugEnabled()) {
            log.debug("ExtensionsFilterWrapper().doFilter()");
            log.debug("    request: " + servletRequest);
            log.debug("    response: " + servletResponse);
            log.debug("    filterChain: " + filterChain);
        }
        super.doFilter(servletRequest, servletResponse, filterChain);
    }

    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("ExtensionsFilterWrapper().destroy()");
        }
        super.destroy();
    }

}
