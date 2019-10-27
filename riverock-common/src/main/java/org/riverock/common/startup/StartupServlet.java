/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.common.startup;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.riverock.common.config.ConfigException;
import org.riverock.common.config.ConfigObject;
import org.riverock.common.config.ConfigService;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.main.Constants;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;

/**
 * $Id: StartupServlet.java 1493 2009-11-22 19:06:46Z serg_main $
 */
public final class StartupServlet extends HttpServlet {
    private static final String LOG_CONFIG_FILE_PARAM_NAME = "log-config-file";
    private static final String LOG_PATH_PARAM_NAME = "log-path";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        System.out.println("\n\n");

        PropertiesProvider.setIsServletEnv(true);
        putAllConfigParameters(config);

        ServletContext context = config.getServletContext();
        PropertiesProvider.setApplicationPath(context.getRealPath("/"));
        PropertiesProvider.setConfigPath(
            PropertiesProvider.getApplicationPath() +
            (PropertiesProvider.getApplicationPath().endsWith(File.separator) ? "" : File.separator) +
            "WEB-INF" + File.separatorChar + "webmill"
        );
        System.out.println("info Application path: " + PropertiesProvider.getApplicationPath());
        System.out.println("info getServletContextName - " + config.getServletContext().getServletContextName());
        System.out.println("info servletConfig - " + config.getServletContext());

        String realPath = context.getRealPath("/");

        realPath = replacePattern(realPath, File.separator + "." + File.separator, File.separator);
        System.out.println("info realPath - " + realPath);

        File dir = new File(realPath);
        System.out.println("info dir - " + dir.getName());

        initLogging();

        try {
            ConfigService.initLocale();
        }
        catch (ConfigException exc) {
            String es = "Error init locale from resource bundle";
            throw new ServletException(es, exc);
        }
    }

    private void putAllConfigParameters(ServletConfig config) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration en = config.getInitParameterNames();
        while(en.hasMoreElements()){
            String key = (String)en.nextElement();
            String value = getInitParameter( key );
            map.put(key, value);
        }
        PropertiesProvider.getParameters().putAll( map );
    }

    private static String replacePattern(String str, String oldToken, String newToken) {
        if (str == null)
            return str;
        StringBuilder result = new StringBuilder(str.length() + 100);
        int i = str.indexOf(oldToken);
        int startOfIndex = 0;
        for (; i != -1; i = str.indexOf(oldToken, startOfIndex)) {
            result.append(str.substring(startOfIndex, i));
            result.append(newToken);
            startOfIndex = i + oldToken.length();
        }

        result.append(str.substring(startOfIndex, str.length()));
        return result.toString();
    }

    private synchronized static void initLogging() {
        boolean isLocal = "true".equalsIgnoreCase( PropertiesProvider.getParameter(ConfigObject.LOCAL_CONFIG_PARAM_NAME) );
        System.out.println("isLocal = " + isLocal);
        String logPath;
        if (isLocal) {
            String s = getLogPathFromConfig();
            if (s==null) {
                System.out.println("parameter '"+LOG_PATH_PARAM_NAME+"' not defined in servet");
                return;
            }

            logPath =
                PropertiesProvider.getApplicationPath()+
                File.separatorChar+
                "WEB-INF" +
                File.separatorChar+
                s;

            File file = new File(logPath);

            if (file.isFile()) {
                System.out.println("logPath "+logPath+" is a file, not a directory");
                return;
            }
            if (!file.exists()) {
                System.out.println("logPath "+file+" not exists. Create one.");
                file.mkdirs();
            }
        }
        else {
            logPath = getLogPathFromJNDI();
        }
        System.out.println("logPath = " + logPath);

        if (logPath!=null) {
            System.setProperty("mill.logging.path", logPath);
            System.setProperty("riverock.logging.path", logPath);
            System.out.println("logPath - " + logPath);
        }
        else {
            System.out.println("Log path not configured. isLocal config: " +isLocal);
            return;
        }

        String logConfigFile = null;
        if (isLocal) {
            logConfigFile =
                PropertiesProvider.getApplicationPath()+
                File.separatorChar+
                "WEB-INF" +
                File.separatorChar+
                getLogConfigFileFormConfig();
        }
        else {
            logConfigFile = getLogConfigFileFromJNDI(logConfigFile);
        }
        logConfigFile = logConfigFile.replace( File.separatorChar == '/'?'\\':'/', File.separatorChar );
        System.out.println("logConfigFile: " + logConfigFile);

        if (logConfigFile!=null) {
            // configure log4j
            PropertyConfigurator.configure(logConfigFile);
        }
        else {
            System.out.println("Log config file not configured. isLocal config: " + isLocal);
        }
    }

    public final void destroy() {
        try {
            LogManager.shutdown();
        } catch (Throwable e) {
            e.printStackTrace(); 
        }
    }

    private static String getLogPathFromConfig() {
        return PropertiesProvider.getParameter( LOG_PATH_PARAM_NAME );
    }

    private static String getLogConfigFileFormConfig() {
        return PropertiesProvider.getParameter( LOG_CONFIG_FILE_PARAM_NAME );
    }

    private static String getLogConfigFileFromJNDI(String logConfigFile) {
	String name = "java:comp/env/" + Constants.JNDI_MILL_LOG_CONFIG_FILE;
        try {
            InitialContext ic = new InitialContext();
            logConfigFile = (String) ic.lookup( name ); 
        }
        catch (NamingException e) {
            System.out.println("Error get name of logConfigFile. JNDI name " + name);
            e.printStackTrace(System.out);
        }
        return logConfigFile;
    }

    private static String getLogPathFromJNDI() {
        try {
            InitialContext ic = new InitialContext();
            String logPath = (String) ic.lookup("java:comp/env/" + Constants.JNDI_MILL_LOG_PATH);
            logPath = logPath.replace( File.separatorChar == '/'?'\\':'/', File.separatorChar );

            return logPath;
        }
        catch (NamingException e) {
            System.out.println("Error get millLogPath object: " + e.toString());
            e.printStackTrace(System.out);
            return null;
        }
    }

    public static void test() {
        Context ctx;
        try {
            ctx = new InitialContext();
        }
        catch (NamingException e) {
            System.out.println("Couldn't build an initial context : <br>" + e);
            return;
        }

        try {
            Object value = ctx.lookup("java:/comp/env/maxExemptions");
            System.out.println("Simple lookup test : " + "<br>");
            System.out.println("Max exemptions value : " + value + "<br>");
        }
        catch (NamingException e) {
            System.out.println("JNDI lookup failed : " + e);
        }

        try {
            System.out.println("list() on java:/comp/env Context : ");
            NamingEnumeration en = ctx.list("java:/comp/env/");
            while (en.hasMoreElements()) {
                System.out.print("Binding : " + "<br>");
                System.out.println(en.nextElement().toString() + "<br>");
            }
            System.out.println("list Bindings() on java:/comp/env Context : " + "<br>");
            en = ctx.listBindings("java:/comp/env/");
            while (en.hasMoreElements()) {
                System.out.print("Binding : " + "<br>");
                System.out.println(en.nextElement().toString() + "<br>");
            }
        }
        catch (NamingException e) {
            System.out.println("JNDI lookup failed : " + e);
        }

        try {
            System.out.println("list() on /comp/env Context : ");
            NamingEnumeration en = ctx.list("java:/comp/env/");
            while (en.hasMoreElements()) {
                System.out.print("Binding : " + "<br>");
                System.out.println(en.nextElement().toString() + "<br>");
            }
            System.out.println("list Bindings() on /comp/env Context : " + "<br>");
            en = ctx.listBindings("bean/");
            while (en.hasMoreElements()) {
                System.out.print("Binding : " + "<br>");
                System.out.println(en.nextElement().toString() + "<br>");
            }
        }
        catch (NamingException e) {
            System.out.println("JNDI lookup failed : " + e);
        }
    }

}
