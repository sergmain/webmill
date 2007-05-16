package org.riverock.portlet.manager.monitor;

import java.io.File;

import org.riverock.portlet.manager.monitor.schema.Directory;

/**
 * User: SMaslyukov
 * Date: 15.05.2007
 * Time: 19:29:30
 */
public class ServerMonitorUtils {

    public static Directory getDirectories(File f) {
        if (!f.exists()) {
            return null;
        }
        if (!f.isDirectory()) {
            return null;
        }

        Directory d;
        if (f.getName()!=null) {
            d = new Directory();
            d.setName(f.getName());
        }
        else {
            d = new Directory();
            d.setName(f.getPath());
        }

        // Loop thru files and directories in this path
        File[] files = f.listFiles();
        if (files==null) {
            return d;
        }
        long totalSize = 0;
        long totalSizeSubDir = 0;
        for (File file : files) {
            if (file.isFile()) {
                totalSize += file.length();
            }
            else if (file.isDirectory()) {
                Directory subDir = getDirectories(file);
                if (subDir != null) {
                    d.getSubDirectory().add(subDir);
                    totalSizeSubDir += subDir.getSizeSubDirectory();
                }
            }
        }
        d.setSizeSubDirectory(totalSizeSubDir + totalSize);
        d.setSize(totalSize);
        return d;
    }
}
