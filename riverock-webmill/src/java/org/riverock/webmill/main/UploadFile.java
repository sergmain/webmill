/*

 * org.riverock.webmill -- Portal framework implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public

 * License as published by the Free Software Foundation; either

 * version 2 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * General Public License for more details.

 *

 * You should have received a copy of the GNU General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 *

 * $Id$

 *

 */

package org.riverock.webmill.main;



import java.io.File;



import javax.servlet.http.HttpServletRequest;



public class UploadFile

{



    public static String save(HttpServletRequest request, int bufferSize,

                              String storeFile, boolean isAppend)

            throws UploadFileException

    {

        throw new UploadFileException("not supported");

    }



// com.oreilly.servlet.multipart.* implemention

    public static String save(HttpServletRequest request, int bufferSize,

                              String storeFile, boolean isAppend, String ext[])

            throws UploadFileException

    {

        throw new UploadFileException("not supported");

/*

        FilePart filePart = null;

        String returnFileName = "", fileName = null;

        try

        {



            boolean isOkUpload = true;

            MultipartParser mp = new MultipartParser(request, bufferSize);



            Part part = mp.readNextPart();



            if (part == null)

                throw new UploadFileException( UploadFileException.FILE_NOT_FOUND_ERROR );



            if (!part.isFile())

                throw new UploadFileException( UploadFileException.WRONG_FORMAT_ERROR );



            filePart = (FilePart) part;

            fileName = filePart.getFileName();



            returnFileName = storeFile + (isAppend?fileName:"");



            if (ext != null)

            {

                int i;

                for (i = 0; i < ext.length; i++)

                {

                    if ((ext[i] != null) && returnFileName.toLowerCase().endsWith(ext[i].toLowerCase()))

                        break;

                }

                if (i == ext.length)

                    throw new UploadFileException( UploadFileException.UNSUPPORTED_EXTENSION_ERROR );

            }



            File file_ = new File(returnFileName);

            returnFileName = file_.getName();



            file_.mkdirs();

            file_.delete();



            if (fileName != null)

                filePart.writeTo(file_);



        }

        catch (Exception e)

        {

            throw new UploadFileException(e.toString());

        }

        finally

        {

        }

        return returnFileName;

*/

    }





    public static void saveCOS(HttpServletRequest request, int bufferSize,

                            File storeFile)

            throws UploadFileException

    {

        throw new UploadFileException("not supported");

/*



        FilePart filePart = null;

        try

        {



            boolean isOkUpload = true;

            MultipartParser mp = new MultipartParser(request, bufferSize);



            Part part = mp.readNextPart();



            if (part == null)

                throw new UploadFileException( UploadFileException.FILE_NOT_FOUND_ERROR );



            if (!part.isFile())

                throw new UploadFileException( UploadFileException.WRONG_FORMAT_ERROR );



            filePart = (FilePart) part;

            String fileName = filePart.getFileName();



            if (fileName != null)

                filePart.writeTo(storeFile);





        }

        catch (Exception e)

        {

            throw new UploadFileException(e.toString());

        }

        finally

        {

        }

*/

    }



}

