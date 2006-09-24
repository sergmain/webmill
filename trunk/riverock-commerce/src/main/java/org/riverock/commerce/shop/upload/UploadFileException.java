/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.shop.upload;

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

