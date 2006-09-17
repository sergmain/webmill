package org.riverock.webmill.container.test;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.net.MalformedURLException;
import java.io.File;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2006
 * Time: 13:56:20
 * <p/>
 * $Id$
 */
public class CustomClassLoader extends URLClassLoader {

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> aClass;
        try {
            aClass = super.loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
        catch(NoClassDefFoundError e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
        return aClass;    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> aClass;
        try {
            aClass = super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
        catch(NoClassDefFoundError e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw e;
        }
        return aClass;    //To change body of overridden methods use File | Settings | File Templates.
    }

    private CustomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    private CustomClassLoader(URL[] urls) {
        super(urls);
    }

    private CustomClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    public static CustomClassLoader getInstance(String classPath, ClassLoader parent) {
        return new CustomClassLoader(prepareURLs(classPath), parent);
    }

    public static CustomClassLoader getInstance(URL[] urls, ClassLoader parent) {
        return new CustomClassLoader(urls, parent);
    }

    public static CustomClassLoader getInstance(URL[] urls) {
        return new CustomClassLoader(urls);
    }

    public static CustomClassLoader getInstance(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        return new CustomClassLoader(urls, parent, factory);
    }

    private static URL[] prepareURLs(String classPath) {

        File dir = new File(classPath);
        File[] files = dir.listFiles();

        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                urls[i] = files[i].toURL();
                System.out.println(urls[i]);
            }
            catch (MalformedURLException e) {
                System.out.println(e.getMessage());
            }
        }
        return urls;
    }
}