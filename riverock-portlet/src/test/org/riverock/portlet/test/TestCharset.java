package org.riverock.portlet.test;

import java.nio.charset.Charset;
import java.util.SortedMap;
import java.util.Map;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 15:57:11
 */
public class TestCharset {
    public static void main(String[] args) {
        SortedMap<String,Charset> map = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : map.entrySet()) {
            System.out.println("entry = " + entry);
        }
    }
}
