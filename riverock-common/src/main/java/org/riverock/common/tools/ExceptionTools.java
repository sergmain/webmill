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

package org.riverock.common.tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * $Revision$
 * $Date$
 * $RCSfile$
 *
 */
public final class ExceptionTools {

    public static String getStackTrace( final Throwable e, final int numLines ) {
        return getStackTrace(e, numLines, null);
    }

    public static String getStackTrace( final Throwable e, final int numLines, final String addAtEndLine ) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bytesOut, true));
        byte[] bytes = bytesOut.toByteArray();
        int countLines = 0;
        int i = 0;
        for (; i < bytes.length; i++) {
            if (bytes[i] == '\n')
                countLines++;

            if (countLines > numLines)
                break;
        }

        if (addAtEndLine== null || "".equals(addAtEndLine)) {
            if (i >= bytes.length)
                return new String(bytes);
            else
                return new String(bytes, 0, i);
        }
        else {
            if (i >= bytes.length)
                return StringUtils.replace(new String(bytes), "\n", addAtEndLine + "\n");
            else
                return StringUtils.replace(new String(bytes, 0, i), "\n", addAtEndLine + "\n");
        }
    }
}