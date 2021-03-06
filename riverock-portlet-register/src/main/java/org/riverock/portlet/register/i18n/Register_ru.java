/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.register.i18n;

import org.apache.log4j.Logger;

import org.riverock.common.resource.XmlResourceBundle;

/**
 * @author Serge Maslyukov
 *         Date: 01.12.2005
 *         Time: 15:12:25
 */
public class Register_ru extends XmlResourceBundle {
    private final static Logger log = Logger.getLogger( Register_ru.class );

    public void logError( String msg, Throwable th ) {
        log.error( msg, th );
    }
}