/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.common.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class MainTools {

    public static void putKey(final Map<String, Object> map, final String key, final Object value) {
        Object obj = map.get(key);
        if (obj == null) {
            map.put(key, value);
            return;
        }

        if (obj instanceof List) {
            if (value instanceof List)
                ((List) obj).addAll((List) value);
            else
                ((List) obj).add(value);
        }
        else {
            List v = new LinkedList();
            v.add(obj);

            if (value instanceof List)
                v.addAll((List) value);
            else
                v.add(value);

            map.remove(key);
            map.put(key, v);
        }
    }

    public static boolean compare(final byte[] array1, final byte[] array2) {
        if (array1 == null && array2 == null)
            return true;

        if (array1 == null || array2 == null)
            return false;

        if (array1.length != array2.length) {
            System.out.println("diff length, array1 " + array1.length + ", array2 " + array2.length);
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                System.out.println("diff byte at " + i);
                return false;
            }
        }
        return true;
    }

    public static int indexOf(final byte bytes[], final byte searchByte) {
        if (bytes == null || bytes.length == 0)
            return -1;

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == searchByte)
                return i;
        }
        return -1;
    }

    public static byte[] getBytes(final byte bytes[], final int offset) {
        if (bytes == null || bytes.length < offset)
            return null;

        byte b[] = new byte[bytes.length - offset];
        for (int i = 0; i < b.length; i++)
            b[i] = bytes[offset + i];

        return b;
    }

    public static String writeToFile(final String full_file_name, final byte bytes[])
        throws Exception {

        File file_ = new File(full_file_name);
        file_.delete();
        file_ = null;

        FileOutputStream out = new FileOutputStream(full_file_name, true);

        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();

        out = null;

        return full_file_name + " создан успешно";
    }

    public static boolean deleteFile(final String fileName) {
        return deleteFile(new File(fileName));
    }

    public static boolean deleteFile(final File file_) {
        return file_ == null || !file_.exists() || file_.delete();

    }

    public static Object createCustomObject(final String s)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (s == null) {
            return null;
        }

        if (Class.forName(s, true, classLoader) == null) {
            throw new ClassNotFoundException("Error create class for name " + s);
        }

        return ((Class) Class.forName(s, true, classLoader)).newInstance();
    }
}
