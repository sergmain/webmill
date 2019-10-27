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
package org.riverock.common.main;

import java.io.File;
import java.io.FileFilter;

/**
 * $Id: ExtensionFileFilter.java 1197 2007-05-16 16:39:27Z serg_main $
 */
public class ExtensionFileFilter implements FileFilter {

    private String ext[] = null;

    public ExtensionFileFilter(String ext_) {
        if (ext_ != null) {
            this.ext = new String[1];
            this.ext[0] = ext_;
        }
    }

    public ExtensionFileFilter(String ext[]) {
        if (ext != null) {
            this.ext = new String[ext.length];
            System.arraycopy(ext, 0, this.ext, 0, ext.length);
        }
    }

    public boolean accept(File file) {
        if (file == null || file.isDirectory()) {
            return false;
        }

        if (this.ext != null) {
            for (String anExt : this.ext) {
                if (file.getName().toLowerCase().endsWith(anExt)) {
                    return true;
                }
            }
        }
        return false;
    }
}

