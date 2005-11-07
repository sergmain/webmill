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
 * User: serg_main
 * Date: 09.01.2004
 * Time: 21:22:36
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.webmill.as.service;

import org.riverock.webmill.as.ApplicationInterface;
import org.riverock.webmill.schema.appl_server.ResourceRequestType;
import org.riverock.interfaces.sso.a3.AuthSession;


public class SimpleApplicationServerProvider implements ApplicationInterface
{
    public final static String hello = "Hello, world";
    public byte[] processRequest(ResourceRequestType applReq, AuthSession authSession) throws Exception
    {
        return hello.getBytes("utf-8");
    }


}
