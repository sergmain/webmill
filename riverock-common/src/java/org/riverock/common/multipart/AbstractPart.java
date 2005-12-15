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
package org.riverock.common.multipart;

import java.io.InputStream;

/**
 * Author: mill
 * Date: Mar 17, 2003
 * Time: 9:22:59 AM
 *
 * $Id$
 */
public interface AbstractPart {
    public final static int PARAMETER_TYPE = 1;
    public final static int FILE_TYPE = 2;

    public final static int FILE_ON_DISK_TYPE = 2;
    public final static int FILE_IN_MEMORY_TYPE = 3;

    public int getSubType();
    public int getType();
    public long getLength();
    public String getStringValue() throws MultipartRequestException;

    public InputStream getInputStream() throws MultipartRequestException;
}