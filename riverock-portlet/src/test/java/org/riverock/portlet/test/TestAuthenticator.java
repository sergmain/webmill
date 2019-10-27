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
package org.riverock.portlet.test;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Mar 21, 2003
 * Time: 11:52:05 AM
 *
 * $Id: TestAuthenticator.java 1145 2006-12-18 18:50:57Z serg_main $
 */
public class TestAuthenticator extends Authenticator {

    public TestAuthenticator() {
    }

    public PasswordAuthentication getPasswordAuthentication() {
        char[] pass = {'1', '2', '3'};
        return new PasswordAuthentication("mill", pass);
    }

}
