package org.riverock.module.web.config;

import javax.portlet.PortletConfig;
import java.util.ResourceBundle;
import java.util.Locale;

import org.riverock.module.web.context.ModuleContext;
import org.riverock.module.web.context.PortletModuleContextImpl;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:49:25
 *         $Id$
 */
public class PortletModuleConfigImpl implements ModuleConfig {
    private PortletConfig portletConfig = null;

    public PortletModuleConfigImpl(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    public ModuleContext getContext() {
        return new PortletModuleContextImpl( portletConfig.getPortletContext() );
    }

    public String getInitParameter(String key) {
        return portletConfig.getInitParameter(key);
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return portletConfig.getResourceBundle(locale);
    }

    public Integer getInitParameterInt(String name) {
        return getInitParameterInt(name, null);
    }

    public Long getInitParameterLong(String name) {
        return getInitParameterLong(name, null);
    }

    public Double getInitParameterDouble(String name) {
        return getInitParameterDouble(name, null);
    }

    public Integer getInitParameterInt( final String name, final Integer defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Integer(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }

    public Long getInitParameterLong( final String name, final Long defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Long(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }

    public Double getInitParameterDouble( final String name, final Double defValue ) {
        String value = portletConfig.getInitParameter(name);
        if (value==null) {
            return defValue;
        }
        try {
            return new Double(value);
        }
        catch(Exception e) {
            return defValue;
        }
    }
}
