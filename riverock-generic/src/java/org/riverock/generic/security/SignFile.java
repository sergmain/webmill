/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

package org.riverock.generic.security;

import org.riverock.common.tools.Base64;
import org.riverock.common.tools.MainTools;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.utils.DateUtils;
import org.riverock.generic.schema.transfer.TransferFileConfigType;
import org.riverock.generic.schema.transfer.TransferFileContentType;
import org.riverock.generic.schema.transfer.TransferFileListType;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import java.io.*;
import java.security.Provider;
import java.security.Security;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SignFile
{
    private static Object syncDebug = new Object();

    private static Logger log = Logger.getLogger(SignFile.class);
    private static int MAXFILES = 300;

    private static TransferFileConfigType tfc = null;
    private static TransferFileListType fileList = new TransferFileListType();

    private static void checkClass(String name)
    {
        try
        {
            if (Class.forName(name) != null)
            {
                System.out.println("class " + name + " present");
            }
            else
            {
                System.out.println("class " + name + " NOT present");
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception create class " + name + " -  " + e.getMessage());
        }
    }


    private static String writeToFile(String full_file_name, byte bytes[])
            throws Exception
    {

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

    public static String getFileContent(String fileName)
            throws Exception
    {
        File file_ = new File(fileName);

        InputStreamReader in = new InputStreamReader( new FileInputStream(file_) );

        String buff = "";
        int ch;
        int sizeBuff = 1000;
        byte buffBytes[] = new byte[sizeBuff];
        int i = 0;
        while ((ch = in.read()) != -1)
        {
            buffBytes[i++] = (byte) ch;

            if (i == sizeBuff)
            {
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
            throws Exception
    {

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
            throws Exception
    {
        TransferFileContentType tf = new TransferFileContentType();

        File tempFile = File.createTempFile("temp_", ".sign");

        byte[] bytes = getFileBytes(
                new FileInputStream(fileToEncrypt), 10000000
        );

        log.debug("Length to gzip " + bytes.length);

        byte[] bytesToEncrypt = null;
        log.debug("Gzip flag is  " + tfc.getIsGzip());
        if (Boolean.TRUE.equals( tfc.getIsGzip() ))
        {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
            gzip.write(bytes);
            gzip.flush();
            gzip.close();

            bytesToEncrypt = byteStream.toByteArray();

            String tempDir = GenericConfig.getGenericTempDir() + File.separatorChar;

//            log.debug("write plain data in file " + tempDir + "file.gzip.1");
//            log.debug( writeToFile(tempDir + "file.gzip.1", bytes) );

//            log.debug("write GZiped data in file " + tempDir + "file.gzip.2");
//            log.debug( writeToFile(tempDir + "file.gzip.2", bytesToEncrypt) );

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
        }
        else
            bytesToEncrypt = bytes;

        log.debug("Length to encrypt is " + bytesToEncrypt.length);

        EncryptFileSignature encFile = new EncryptFileSignature();

        encFile.initParam(
                bytesToEncrypt,
                tempFile,
                tfc.getCertificateFile(),
                GenericConfig.getConfig().getSecurityKeyStorage().getPath(),
                tfc.getPasswordKS(),
                tfc.getAliasKS()
        );
        encFile.encryptFileSIG();

//        writeToFile(GenericConfig.getGenericlTempDir() + File.separatorChar + "file.gzip.sign",
//                getFileBytes(
//                        new FileInputStream(tempFile), 10000000));

        Base64 base64 = new Base64();
        tf.setFileContent64(
            new String(
                base64.encode(
                    getFileBytes( new FileInputStream(tempFile), 10000000) )
            )
        );
//        tf.fileContent64 = "data".getBytes();
        tf.setFileName( path + File.separatorChar + fileToEncrypt.getName() );

        String s = tempFile.getAbsolutePath();
        tempFile = null;
        MainTools.deleteFile(s);
        return tf;
    }

    private static void dir(String parent)
            throws Exception
    {
        File dirName = new File(tfc.getBaseDirectory() + File.separatorChar + parent);
        String fileArr[] = new String[MAXFILES];
        try
        {
            fileArr = dirName.list();
        }
        catch (Exception e)
        {
            log.error("Error get file list in " + dirName.getAbsolutePath() + " directory.", e);
            return;
        }
        if (fileArr == null)
        {
            log.info("Not found directory " + dirName.getAbsolutePath());
            return;
        }

        for (int i = 0; i < fileArr.length; i++)
        {
            String s = tfc.getBaseDirectory() + File.separatorChar +
                    parent + File.separatorChar +
                    fileArr[i];
            log.debug("Process file: " + s);
            File file_ = new File(s);

            if (tfc.getExcludeAsReference().indexOf(file_.getName()) != -1)
            {
                log.debug("Skip path " + file_.getName());
                continue;
            }

            if (file_.isFile())
            {
                fileList.addTransferFileContent( encryptFile(file_, parent) );
            }
            else if (file_.isDirectory())
            {
                dir(parent + File.separatorChar + fileArr[i]);
            }
            file_ = null;
        }
        return;
    }

    public static void main(String args[])
            throws Exception
    {

//        TransferFileContent tf = new TransferFileContent();
//        Base64 base64 = new Base64();
        org.riverock.generic.startup.StartupApplication.init();

        checkClass("org.bouncycastle.jce.provider.BouncyCastleProvider");
        checkClass("org.bouncycastle.jceX509KeyUsage");
        checkClass("org.apache.log4j.Category");
        checkClass("org.exolab.castor.mapping.Mapping");
        checkClass("com.oreilly.servlet.multipart.FilePart");


        Provider prov[] = Security.getProviders();
        for (int i = 0; i < prov.length; i++)
            System.out.println("Prov #" + i + ": " + prov[i].getName()+" v"+prov[i].getVersion());

        Provider provider = Security.getProvider("BC");
        if (provider==null)
            System.out.println("Provider DB not found");

//                Security.addProvider(
//            new com.sun.net.ssl.internal.ssl.Provider());
//org.bouncycastle.jce.provider.BouncyCastleProvider

        System.out.println(
                provider.getName() + " v" +
                provider.getVersion()
        );

        System.out.println( "Info "+
                provider.getInfo()
        );


        InputSource inSrc = new InputSource(new FileInputStream(args[0]));
        tfc = (TransferFileConfigType) Unmarshaller.unmarshal(TransferFileConfigType.class, inSrc);


        fileList.setDateCreate(
            DateUtils.getCurrentDate("dd.MM.yyyy HH:mm:ss")
        );
        fileList.setIsGzip( tfc.getIsGzip() );

        log.debug("Base dir: " + tfc.getBaseDirectory() );
        log.debug("is Gzip: " + tfc.getIsGzip() );
        log.debug("ExportFile: " + tfc.getExportFile() );

        for (int i = 0; i < tfc.getDirectoryCount(); i++)
        {
            String processDir = tfc.getDirectory(i);
            log.debug("Directory: " + processDir);
        }
        for (int i = 0; i < tfc.getExcludeCount(); i++)
        {
            String processDir = tfc.getExclude(i);
            log.debug("Exclude: " + processDir);
        }

        log.debug("Run processing");
        for (int i = 0; i < tfc.getDirectoryCount(); i++)
        {
            String processDir = tfc.getDirectory(i);
            dir(processDir);
        }

        String encoding = "UTF-8";

        FileOutputStream fos = new FileOutputStream(tfc.getExportFile());
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));
        marsh.setRootElement("TransferFileList");
        marsh.setMarshalAsDocument(true);
        marsh.setEncoding(encoding);
        marsh.marshal( fileList );

    }
}
