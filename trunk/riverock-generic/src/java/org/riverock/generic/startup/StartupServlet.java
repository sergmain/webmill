/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * $Id$
 */
package org.riverock.generic.startup;

import java.io.File;
import java.security.Provider;
import java.security.Security;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.riverock.generic.main.Constants;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.config.GenericConfig;
import org.riverock.common.config.ConfigService;
import org.riverock.common.config.ConfigException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class StartupServlet extends HttpServlet
{
    private final static Logger log = Logger.getLogger(StartupServlet.class);

    private static boolean checkClass(String name)
    {
        try
        {
            if (Class.forName(name) != null)
            {
                log.info("class " + name + " present");
                System.out.println("class " + name + " present");
                return true;
            }
            else
            {
                log.fatal("class " + name + " NOT present");
                System.out.println("class " + name + " NOT present");
            }
        }
        catch (Exception e)
        {
            log.fatal("Exception create class " + name + " -  " + e.getMessage());
            System.out.println("Exception create class " + name + " -  " + e.getMessage());
        }
        return false;
    }

    private static String replacePattern(String str, String oldToken, String newToken)
    {
        if (str == null)
            return str;
        StringBuffer result = new StringBuffer(str.length() + 100);
        int i = str.indexOf(oldToken);
        int startOfIndex = 0;
        for (; i != -1; i = str.indexOf(oldToken, startOfIndex))
        {
            result.append(str.substring(startOfIndex, i));
            result.append(newToken);
            startOfIndex = i + oldToken.length();
        }

        result.append(str.substring(startOfIndex, str.length()));
        return result.toString();
    }

    public static void test()
    {
        Context ctx = null;

        try
        {
            ctx = new InitialContext();
        }
        catch (NamingException e)
        {
            System.out.println("Couldn't build an initial context : <br>" + e);
            return;
        }

        try
        {
            Object value = ctx.lookup("java:/comp/env/maxExemptions");
            System.out.println("Simple lookup test : " + "<br>");
            System.out.println("Max exemptions value : " + value + "<br>");
        }
        catch (NamingException e)
        {
            System.out.println("JNDI lookup failed : " + e);
        }

        try
        {
            Context envCtx = (Context) ctx.lookup("java:/comp/env/");
            System.out.println("list() on java:/comp/env Context : ");
            NamingEnumeration enum = ctx.list("java:/comp/env/");
            while (enum.hasMoreElements())
            {
                System.out.print("Binding : " + "<br>");
                System.out.println(enum.nextElement().toString() + "<br>");
            }
            System.out.println("list Bindings() on java:/comp/env Context : " + "<br>");
            enum = ctx.listBindings("java:/comp/env/");
            while (enum.hasMoreElements())
            {
                System.out.print("Binding : " + "<br>");
                System.out.println(enum.nextElement().toString() + "<br>");
            }
        }
        catch (NamingException e)
        {
            System.out.println("JNDI lookup failed : " + e);
        }

        try
        {
            System.out.println("list() on /comp/env Context : ");
            NamingEnumeration enum = ctx.list("java:/comp/env/");
            while (enum.hasMoreElements())
            {
                System.out.print("Binding : " + "<br>");
                System.out.println(enum.nextElement().toString() + "<br>");
            }
            System.out.println("list Bindings() on /comp/env Context : " + "<br>");
            enum = ctx.listBindings("bean/");
            while (enum.hasMoreElements())
            {
                System.out.print("Binding : " + "<br>");
                System.out.println(enum.nextElement().toString() + "<br>");
            }
        }
        catch (NamingException e)
        {
            System.out.println("JNDI lookup failed : " + e);
        }
    }

    private synchronized static void initLogging() {
        String logPath = null;
        try {
            InitialContext ic = new InitialContext();
            logPath = (String) ic.lookup("java:comp/env/" + Constants.JNDI_MILL_LOG_PATH);
            if (File.separatorChar=='\\')
                logPath = logPath.replace( '/', '\\');
            else
                logPath = logPath.replace( '\\', '/');

            System.setProperty("mill.logging.path", logPath);
            System.setProperty("riverock.logging.path", logPath);
            System.out.println("logPath - " + logPath);

        }
        catch (NamingException e) {
            System.out.println("Error get millLogPath object" + e.toString());
            e.printStackTrace(System.out);
            return;
        }

        String logConfigFile = null;
        try {
            InitialContext ic = new InitialContext();
            logConfigFile = (String) ic.lookup("java:comp/env/" + Constants.JNDI_MILL_LOG_CONFIG_FILE); // mill/LogConfigFile");
            if (File.separatorChar=='\\')
                logConfigFile = logConfigFile.replace( '/', '\\');
            else
                logConfigFile = logConfigFile.replace( '\\', '/');

            System.out.println("millLogConfigFile - " + logConfigFile);
        }
        catch (NamingException e) {
            System.out.println("Error get millLogConfigFile object");
            e.printStackTrace(System.out);
            return;
        }
        PropertyConfigurator.configure(logConfigFile);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

//        test();
        Object testObj = config.getServletContext().getAttribute("javax.servlet.context.tempdir");
        System.out.println("info Object tempdir - " + testObj);
        if (testObj!=null && testObj instanceof java.io.File) {
            File testFile = (File)testObj;
            System.out.println("info javax.servlet.context.tempdir - " + testFile);
        }
/*
        if (log.isDebugEnabled())
        {
            Object testObj = config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            log.debug("Object tempdir - "+testObj);
            if (testObj instanceof java.io.File)
            {
                File testFile = (File)config.getServletContext().getAttribute("javax.servlet.context.tempdir");
                log.debug("tempdir - "+testFile.getName());
            }
        }
*/

        PropertiesProvider.setIsServletEnv( true );

        ServletContext context = config.getServletContext();

        PropertiesProvider.setApplicationPath( config.getServletContext().getRealPath("/") );

//        context.getResourcePaths()

        PropertiesProvider.setConfigPath(
            PropertiesProvider.getApplicationPath() +
            (PropertiesProvider.getApplicationPath().endsWith(File.separator) ? "" : File.separator) +
            "WEB-INF" + File.separatorChar + "mill"
        );

        log.info("Application path: " + PropertiesProvider.getApplicationPath() );
        System.out.println("info Application path: " + PropertiesProvider.getApplicationPath() );

        log.info("getServletContextName - " + config.getServletContext().getServletContextName() );
        System.out.println("info getServletContextName - " + config.getServletContext().getServletContextName() );

        log.info("servletConfig - " + config.getServletContext().getServletContextName() );
        System.out.println("info servletConfig - " + config.getServletContext() );

        String realPath = context.getRealPath("/");

        realPath = replacePattern(realPath, File.separator + "." + File.separator, File.separator);
        log.info("realPath - " + realPath);
        System.out.println("info realPath - " + realPath);

        File dir = new File(realPath);
        log.info("dir - " + dir.getName());
        System.out.println("info dir - " + dir.getName());

        GenericConfig.setContextName( "/" + dir.getName() );

        log.info("ServletContextName - " + GenericConfig.getContextName());
        System.out.println("info ServletContextName - " + GenericConfig.getContextName());

        Provider provider = Security.getProvider( "BC" );
        if (provider != null) {
            log.info( "Security provider  present. " + provider.getInfo() );
            System.out.println( "info Security provider  present. " + provider.getInfo() );
        }
        else {
            log.warn("'Bouncycastle' security provider not present. Check for class");

            if (!checkClass("org.bouncycastle.jce.provider.BouncyCastleProvider")) {
                log.error("Security provider BouncyCastle not present");
                System.out.println("fatal Security provider BouncyCastle not present");
            }
        }

        try {
            System.out.println("info Xalan version - " + org.apache.xalan.Version.getVersion());
        }
        catch (Exception e) {
            System.out.println("Error get version of xalan " + e.getMessage());
        }
        try {
            System.out.println("info Xerces version - " + org.apache.xerces.impl.Version.getVersion());
        }
        catch (Exception e) {
            System.out.println("Error get version of xerces " + e.getMessage());
        }
        try {
            System.out.println("info Castor version - " + org.exolab.castor.util.Version.getBuildVersion());
        }
        catch (Exception e) {
            System.out.println("Error get version of Castor " + e.getMessage());
        }

        initLogging();

        // init time zone
        try {
            ConfigService.initLocale();
            GenericConfig.getTZ();
        }
        catch(ConfigException exc) {
            String es = "Error init time zone";
            log.error( es, exc );
            throw new ServletException( es, exc );
        }
    }
}
