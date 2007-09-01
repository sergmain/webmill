package org.riverock.webmill.portal.static_content;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.utils.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 01.09.2007
 * Time: 20:53:03
 */
public class StaticContent {
    private final static Logger log = Logger.getLogger( StaticContent.class );

    private static final String[] staticWithPattern = initStaticPattern();

    private static final String[] staticConcrete = initStaticConcrete();
    private static final String STATIC_CONTENT_LIST_TXT = "/org/riverock/webmill/portal/static-content-list.txt";

    public static boolean isStaticContent(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String realPath) {
        String uri = httpRequest.getRequestURI();
        if (isStaticContent(uri) ) {
            if (log.isDebugEnabled()) {
                log.debug("Process static content for uri " + uri);
            }
            File realPathFile = new File(realPath);
            if (!realPathFile.exists()) {
                throw new PortalException("Real path not exists, " + realPathFile.getAbsolutePath());
            }

            if (uri.charAt(0)=='/') {
                uri = uri.substring(1);
            }
            File file = new File(realPathFile, uri);
            if (!file.exists()) {
                throw new PortalException("URI not exists, " + file.getAbsolutePath());
            }
            String contentType = null;
            String fileName = file.getName().toLowerCase();

            // TODO add config for determinate content type
            if (fileName.endsWith(".css")) {
                contentType = "text/css";
            }
            else if (fileName.endsWith(".js")) {
                contentType = "text/javascript";
            }
            else if (fileName.endsWith(".txt")) {
                contentType = "text/plain";
            }

            if (log.isDebugEnabled()) {
                log.debug("content type: " + contentType) ;
                log.debug("file: " + file.getAbsolutePath()) ;
            }

            try {
                ServletUtils.outputFileToResponse(httpResponse, file, contentType);
                return true;
            }
            catch (Exception e) {
                throw new PortalException("Error forward reuest", e);
            }
        }
        return false;
    }

    private static boolean isStaticContent(String uri) {
        if (log.isDebugEnabled()) {
            log.debug("uri is static? uri: " + uri);
            for (String s : staticConcrete) {
                log.debug("    concrete: " + s);
            }
            for (String s : staticWithPattern) {
                log.debug("    pattern: " + s);
            }
        }
        if (StringUtils.isEmpty(uri)) {
            return false;
        }
        for (String s : staticConcrete) {
            if (uri.equals(s)) {
                return true;
            }
        }
        for (String s : staticWithPattern) {
            if (uri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private static String[] initStaticPattern() {
        List<String> strings = getStrings();
        List<String> result = new ArrayList<String>();
        for (String string : strings) {
            if (string.charAt(string.length()-1)=='*') {
                result.add(string.substring(0, string.length()-2));
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private static String[] initStaticConcrete() {
        List<String> strings = getStrings();
        List<String> result = new ArrayList<String>();
        for (String string : strings) {
            if (string.charAt(string.length()-1)!='*') {
                result.add(string);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private static List<String> getStrings() {
        InputStream inputStream=null;
        try {
            inputStream = StaticContent.class.getResourceAsStream(STATIC_CONTENT_LIST_TXT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> strings = new ArrayList<String>();
            String s;
            while ((s=reader.readLine())!=null) {
                if (StringUtils.isBlank(s)) {
                    continue;
                }
                strings.add(s);
            }
            return strings;
        }
        catch (Exception e) {
            throw new PortalException("Error load static content list", e);
        }
        finally {
            if (inputStream!=null) {
                try {
                    inputStream.close();
                    //noinspection UnusedAssignment
                    inputStream=null;
                }
                catch (IOException e) {
                    log.error("Error close input stream");
                }
            }
        }
    }

}
