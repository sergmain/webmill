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

package org.riverock.generic.main;



import java.io.File;

import java.io.FileFilter;

import java.io.FileNotFoundException;



import org.riverock.common.config.ConfigException;

import org.riverock.generic.exception.GenericException;



import org.apache.log4j.Logger;



public class CacheDirectory

{

    private static Logger log = Logger.getLogger("org.riverock.generic.main.CacheDirectory");



    private long lastAccessTime = -1;

    private long delayPeriod = 1000 * 5;



    private File currentDir = null;

    private CacheFile[] files = null;

    private FileFilter fileFilter = null;



    public String toString()

    {

        return "CacheDirectory=[isUseCache: " + isUseCache() + "; currentDir: " + currentDir + "; files: " + files + "; fileFilter: " + fileFilter + "]";

    }



    public CacheFile[] getFileArray()

    {

        return files;

    }



    public boolean isNeedReload()

        throws GenericException

    {

        try

        {

            if (currentDir == null)

                return true;



            File[] currentFiles = currentDir.listFiles(fileFilter);



            if (files == null)

                return false;



            if (files.length != currentFiles.length)

                return true;



            for (int i = 0; i < files.length; i++)

            {

                File temp = files[i].getFile();

                boolean isNeedReload = true;

                for (int j = 0; j < currentFiles.length; j++)

                {

                    File currFile = currentFiles[j];

                    if (temp.getName().equals(currFile.getName()) &&

                        temp.lastModified() == currFile.lastModified()

                    )

                    {

                        if (log.isDebugEnabled())

                            log.debug("File " + temp.getName() + " was changed. Return status is true");



                        isNeedReload = false;

                    }

                }

                if (isNeedReload)

                    return true;

            }

            return false;

        }

        catch (Exception e)

        {

            log.error("Exception in isNeedReload()", e);

            throw new GenericException(e.toString());

        }

        catch (Error e)

        {

            log.error("Error in isNeedReload()", e);

            throw new GenericException(e.toString());

        }



    }



    public boolean isUseCache()

    {

        if (System.currentTimeMillis() - lastAccessTime <= delayPeriod)

        {

            if (log.isDebugEnabled())

                log.debug("#7.9.0 Use optimistic cached file");



            return true;

        }



        lastAccessTime = System.currentTimeMillis();

        return false;

    }



    public void processDirectory()

        throws ConfigException

    {

        try

        {

            if (currentDir == null)

                return;



            File currentList[] = currentDir.listFiles(fileFilter);



            if (log.isDebugEnabled())

                log.debug("currentList " + currentList);



            if (currentList == null)

                return;



            if (log.isDebugEnabled())

                log.debug("#2.003 found " + currentList.length + " files");



            files = new CacheFile[currentList.length];

            if (log.isDebugEnabled())

                log.debug("#1.004 " + currentList.length);



            for (int i = 0; i < currentList.length; i++)

                files[i] = new CacheFile(currentList[i]);

        }

        catch (Exception e)

        {

            String errorString = "error processing directory";

            log.error(errorString, e);

            throw new ConfigException(e.toString());

        }

    }



    public CacheDirectory(String fileName, FileFilter filter)

        throws FileNotFoundException, ConfigException

    {

        this(fileName, filter, 1000 * 10);

    }



    public CacheDirectory(File dir, FileFilter filter)

        throws FileNotFoundException, ConfigException

    {

        this(dir, filter, 1000 * 10);

    }



    public CacheDirectory(String dirName, FileFilter filter, long delayPeriod_)

        throws FileNotFoundException, ConfigException

    {

        this(new File(dirName), filter, delayPeriod_);

    }



    public CacheDirectory(File dir, FileFilter filter, long delayPeriod_)

        throws FileNotFoundException, ConfigException

    {

        if (dir == null)

        {

            log.warn("Call CacheDirectory constructor with dir==null");

            return;

        }

        fileFilter = filter;

        currentDir = dir;

        if (currentDir == null)

        {

            String errorString = "Error create object File for directory " + currentDir;

            log.error(errorString);

            return;

        }



        if (!currentDir.exists())

        {

            String errorString = "Directory '" + currentDir + "' not exists";

            log.warn(errorString);

            throw new FileNotFoundException( errorString );

        }



        if (!currentDir.isDirectory())

        {

            String errorString = "Path '" + currentDir + "' is not directory";

            log.warn(errorString);

            throw new FileNotFoundException( errorString );

        }



        delayPeriod = delayPeriod_;

        lastAccessTime = System.currentTimeMillis();



        processDirectory();

    }

}