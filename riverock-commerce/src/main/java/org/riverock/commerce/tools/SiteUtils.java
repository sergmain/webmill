/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.commerce.tools;

/**
 * User: smaslyukov
 * Date: 09.08.2004
 * Time: 19:58:16
 * $Id: SiteUtils.java 479 2006-01-30 21:48:42Z serg_main $
 */
public final class SiteUtils {

    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    public static String getTempDir() {
        return System.getProperty( JAVA_IO_TMPDIR );
    }

}
