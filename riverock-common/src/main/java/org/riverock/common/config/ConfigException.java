/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 19:46:08
 * @author Serge Maslyukov
 * $Id: ConfigException.java 1040 2006-11-14 13:57:38Z serg_main $
 */

package org.riverock.common.config;

public class ConfigException extends RuntimeException {

    public ConfigException() {
        super();
    }

    public ConfigException(String s) {
        super(s);
    }

    public ConfigException(String s, Throwable cause){
        super(s, cause);
    }
}
