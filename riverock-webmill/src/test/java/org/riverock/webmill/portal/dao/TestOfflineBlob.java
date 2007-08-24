package org.riverock.webmill.portal.dao;

import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 17:51:45
 */
public class TestOfflineBlob extends TestCase {

    public void testBlobConstructor() throws SQLException {
        OfflineBlob offlineBlob = new OfflineBlob("20 20 20");
        byte[] bytes = offlineBlob.getBytes( 1, (int)offlineBlob.length());
        assertNotNull(bytes);
        for (byte aByte : bytes) {
            assertEquals(0x20, aByte);
        }
    }
}
