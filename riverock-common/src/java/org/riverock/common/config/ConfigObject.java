/*

 * org.riverock.common -- Supporting classes, interfaces, and utilities

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

package org.riverock.common.config;



import java.io.File;

import java.io.FileInputStream;



import javax.naming.InitialContext;

import javax.naming.NamingException;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



public class ConfigObject

{

    private static Logger log = Logger.getLogger("org.riverock.common.config.ConfigObject");



    private String nameConfigFile = null;



    private Object configObject = null;



    public ConfigObject()

    {

    }



    public Object getConfigObject()

    {

        return configObject;

    }



    public static ConfigObject load(String nameJndiCtx, String nameConfigFile_, Class configClass)

        throws ConfigException

    {

        ConfigObject config = new ConfigObject();

        File configFile = null;

        if (PropertiesProvider.getIsServletEnv())

        {

            try

            {

                InitialContext ic = new InitialContext();

                config.nameConfigFile = (String)ic.lookup("java:comp/env/" + nameJndiCtx);

                configFile = new File( config.nameConfigFile );

            }

            catch (NamingException e)

            {

                log.error("Error get value from JDNI context", e);

                throw new ConfigException( e.toString() );

            }

        }

        else

        {



            String defURL = null;



            if (PropertiesProvider.getConfigPath() == null)

            {

                String es = "Config path not resolved";

                log.fatal(es);

                throw new IllegalStateException(es);

            }



            if (log.isDebugEnabled())

                log.debug("#15.100");



            defURL = PropertiesProvider.getConfigPath() +

                (PropertiesProvider.getConfigPath().endsWith(File.separator) ? "" : File.separator) +

                nameConfigFile_;



            if (log.isDebugEnabled())

                log.debug("#15.101" + defURL);



            config.nameConfigFile = defURL;



            configFile = new File( defURL );



        }



        if (!configFile.exists())

        {

            String errorString = "Config file '" + configFile.getName() + "' not found";

            log.error(errorString);

            throw new IllegalArgumentException(errorString);

        }





        if (log.isDebugEnabled())

            log.debug("Start unmarshalling file " + nameConfigFile_);



        try

        {



            InputSource inSrc = new InputSource(new FileInputStream(configFile));

            config.configObject = Unmarshaller.unmarshal(configClass, inSrc);

        }

        catch (Exception e)

        {

            log.fatal("Exception while unmarshalling config file ", e);

            throw new ConfigException( e.toString() );

        }



        return config;

    }

}

