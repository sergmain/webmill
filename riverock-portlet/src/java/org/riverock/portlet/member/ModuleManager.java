/*

 * org.riverock.portlet -- Portlet Library

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

package org.riverock.portlet.member;



import org.riverock.portlet.schema.member.ContentType;

import org.riverock.portlet.schema.member.ModuleType;

import org.riverock.portlet.schema.member.SqlCacheType;

import org.riverock.portlet.schema.member.QueryAreaType;

import org.riverock.portlet.schema.member.types.ContentTypeActionType;

import org.riverock.portlet.main.Constants;

import org.riverock.generic.main.CacheDirectory;

import org.riverock.generic.main.ExtensionFileFilter;

import org.riverock.generic.main.CacheFile;

import org.riverock.common.config.PropertiesProvider;

import org.riverock.common.config.ConfigException;

import org.riverock.webmill.config.WebmillConfig;



import org.apache.log4j.Logger;



import java.io.File;

import java.io.FileFilter;

import java.io.FileNotFoundException;



public class ModuleManager

{

    private static Logger cat = Logger.getLogger( "org.riverock.portlet.member.ModuleManager" );



    private static FileFilter memberFilter = new ExtensionFileFilter(".xml");



    private static CacheDirectory mainDir = null;

    private static CacheDirectory userDir = null;



    private static MemberFile mainMemberFile[] = null;

    private static MemberFile userMemberFile[] = null;



    private static boolean isUserDirectoryExists = true;



    public static MemberFile[] getMemberFileArray()

    {

        MemberFile[] temp = new MemberFile[getCountFile()];



        int idx = 0;

        if (mainMemberFile!=null)

        {

            for (int i=0; i<mainMemberFile.length; i++)

            {

                if (mainMemberFile[i]!= null)

                    temp[idx++] = mainMemberFile[i];

            }

        }

        if (userMemberFile!=null)

        {

            for (int i=0; i<userMemberFile.length; i++)

            {

                if (userMemberFile[i]!= null)

                    temp[idx++] = userMemberFile[i];

            }

        }



        return temp;

    }



    public static int getCountFile()

    {

        return

            (mainMemberFile==null?0:mainMemberFile.length)+

            (userMemberFile==null?0:userMemberFile.length);

    }



    public static int getCountModule()

    {

        int count = 0;

        if (mainMemberFile!=null)

        {

            for (int i=0; i<mainMemberFile.length; i++)

            {

                if (mainMemberFile[i]!=null)

                    count += mainMemberFile[i].getMemberModuleCount();

            }

        }

        if (userMemberFile!=null)

        {

            for (int i=0; i<userMemberFile.length; i++)

            {

                if (userMemberFile[i]!=null)

                    count += userMemberFile[i].getMemberModuleCount();

            }

        }



        return count;

    }





    public static ContentType getContent(String moduleName, int actionType)

            throws Exception

    {

        ModuleType mod = getModule(moduleName);

        if (mod == null)

            return null;



        for (int i = 0; i < mod.getContentCount(); i++)

        {

            ContentType cnt = mod.getContent(i);

            if (cnt.getAction().getType() == actionType)

                return cnt;

        }

        return null;

    }



    private static String getCustomDir()

        throws ConfigException

    {

        String dir = null;

        dir = WebmillConfig.getCustomMemberDir();

/*

        try {

            InitialContext ic = new InitialContext();

            dir = (String) ic.lookup("java:comp/env/"+ Constants.JNDI_CUSTOM_MEMBER_DIR );

            if (cat.isDebugEnabled())

                cat.debug("ConfigFile - "+ dir);

        }

        catch (NamingException e)

        {

            dir = null;

            cat.warn("Custom member dir enviropment in JNDI not defined", e);

        }

*/

        return dir;

    }



    public static void init()

        throws Exception

    {

        if (mainDir==null)

            mainDir = new CacheDirectory(

                PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_MEMBER_DIR,

                memberFilter

            );



        if (mainMemberFile == null || !mainDir.isUseCache())

        {

            if (mainDir.isNeedReload())

                mainDir = new CacheDirectory(

                    PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_MEMBER_DIR,

                    memberFilter

                );



            if (cat.isDebugEnabled())

                cat.debug("#2.001 read list file");



            mainDir.processDirectory();



            if (cat.isDebugEnabled())

                cat.debug("#2.003 array of files - " + mainDir.getFileArray());



            CacheFile cacheFile[] = mainDir.getFileArray();

            mainMemberFile = null;

            mainMemberFile = new MemberFile[cacheFile.length];

            for (int i=0; i<mainDir.getFileArray().length; i++)

            {

                mainMemberFile[i] = new MemberFile( cacheFile[i].getFile());

            }

        }

        // init Custom member module list

        if (isUserDirectoryExists)

        {

            if (userDir==null)

            {

                String customMemberDir = getCustomDir();

                if (customMemberDir!=null && customMemberDir.length()!=0)

                {

                    try

                    {

                        userDir = new CacheDirectory(

                            customMemberDir,

                            memberFilter,

                            1000*30 // сканировать директорий каждые 30 секунд

                        );

                    }

                    catch( FileNotFoundException e )

                    {

                        isUserDirectoryExists = false;

                        return;

                    }

                }

                else

                    return;

            }



            if (userMemberFile == null || !userDir.isUseCache())

            {

                if (userDir.isNeedReload())

                {

                    String customMemberDir = getCustomDir();

                    if (customMemberDir!=null && customMemberDir.length()!=0)

                    {

                        userDir = new CacheDirectory(

                            customMemberDir,

                            memberFilter,

                            1000*30 // сканировать директорий каждые 30 секунд

                        );

                    }

                }



                if (cat.isDebugEnabled())

                    cat.debug("#2.001 read list file");



                userDir.processDirectory();



                if (cat.isDebugEnabled())

                    cat.debug("#2.003 array of files - " + userDir.getFileArray());



                CacheFile cacheFile[] = userDir.getFileArray();

                userMemberFile = null;

                userMemberFile = new MemberFile[cacheFile.length];

                for (int i=0; i<userDir.getFileArray().length; i++)

                {

                    userMemberFile[i] = new MemberFile( cacheFile[i].getFile());

                }

            }

        }

    }



    public static ModuleType getModule(String moduleName )

            throws Exception

    {

        init();

        for (int k=0; k < mainMemberFile.length; k++)

        {

            if (mainMemberFile[k]==null)

                continue;



            MemberFile mf = mainMemberFile[k];

            ModuleType mod = mf.getModule(moduleName);

            if (mod != null)

            {

                if (cat.isDebugEnabled())

                    cat.debug("Module '" + moduleName + "' is found in main directory");



                for (int i=0; i<mod.getContentCount(); i++)

                {

                    ContentType content = mod.getContent(i);

                    QueryAreaType qa = content.getQueryArea();

                    if (qa.getSqlCache()==null)

                        qa.setSqlCache( new SqlCacheType() );



                    if (Boolean.FALSE.equals(qa.getSqlCache().getIsInit()) )

                    {

                        switch (content.getAction().getType())

                        {

                            case ContentTypeActionType.INDEX_TYPE:

                                break;

                            case ContentTypeActionType.INSERT_TYPE:

                                break;

                            case ContentTypeActionType.CHANGE_TYPE:

                                break;

                            case ContentTypeActionType.DELETE_TYPE:

                                break;

                            default:

                                cat.error("unknown type of content - "+content.getAction().toString());

                        }

                    }

                }

                return mod;

            }

        }



        if (cat.isDebugEnabled())

            cat.debug("Module '" + moduleName + "' in main directory not found");



        if (userMemberFile!=null)

        {

            for (int k=0; k < userMemberFile.length; k++)

            {

                if (userMemberFile[k]==null)

                    continue;



                MemberFile mf = userMemberFile[k];

                ModuleType mod = mf.getModule( moduleName );

                if (mod != null)

                {

                    if (cat.isDebugEnabled())

                        cat.debug("Module '" + moduleName + "' is found in custom directory");



                    return mod;

                }

            }

        }



        if (cat.isDebugEnabled())

            cat.debug("Module '" + moduleName + "' not found");



        return null;

    }

}