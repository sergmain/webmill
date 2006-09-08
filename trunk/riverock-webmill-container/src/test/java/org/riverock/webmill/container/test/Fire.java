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
