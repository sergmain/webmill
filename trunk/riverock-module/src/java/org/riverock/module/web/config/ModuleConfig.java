package org.riverock.module.web.config;

import java.util.ResourceBundle;
import java.util.Locale;

import org.riverock.module.web.context.ModuleContext;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 14:46:00
 *         $Id$
 */
public interface ModuleConfig {
    public ModuleContext getContext();

    public String getInitParameter(String key);
    public ResourceBundle getResourceBundle(Locale locale);

    public Integer getInitParameterInt( String name );
    public Integer getInitParameterInt( String name, Integer defValue );

    public Long getInitParameterLong( String name );
    public Long getInitParameterLong( String name, Long defValue );

    public Double getInitParameterDouble( String name );
    public Double getInitParameterDouble( String name, Double defValue );
}
