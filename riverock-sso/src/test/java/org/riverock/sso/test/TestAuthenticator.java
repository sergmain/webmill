/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.sso.test;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.log4j.Logger;

/**
 * Author: mill
 * Date: Mar 21, 2003
 * Time: 11:52:05 AM
 *
 * $Id$
 */
public class TestAuthenticator extends Authenticator
{
    private static Logger cat = Logger.getLogger( "org.riverock.test.TestAuthenticator" );

    public TestAuthenticator()
    {
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        char[] pass = { 'v', 'b', 'k', 'k', 'g', 'f', 'h', 'j', 'k', 'm' };
        return new PasswordAuthentication("mill", pass);
    }

}
