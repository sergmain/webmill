package org.riverock.webmill.portal.search;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 17:01:47
 */
public class SearchFactory {
    private final static DirectorySearch DIRECTORY_SEARCH = new DirectorySearchImpl();

    public static DirectorySearch getDirectorySearch() {
        return DIRECTORY_SEARCH;
    }
}
