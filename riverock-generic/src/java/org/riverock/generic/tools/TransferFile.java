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

package org.riverock.generic.tools;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 *
 * Author: Serg Malyukov
 * Date: Aug 31, 2002
 * Time: 8:32:21 PM
 *
 *
 * $Id$
 */
public final class TransferFile {
    private final static Logger log = Logger.getLogger( TransferFile.class );

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

    public static void processData(HttpServletRequest request)
        throws Exception
    {
// Todo Need rewrite with org.riverock.multipart package
        throw new Exception("Need rewrite with org.riverock.multipart package");
/*
        try
        {
            MultipartParser mp = new MultipartParser(request, 3 * 1024 * 1024);

            Part part = mp.readNextPart();

            if (part.isFile() &&
                ((FilePart) part).getFileName() != null &&
                ((FilePart) part).getFileName().trim().length() != 0
            )
            {
                FilePart filePart = (FilePart) part;

                if (cat.isDebugEnabled())
                    cat.debug("Name file " + filePart.getFileName());

                File tempDir = new File(GenericConfig.getGenericlTempDir());
                tempDir.mkdirs();
                File file_ = File.createTempFile("x509file_", ".tmp", tempDir);

                filePart.writeTo(file_);

                String nameTempFile = file_.getName();

                if (cat.isDebugEnabled())
                    cat.debug("Upload to " + tempDir.getAbsolutePath() + "; File: " + nameTempFile);
// end upload file


//start processing file
                InputSource inSrc = new InputSource(new FileInputStream(file_));
                TransferFileListType tf =
                    (TransferFileListType) Unmarshaller.unmarshal(TransferFileListType.class, inSrc);

                MainTools.deleteFile(file_.getAbsolutePath());
                for (int i = 0; i < tf.getTransferFileContentCount(); i++)
                {
                    TransferFileContentType tfc = tf.getTransferFileContent(i);

                    File tempFile = File.createTempFile("temp_", ".sign", tempDir);

                    if (cat.isDebugEnabled())
                    {
                        cat.debug("temp signed file " + tempFile.getName());
                        cat.debug("temp signed file " + tempFile.getAbsolutePath());
                        cat.debug("temp signed file " + tempFile.getCanonicalPath());
                    }

                    DecryptFileSignature dfs = new DecryptFileSignature();

                    Base64 base64 = new Base64();
                    writeToFile(
                        GenericConfig.getGenericlTempDir() + File.separatorChar + "file.gzip.sign.4",
                        base64.decode(tfc.getFileContent64().getBytes() )
                    );
                    dfs.initParam(
                        base64.decode(tfc.getFileContent64().getBytes()),
                        tempFile,
                        GenericConfig.getWebmillConfig().getMillEngineKey().getPath(),
                        GenericConfig.getWebmillConfig().getMillEngineKey().getAlias()
                    );
                    dfs.decryptFile();

                    String s = GenericConfig.millApplPath + File.separatorChar + tfc.getFileName();
                    File targetFile = new File(s);

                    if (cat.isDebugEnabled())
                        cat.debug("Target file :" + s);

                    //File targetDir = new File(targetFile.getPath());
                    File targetDir = targetFile.getParentFile();

                    if (cat.isDebugEnabled())
                        cat.debug("Target dir :" + targetDir.getAbsolutePath());

                    targetDir.mkdirs();


                    if (tf.getIsGzip())
                    {
                        FileInputStream byteInStream = null;
                        GZIPInputStream gzip = null;

                        if (cat.isDebugEnabled())
                        {
                            byteInStream = new FileInputStream(tempFile);
                            gzip = new GZIPInputStream(byteInStream);
                            writeToFile(
                                GenericConfig.getGenericlTempDir() + File.separatorChar + "file.gzip.4",
                                getFileBytes(gzip, 10000000)
                            );
                        }
                        byteInStream = new FileInputStream(tempFile);
                        gzip = new GZIPInputStream(byteInStream);

                        writeToFile(
                            tempFile.getAbsolutePath(),
                            getFileBytes(gzip, 10000000)
                        );

                    }

                    writeToFile(
                        targetFile.getAbsolutePath(),
                        getFileBytes(new FileInputStream(tempFile), 10000000)
                    );

                    MainTools.deleteFile(tempFile);
                    tempFile = null;
                }
            }
        }
        catch (Exception e)
        {
            cat.error("Error upload signed file.", e);
            throw e;
        }
*/
    }
}
