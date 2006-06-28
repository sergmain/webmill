package org.riverock.webmill.container.test;

import java.io.File;
import java.io.IOException;

/**
 * @author Sergei Maslyukov
 *         Date: 28.06.2006
 *         Time: 21:19:06
 */
public class DirectoryNameTest {
    public static void main(String[] args) throws IOException {
        String s = "c:\\opt2\\_www\\_me.askmore.info\\blog-new\\";
        File file = new File(s);

        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        System.out.println("file.getCanonicalPath() = " + file.getCanonicalPath());
        System.out.println("file. = " + file.getParent());
    }
}
