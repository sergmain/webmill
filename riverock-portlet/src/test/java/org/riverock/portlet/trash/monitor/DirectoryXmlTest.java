package org.riverock.portlet.trash.monitor;

import java.io.File;

import org.apache.commons.lang.CharEncoding;

import org.riverock.portlet.manager.monitor.schema.Directory;
import org.riverock.portlet.manager.monitor.ServerMonitorUtils;
import org.riverock.common.tools.StringTools;
import org.riverock.common.tools.XmlTools;

/**
 * User: SMaslyukov
 * Date: 15.05.2007
 * Time: 19:56:05
 */
public class DirectoryXmlTest {

    public static void main(String[] args) throws Exception {
        long startMills = System.currentTimeMillis();

        File f = new File(args.length==0?File.separator:args[0]);
        Directory d = ServerMonitorUtils.getDirectories(f);
        byte[] bytes = XmlTools.getXml(d, "Directory", CharEncoding.UTF_8);
        System.out.println("xml = " + new String(bytes) );
        System.out.println("Processed for "+ (System.currentTimeMillis()-startMills)/1000+" seconds.");
    }

    private static void print(Directory d, int level) {
        if (d==null) {
            return;
        }
        for (Directory directoryBean : d.getSubDirectory()) {
            String s = new StringBuilder(directoryBean.getName())
                .append(", size: ")
                .append(directoryBean.getSize())
                .append(" total sub: ")
                .append(directoryBean.getSizeSubDirectory())
                .toString();
            System.out.println(StringTools.appendString(s, ' ', s.length()+level*2, true));
            print(directoryBean, level+1);
        }
    }


}
