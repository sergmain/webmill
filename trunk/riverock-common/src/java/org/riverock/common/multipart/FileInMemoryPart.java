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



import java.io.InputStream;



import org.apache.log4j.Logger;



public class FileInMemoryPart implements AbstractPart

{

    private static Logger log = Logger.getLogger(FileOnDiskPart.class);



    private UploadedBytes bytes = null;



    public FileInMemoryPart(UploadedBytes  bytes_)

    {

        this.bytes = bytes_;

    }



    public int getSubType()

    {

        return FILE_IN_MEMORY_TYPE;

    }



    public int getType()

    {

        return FILE_TYPE;

    }



    public InputStream getInputStream()

        throws MultipartRequestException

    {

        return bytes;

    }



    public String getStringValue()

        throws MultipartRequestException

    {

        return bytes.toString();

    }

}

