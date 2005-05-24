package org.riverock.module.web.request;

import org.riverock.module.web.session.ModuleSession;
import org.riverock.module.web.user.ModuleUser;
import org.riverock.module.exception.ModuleException;

import java.util.Locale;
import java.security.Principal;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:39:17
 *         $Id$
 */
public interface ModuleRequest {

    public Object getOriginRequest();
    public Locale getLocale();

    public Integer getInt(String key);
    public Integer getInt(String key, Integer defValue);
    public String getString(String key);
    public String getString(String key, String defValue);
    public Long getLong(String key);
    public Long getLong(String key, Long defValue);
    public Double getDouble(String key);
    public Double getDouble(String key, Double defValue);

    public String getParameter(String key);
    public void setAttribute(String key, Object value);

    public ModuleSession getSession();
    public ModuleSession getSession(boolean isCreate);

    public ModuleUser getUser();

    public String getServerName();
    public String getRemoteAddr();
    public String getUserAgent();

    public boolean isUserInRole(String role);

    public Long getServerNameId() throws ModuleException;
}
