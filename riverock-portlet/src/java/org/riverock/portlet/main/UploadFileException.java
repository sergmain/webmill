/*

 * org.riverock.portlet -- Portlet Library

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



package org.riverock.portlet.main;



public class UploadFileException extends Exception

{

    public final static String FILE_NOT_FOUND_ERROR = "Data for storing in file not found. Error #20.01";

    public final static String WRONG_FORMAT_ERROR = "Wrong format uploaded data. Error #20.02";

    public final static String UNSUPPORTED_EXTENSION_ERROR = "Unsupported file extension. Error #20.03";

    public final static String WRONG_REQUEST_HEADER_ERROR = "Bad request, header is wrong. Error #20.04";



    public UploadFileException()

    {

        super();

    }



    public UploadFileException(String s)

    {

        super(s);

    }



    public String toString()

    {

        return super.toString();

    }



    public String getMessage()

    {

        return super.getMessage();

    }



}



