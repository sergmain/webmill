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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.main;

import java.io.File;
import java.io.FileFilter;

/**
 *
 *  $Id$
 *
 */
public class ExtensionFileFilter implements FileFilter
{

    private String ext[] = null;

    public ExtensionFileFilter(String ext_)
    {
        if (ext_!=null)
        {
            ext = new String[1];
            ext[0] = ext_;
        }
    }

    public ExtensionFileFilter(String ext_[])
    {
        if (ext_!=null)
        {
            ext = new String[ ext_.length ];
            for (int i =0; i<ext_.length; i++)
            {
                ext[i] = ext_[i];
            }
        }
    }

    public boolean accept(File file_)
    {
        if (file_==null)
            return false;

        if (file_.isDirectory())
            return false;

        if (ext!= null)
        {
            for (int i=0; i< ext.length; i++)
            {
                if (file_.getName().toLowerCase().endsWith( ext[i] ))
                    return true;
            }
        }
        return false;
    }
}

