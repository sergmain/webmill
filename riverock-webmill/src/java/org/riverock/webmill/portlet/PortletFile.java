/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * $Id$
 */
package org.riverock.webmill.portlet;

import org.riverock.generic.main.CacheFile;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.PortletAppType;
import org.riverock.generic.tools.XmlTools;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.webmill.config.WebmillConfig;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Enumeration;

public class PortletFile extends CacheFile
{
    private static Logger log = Logger.getLogger("org.riverock.webmill.portlet.PortletFile");

//    private PortletListType portlet = null;
    private Hashtable portletHash = null; // PortletDescriptionType

    protected void finalize() throws Throwable
    {
        if (portletHash!=null)
        {
            portletHash.clear();
            portletHash = null;
        }
        super.finalize();
    }

    public Enumeration getPortletDescriptions()
    {
        if (portletHash==null)
            return null;

        return portletHash.elements();
    }

    public int getPortletDescriptionCount()
    {
        if (portletHash==null)
            return 0;

        return portletHash.size();
    }

    private static Object syncObjPortlet = new Object();
    public PortletType getPortletDescription(String namePortlet_)
        throws FileManagerException
    {
        if (log.isDebugEnabled())
            log.debug("portletHash " + portletHash + ", namePortlet "+namePortlet_);

        try
        {
            if (portletHash == null || namePortlet_==null)
            {
                log.warn("Try getPortletDescription with portletHash "+portletHash+" and namePortlet_ "+namePortlet_);
                return null;
            }

            boolean isUseCache_  =  isUseCache();
            if (log.isDebugEnabled())
            {
                log.debug("Get portlet description '" + namePortlet_ + "' from file '" + getFile() );
                log.debug("isUseCache " + isUseCache_ );
            }

            if ( isUseCache_ )
            {
                PortletType p = (PortletType)portletHash.get(namePortlet_);

                if (log.isDebugEnabled())
                {
                    log.debug("Portlet " + p );
                    if (p==null)
                    {
                        synchronized(syncObjPortlet)
                        {

                            Enumeration en = portletHash.keys();
                            XmlTools.writeToFile(en, WebmillConfig.getWebmillDebugDir()+"portlet-enumeration.xml");
                            for (int i=0; en.hasMoreElements() ; i++)
                            {
                                log.debug("key #"+i+" "+en.nextElement());
                            }
                            XmlTools.writeToFile(portletHash, WebmillConfig.getWebmillDebugDir()+"portlet-hash.xml");
                        }
                    }
                }

                return p;
            }

            if (log.isDebugEnabled())
            {
                log.debug("#7.00.01 " + getFile() );
                log.debug("#7.01.00 check was file modified");
            }

            if ( isNeedReload() )
            {

                if (log.isInfoEnabled())
                    log.info("Redeploy member file " + getFile().getName());

                setLastModified();

                processFile();

                if (log.isDebugEnabled())
                    log.debug("#7.01.02 Unmarshal done");

                return (PortletType)portletHash.get(namePortlet_);
            }
            if (log.isDebugEnabled())
                log.debug("#7.02 file not changed. Get module from cache");

            return (PortletType)portletHash.get(namePortlet_);
        }
        catch (Exception e)
        {
            log.error("Exception while get module " + namePortlet_, e);
            return null;
        }
        catch (Error e)
        {
            log.error("Error while get module " + namePortlet_, e);
            return null;
        }

    }

    public PortletFile(File tempFile)
            throws FileManagerException, FileNotFoundException
    {
        super( tempFile, 1000*10 );
        try
        {

        if (log.isDebugEnabled())
            log.debug("Start unmarshalling file " + tempFile);

            if (log.isDebugEnabled())
                log.debug("Unmarshal new portlet file: " + tempFile );

            processFile();

            if (log.isDebugEnabled())
                log.debug("Unmarshal new portlet file is done.");
        }
        catch (Exception e)
        {
            String errorString = "Exception while unmarshalling portlet file + " + getFile();
            log.error(errorString, e);
            if (portletHash!=null)
            {
                portletHash.clear();
                portletHash = null;
            }
            throw new FileManagerException(errorString, e);
        }
    }

    private static Object syncObj = new Object();
    private void processFile() throws FileManagerException {

        try {
            InputSource inSrc = new InputSource(new FileInputStream( getFile() ));
            PortletAppType portletApp =
                (PortletAppType) Unmarshaller.unmarshal(PortletAppType.class, inSrc);

//            portletApp.validate();

            portletHash = new Hashtable();
            for (int i=0; i<portletApp.getPortletCount(); i++)
            {
                PortletType desc = portletApp.getPortlet(i);
                portletHash.put( desc.getPortletName().getContent(), desc);
            }

            if (log.isDebugEnabled())
            {
                synchronized(syncObj)
                {
                    try
                    {
                        FileWriter w = new FileWriter(WebmillConfig.getWebmillDebugDir() + "test-webmill-portlets.xml");
                        Marshaller.marshal(portletApp, w);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        catch(Exception e)
        {
            String errorString = "Error processing portlet file "+getFile();
            log.error( errorString, e);
            throw new FileManagerException(errorString, e);
        }
    }
}
