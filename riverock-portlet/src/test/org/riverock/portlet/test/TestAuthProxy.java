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

 * User: Admin

 * Date: Mar 20, 2003

 * Time: 9:28:58 PM

 *

 * $Id$

 */

package org.riverock.portlet.test;



import java.io.BufferedReader;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.Authenticator;

import java.net.HttpURLConnection;

import java.net.URL;



import junit.framework.TestCase;



public class TestAuthProxy extends TestCase

{

    public TestAuthProxy(){}



    public TestAuthProxy(String testName)

    {

        super(testName);

    }



    public static void main(String[] s)

        throws Exception

    {

        System.setProperty("http.proxyHost", "ser");

        System.setProperty("http.proxyPort", "3128");



        URL url = new URL("http", "me", 80, "/");

        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();

        Authenticator.setDefault( new TestAuthenticator() );

        System.out.println("usingproxy status - "+urlConn.usingProxy());

        System.out.println("Class name - "+urlConn.getClass().getName());

        InputStream in = url.openStream();

        BufferedReader theReader = new BufferedReader(

             new InputStreamReader( url.openStream())

        );





        final int kMaxSizeHTML = 100000;

        char readBuffer[] = new char[kMaxSizeHTML];

        int countRead = theReader.read(readBuffer, 0, kMaxSizeHTML);

        String contentStr = "";

        String tmpStr;

        BufferedWriter wr = new BufferedWriter(

            new FileWriter("c:\\opt1\\auth-proxy.txt")

        );

        while ((countRead!=-1) && (countRead != 0) && (contentStr.length()<kMaxSizeHTML) )

        {

            wr.write( readBuffer, 0, countRead );

            tmpStr = new String(readBuffer, 0, countRead);

            contentStr +=  tmpStr;

            countRead = theReader.read(readBuffer, 0, kMaxSizeHTML );

        }

        wr.flush();

        wr.close();

        wr = null;

    }

}







