/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.common.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.time.DateFormatUtils;

import org.riverock.common.annotation.schema.transfer.TransferFileConfigType;
import org.riverock.common.annotation.schema.transfer.TransferFileContentType;
import org.riverock.common.annotation.schema.transfer.TransferFileListType;
import org.riverock.common.tools.MainTools;

public class SignFile {
    private static final Object syncDebug = new Object();

    private static int MAXFILES = 300;

    private static TransferFileConfigType tfc = null;
    private static TransferFileListType fileList = new TransferFileListType();

    private static void checkClass(String name) {
        try {
            if (Class.forName(name) != null) {
                System.out.println("class " + name + " present");
            }
            else {
                System.out.println("class " + name + " NOT present");
            }
        }
        catch (Exception e) {
            System.out.println("Exception create class " + name + " -  " + e.getMessage());
        }
    }

    public static String getFileContent(String fileName) throws Exception {
        File file_ = new File(fileName);

        InputStreamReader in = new InputStreamReader(new FileInputStream(file_));

        String buff = "";
        int ch;
        int sizeBuff = 1000;
        byte buffBytes[] = new byte[sizeBuff];
        int i = 0;
        while ((ch = in.read()) != -1) {
            buffBytes[i++] = (byte) ch;

            if (i == sizeBuff) {
                buff += new String(buffBytes, 0, i);
                i = 0;
            }
        }
        if (i != 0)
            buff += new String(buffBytes, 0, i);


        in.close();

        return buff;
    }

    public static byte[] getFileBytes(InputStream in, int sizeBuff)
        throws Exception {

        int ch;
        byte buffBytes[] = new byte[sizeBuff];
        int i = 0;
        while (((ch = in.read()) != -1) && (i < sizeBuff))
            buffBytes[i++] = (byte) ch;

        in.close();
        byte retBytes[] = new byte[i];
        for (int j = 0; j < i; j++)
            retBytes[j] = buffBytes[j];

        return retBytes;
    }

    public static TransferFileContentType encryptFile(File fileToEncrypt, String path)
        throws Exception {
        TransferFileContentType tf = new TransferFileContentType();

        File tempFile = File.createTempFile("temp_", ".sign");

        byte[] bytes = getFileBytes(
            new FileInputStream(fileToEncrypt), 10000000
        );

        byte[] bytesToEncrypt = null;
        if (Boolean.TRUE.equals(tfc.isIsGzip())) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
            gzip.write(bytes);
            gzip.flush();
            gzip.close();

            bytesToEncrypt = byteStream.toByteArray();

//            String tempDir = GenericConfig.getGenericDebugDir() + File.separatorChar;

//            log.debug("write plain data in file " + tempDir + "file.gzip.1");
//            log.debug( writeToFile(tempDir + "file.gzip.1", bytes) );

//            log.debug("write GZiped data in file " + tempDir + "file.gzip.2");
//            log.debug( writeToFile(tempDir + "file.gzip.2", bytesToEncrypt) );

/*
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(bytesToEncrypt);
            if (log.isDebugEnabled())
            {
                synchronized(syncDebug)
                {
                    GZIPInputStream gzip1 = new GZIPInputStream(byteInStream);
                    log.debug("write UnGZiped data in file " + tempDir + "file.gzip.3");
                    log.debug( writeToFile(tempDir + "file.gzip.3", getFileBytes(gzip1, 10000000)) );
                }
            }
*/
        }
        else {
            bytesToEncrypt = bytes;
        }

        EncryptFileSignature encFile = new EncryptFileSignature();

        String keystoreFile = null;
//        keystoreFile = GenericConfig.getConfig().getSecurityKeyStorage().getPath();
        if (keystoreFile == null) {
            throw new IllegalStateException("need implement");
        }

        encFile.initParam(
            bytesToEncrypt,
            tempFile,
            tfc.getCertificateFile(),
            keystoreFile,
            tfc.getPasswordKS(),
            tfc.getAliasKS()
        );
        encFile.encryptFileSIG();

//        writeToFile(GenericConfig.getGenericlTempDir() + File.separatorChar + "file.gzip.sign",
//                getFileBytes(
//                        new FileInputStream(tempFile), 10000000));

/*
        Base64 base64 = new Base64();
        tf.setFileContent64(
            new String(
                base64.encode(
                    getFileBytes(new FileInputStream(tempFile), 10000000))
            )
        );
*/
        tf.setFileName(path + File.separatorChar + fileToEncrypt.getName());

        String s = tempFile.getAbsolutePath();
        tempFile = null;
        MainTools.deleteFile(s);
        return tf;
    }

    private static void dir(String parent)
        throws Exception {
        File dirName = new File(tfc.getBaseDirectory() + File.separatorChar + parent);
        String fileArr[];
        try {
            fileArr = dirName.list();
        }
        catch (Exception e) {
            return;
        }
        if (fileArr == null) {
            return;
        }

        for (int i = 0; i < fileArr.length; i++) {
            String s = tfc.getBaseDirectory() + File.separatorChar +
                parent + File.separatorChar +
                fileArr[i];
            File file_ = new File(s);

            if (tfc.getExclude().indexOf(file_.getName()) != -1) {
                continue;
            }

            if (file_.isFile()) {
                fileList.getTransferFileContent().add(encryptFile(file_, parent));
            }
            else if (file_.isDirectory()) {
                dir(parent + File.separatorChar + fileArr[i]);
            }
            file_ = null;
        }
        return;
    }

    public static void main(String args[])
        throws Exception {

//        TransferFileContent tf = new TransferFileContent();
//        Base64 base64 = new Base64();
        org.riverock.common.startup.StartupApplication.init();

        checkClass("org.bouncycastle.jce.provider.BouncyCastleProvider");
        checkClass("org.bouncycastle.jceX509KeyUsage");
        checkClass("org.apache.log4j.Category");
        checkClass("org.exolab.castor.mapping.Mapping");
        checkClass("com.oreilly.servlet.multipart.FilePart");


        Provider prov[] = Security.getProviders();
        for (int i = 0; i < prov.length; i++)
            System.out.println("Prov #" + i + ": " + prov[i].getName() + " v" + prov[i].getVersion());

        Provider provider = Security.getProvider("BC");
        if (provider == null) {
            System.out.println("Provider DB not found");

        }
        else {
//                Security.addProvider(
//            new com.sun.net.ssl.internal.ssl.Provider());
//org.bouncycastle.jce.provider.BouncyCastleProvider

            System.out.println(
                provider.getName() + " v" +
                    provider.getVersion()
            );

            System.out.println("Info " +
                provider.getInfo()
            );
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(TransferFileConfigType.class.getPackage().getName());

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        FileInputStream stream = new FileInputStream(args[0]);
        Source source = new StreamSource(stream);
        tfc = unmarshaller.unmarshal(source, TransferFileConfigType.class).getValue();

        fileList.setDateCreate(
            DateFormatUtils.format(System.currentTimeMillis(), "dd.MM.yyyy HH:mm:ss", TimeZone.getTimeZone("Europe/Moscow"), Locale.ENGLISH)
        );
        fileList.setIsGzip(tfc.isIsGzip());

        for (String processDir : tfc.getDirectory()) {
            dir(processDir);
        }

        String encoding = "UTF-8";

/*
        FileOutputStream fos = new FileOutputStream(tfc.getExportFile());
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setRootElement("TransferFileList");
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        marsh.marshal( fileList );
*/

    }
}
