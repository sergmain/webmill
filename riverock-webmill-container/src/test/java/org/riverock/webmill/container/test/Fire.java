/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.test;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.io.File;
import java.lang.reflect.Method;

/**
 * @author Sergei Maslyukov
 *         Date: 07.09.2006
 *         Time: 21:40:36
 *         <p/>
 *         $Id$
 */
public class Fire {

    public static void main(String[] args) {

        try {

            String path = "c:\\lib\\";

            File dir = new File(path);
            File[] files = dir.listFiles();

            URL[] urls = new URL[files.length];
            for (int i = 0; i < files.length; i++) {
                try {
                    urls[i] = files[i].toURL();
                    System.out.println(urls);
                }
                catch (MalformedURLException e) {
                    System.out.println(e.getMessage());
                }
            }

            URLClassLoader cl = new URLClassLoader(urls);

            Class cls = cl.loadClass("alyxis.volumeserver.VolumeServer");

            Method main = cls.getMethod("main", new Class[] { String[].class });
            main.invoke(null, new Object[] { args });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
