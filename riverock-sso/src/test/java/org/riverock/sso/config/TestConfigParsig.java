package org.riverock.sso.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.riverock.common.config.ConfigObject;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.sso.annotation.schema.config.SsoConfig;
import org.riverock.sso.annotation.schema.config.AuthProvider;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 2:30:35
 */
public class TestConfigParsig extends TestCase {

    private File ssoPath;
    private static final String CONFIG_FILE_XML = "config-file.xml";

    protected void setUp() throws java.lang.Exception {
        System.out.println("Start setUp()");

        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir==null) {
            throw new Exception("Temp dir property not defined in VM");
        }
        File tmpDirFile = new File(tmpDir+File.separatorChar+"sso");
        tmpDirFile.mkdirs();
        assertTrue(tmpDirFile.exists());
        assertTrue(tmpDirFile.isDirectory());
        File tempFile = File.createTempFile("sso", "", tmpDirFile );
        tempFile = convertToDirAndCreate(tempFile);

        this.ssoPath = tempFile;
    }

    public void testParse() throws Exception {


        File temp = new File(ssoPath, CONFIG_FILE_XML);
        FileOutputStream os = new FileOutputStream(temp);
        InputStream is = TestConfigParsig.class.getResourceAsStream("/xml/config/config-sso.xml");
        byte[] bytes = new byte[0x200];
        int count;
        while ((count=is.read(bytes))!=-1) {
            os.write(bytes, 0, count);
        }
        os.flush();
        os.close();
        os=null;
        is.close();
        PropertiesProvider.setConfigPath(ssoPath.getAbsolutePath());

        ConfigObject configObject = ConfigObject.load(
            null , null, CONFIG_FILE_XML, SsoConfig.class
        );

        assertNotNull(configObject);
/*
<SsoConfig xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="C:\sandbox\riverock\riverock-sso\src-schema\xsd\riverock-sso-config.xsd">
	<Auth>
		<AuthProvider isUse="true">
			<ProviderName>Internal Webmill auth provider</ProviderName>
			<ProviderClass>org.riverock.webmill.a3.provider.InternalAuthProvider</ProviderClass>
		</AuthProvider>
	</Auth>
</SsoConfig>
*/

        assertNotNull(configObject.getConfigObject());
        SsoConfig ssoConfig = (SsoConfig)configObject.getConfigObject();
        assertNotNull(ssoConfig.getAuth());
        assertNotNull(ssoConfig.getAuth().getAuthProvider());
        assertEquals(1, ssoConfig.getAuth().getAuthProvider().size());
        AuthProvider provider = ssoConfig.getAuth().getAuthProvider().get(0);
        assertEquals("org.riverock.webmill.a3.provider.InternalAuthProvider", provider.getProviderClass());
        assertEquals("Internal Webmill auth provider", provider.getProviderName());
    }

    private File convertToDirAndCreate(File tempFile) {
        if (tempFile.exists()) {
            tempFile.delete();
        }
        assertTrue(!tempFile.exists());
        tempFile.mkdirs();
        assertTrue(tempFile.exists());
        assertTrue(tempFile.isDirectory());

        return tempFile;
    }
    
}
