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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;

/**
 * Simple class loader to illustrate custom Class
 * loading with new delegation model in Java 1.2.
 */
public class Simple1_2ClassLoader extends ClassLoader {

    /**

     http://www.javaworld.com/javaforums/showflat.php?Cat=&Board=TheoryPractice&Number=15263&page=12&view=collapsed&sb=5&o=&fpart=1
     http://sourceforge.net/projects/sadun-util
     http://forum.java.sun.com/thread.jspa?threadID=752623
     http://forum.java.sun.com/thread.jspa?threadID=762571

     */

    private String classPath = null;
    private URLClassLoader cl = null;

    private Manifest manifest;

    /**
     * Provide delegation constructor
     */
    public Simple1_2ClassLoader(ClassLoader parent, String classPath) {
        super(parent);
        this.classPath = classPath;
        init();
    }

    /**
     * Same old ClassLoader constructor
     */
    public Simple1_2ClassLoader(String classPath) {
        super();
        this.classPath = classPath;
        init();
    }

    // **************************************************************************
    // Initialize the ClassLoader by loading the Manifest, if there is one
    // The Manifest contains the package versioning information
    // For simplicity, our manifest will be placed in a store directory
    // **************************************************************************
    private void init() {

        File dir = new File(classPath);
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

        this.cl = new URLClassLoader(urls);


/*

        FileInputStream fi = null;

        // Check for a manifest in the store directory
        try {
            fi = new FileInputStream(
                classPath+File.separatorChar+"classes"+File.separatorChar+"META-INF"+File.separatorChar+"MANIFEST.MF"
            );
            manifest = new Manifest(fi);
        }
        catch (Exception e) {
            // No manifest
        }
        finally {
            if (null != fi) {
                try {
                    fi.close();
                }
                catch (Exception e) {
                }
            }
        }
*/

    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return cl.loadClass(name);
    }

    /**
     * This is the method where the task of class loading
     * is delegated to our custom loader.
     *
     * @param name the name of the class
     * @return the resulting <code>Class</code> object
     * @throws ClassNotFoundException if the class could not be found
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        Class clazz = cl.loadClass(name);
        if (clazz!=null) {
            return clazz;
        }
        return super.findClass(name);
/*
        FileInputStream fi = null;

        try {
            System.out.println("Simple1_2ClassLoader finding class: " + name);

            String path = name.replace('.', '/');
            fi = new FileInputStream(classPath+File.separatorChar+ path + ".impl");
            byte[] classBytes = new byte[fi.available()];
            fi.read(classBytes);
            definePackage(name);
            return defineClass(name, classBytes, 0, classBytes.length);

        }
        catch (Exception e) {
            // We could not find the class, so indicate the problem with an exception
            throw new ClassNotFoundException(name);
        }
        finally {
            if (null != fi) {
                try {
                    fi.close();
                }
                catch (Exception e) {
                }
            }
        }
*/
    }

    /**
     * Identify where to load a resource from, resources for
     * this simple ClassLoader are in a directory name "store"
     *
     * @param name the resource name
     * @return URL for resource or null if not found
     */
    protected URL findResource(String name) {
        return cl.findResource(name);
/*
        File searchResource = new File(classPath+File.separatorChar + name);
        URL result = null;

        if (searchResource.exists()) {
            try {
                return searchResource.toURL();
            }
            catch (MalformedURLException mfe) {
            }
        }

        return result;
*/
    }

    /**
     * Used for identifying resources from multiple URLS
     * Since our simple Classloader only has one repository
     * the returned Enumeration contains 0 to 1 items
     *
     * @param name the resource name
     * @return Enumeration of one URL
     */
    protected Enumeration findResources(final String name) throws IOException {
        return cl.findResources(name);
/*

        // Since we only have a single repository we will only have one
        // resource of a particular name, the Enumeration will just return
        // this single URL

        return new Enumeration() {
            URL resource = findResource(name);

            public boolean hasMoreElements() {
                return (resource != null ? true : false);
            }

            public Object nextElement() {
                if (!hasMoreElements()) {
                    throw new NoSuchElementException();
                }
                else {
                    URL result = resource;
                    resource = null;
                    return result;
                }
            }
        };
*/
    }

    /**
     * Minimal package definition
     */
    private void definePackage(String className) {
        // Extract the package name from the class name,
        String pkgName = className;
        int index = className.lastIndexOf('.');
        if (-1 != index) {
            pkgName = className.substring(0, index);
        }

        // Pre-conditions - need a manifest and the package
        // is not previously defined
        if (null == manifest ||
            getPackage(pkgName) != null) {
            return;
        }

        String specTitle,
            specVersion,
            specVendor,
            implTitle,
            implVersion,
            implVendor;

        // Look up the versioning information
        // This should really look for a named attribute
        Attributes attr = manifest.getMainAttributes();

        if (null != attr) {
            specTitle = attr.getValue(Name.SPECIFICATION_TITLE);
            specVersion = attr.getValue(Name.SPECIFICATION_VERSION);
            specVendor = attr.getValue(Name.SPECIFICATION_VENDOR);
            implTitle = attr.getValue(Name.IMPLEMENTATION_TITLE);
            implVersion = attr.getValue(Name.IMPLEMENTATION_VERSION);
            implVendor = attr.getValue(Name.IMPLEMENTATION_VENDOR);

            definePackage(pkgName,
                specTitle,
                specVersion,
                specVendor,
                implTitle,
                implVersion,
                implVendor,
                null); // no sealing for simplicity
        }
    }
}

