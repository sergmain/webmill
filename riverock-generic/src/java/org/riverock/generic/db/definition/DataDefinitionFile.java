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
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.main.CacheFile;
import org.riverock.generic.schema.db.DataDefinitionListType;
import org.riverock.generic.config.GenericConfig;

/**
 * $Id$
 */
public class DataDefinitionFile extends CacheFile
{
    private static Logger log = Logger.getLogger(DataDefinitionFile.class);

    public DataDefinitionListType definitionList = null;
//    private Hashtable definitionHash = null;
//    private Hashtable definitionPrevVersionHash = null;
//    public final static char separator = '-';

//    protected void finalize() throws Throwable
//    {
//        if (definitionHash!=null)
//        {
//            definitionHash.clear();
//            definitionHash = null;
//        }
//        super.finalize();
//    }

/*
    public Enumeration getKeys()
    {
        if (definitionHash==null)
            return null;

        return definitionHash.keys();
    }

    public Hashtable getHash()
    {
        if (definitionHash==null)
            return null;

        return definitionHash;
    }

    public Collection getValues()
    {
        if (definitionHash==null)
            return null;

        return definitionHash.values();
    }

    public int getApplicationCount()
    {
        if (definitionHash==null)
            return 0;

        return definitionHash.size();
    }

    public DataDefinitionType getDataDefinition(String nameDefinition_)
    {
        if (definitionHash == null || nameDefinition_==null)
            return null;

        String search = nameDefinition_;

        if (log.isDebugEnabled())
            log.debug("Get data definition '" + search + "' from file '" + getFile().getName()+"'");

        if ( isUseCache() )
            return (DataDefinitionType)definitionHash.get(search);

        try
        {
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

                return (DataDefinitionType)definitionHash.get(search);
            }
        }
        catch (Exception e)
        {
            log.error("Exception while get module " + search, e);
            return null;
        }

        if (log.isDebugEnabled())
            log.debug("#7.02 file not changed. Get module from cache");

        return (DataDefinitionType)definitionHash.get(search);
    }
*/

    public DataDefinitionFile(File tempFile)
        throws Exception
    {
        super( tempFile, 1000*10 );

        if (log.isDebugEnabled())
            log.debug("Start unmarshalling file " + tempFile);

        try
        {
            if (log.isDebugEnabled())
                log.debug("Unmarshal new file: " + tempFile.getName());

            processFile();
        }
        catch (Exception e)
        {
            log.error("Exception while unmarshalling definition file + " + getFile(), e);
//            if (definitionHash!=null)
//            {
//                definitionHash.clear();
//                definitionHash = null;
//            }
            throw e;
        }
    }

    private static Object syncObj = new Object();
    private void processFile() throws Exception
    {
        try
        {
            InputSource inSrc = new InputSource(new FileInputStream( getFile() ));
            definitionList = (DataDefinitionListType) Unmarshaller.unmarshal(DataDefinitionListType.class, inSrc);
            definitionList.validate();

            if (log.isDebugEnabled())
            {
                synchronized(syncObj)
                {
                    try
                    {
                        FileWriter w = new FileWriter(GenericConfig.getGenericDebugDir() + "definitionList.xml");
                        Marshaller.marshal(definitionList, w);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        catch(Exception e)
        {
            String errorString = "Error processing data definition file";
            log.error( errorString, e);
            throw e;
        }
    }
}
