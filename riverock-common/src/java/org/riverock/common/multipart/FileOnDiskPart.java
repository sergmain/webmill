/*

 * org.riverock.common -- Supporting classes, interfaces, and utilities

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



/**

 * Author: mill

 * Date: Mar 17, 2003

 * Time: 9:44:56 AM

 *

 * $Id$

 */



package org.riverock.common.multipart;



import java.io.FileInputStream;

import java.io.InputStream;

import java.io.FileNotFoundException;



import org.apache.log4j.Logger;



public class FileOnDiskPart implements AbstractPart

{

    private static Logger log = Logger.getLogger(FileOnDiskPart.class);



    private UploadedFile file = null;



    public FileOnDiskPart(UploadedFile  file_)

    {

        this.file = file_;

    }



    public int getSubType()

    {

        return FILE_ON_DISK_TYPE;

    }



    public int getType()

    {

        return FILE_TYPE;

    }



    public InputStream getInputStream()

        throws MultipartRequestException

    {

        try

        {

            return new FileInputStream(file);

        }

        catch (FileNotFoundException fileNotFoundException)

        {

            log.error("Exception in ", fileNotFoundException);

            throw new MultipartRequestException( fileNotFoundException.toString() );

        }

    }



    public String getStringValue()

        throws MultipartRequestException

    {

        throw new MultipartRequestException("Not implemented");

    }

}

