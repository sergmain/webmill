/*
 * org.riverock.sso - Single Sign On implementation
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
package org.riverock.sso.test;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 * User: Admin
 * Date: Mar 20, 2003
 * Time: 9:28:58 PM
 * <p/>
 * $Id: TestAuthProxy.java 1144 2006-12-16 20:30:13Z serg_main $
 */
public class TestAuthProxy /* extends TestCase */{

    private static class TestAuthenticator extends Authenticator {

        public TestAuthenticator() {
        }

        public PasswordAuthentication getPasswordAuthentication() {
            char[] pass = {'1', '2', '3'};
            return new PasswordAuthentication("mill", pass);
        }

    }

    public TestAuthProxy() {
    }

    /*
        public TestAuthProxy(String testName)
        {
            super(testName);
        }

    */
    public static void main(String[] s)
        throws Exception {
        System.setProperty("http.proxyHost", "ser");
        System.setProperty("http.proxyPort", "3128");

        URL url = new URL("http", "me", 80, "/");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        Authenticator.setDefault(new TestAuthenticator());
        System.out.println("usingproxy status - " + urlConn.usingProxy());
        System.out.println("Class name - " + urlConn.getClass().getName());
        InputStream in = url.openStream();
        BufferedReader theReader = new BufferedReader(
            new InputStreamReader(url.openStream())
        );


        final int kMaxSizeHTML = 100000;
        char readBuffer[] = new char[kMaxSizeHTML];
        int countRead = theReader.read(readBuffer, 0, kMaxSizeHTML);
        String contentStr = "";
        String tmpStr;
        BufferedWriter wr = new BufferedWriter(
            new FileWriter("c:\\opt1\\auth-proxy.txt")
        );
        while ((countRead != -1) && (countRead != 0) && (contentStr.length() < kMaxSizeHTML)) {
            wr.write(readBuffer, 0, countRead);
            tmpStr = new String(readBuffer, 0, countRead);
            contentStr += tmpStr;
            countRead = theReader.read(readBuffer, 0, kMaxSizeHTML);
        }
        wr.flush();
        wr.close();
        wr = null;
    }
}



