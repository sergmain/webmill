/*
 * org.riverock.common - Supporting classes and utilities
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
    public static final String THROWABLE_IS_NULL = "Throwable is null";

    public static String getStackTrace( final Throwable e, final int numLines ) {
        return getStackTrace(e, numLines, null);
    }

    public static String getStackTrace( final Throwable e, final int numLines, final String addAtEndLine ) {
        if (e==null) {
            return THROWABLE_IS_NULL;
        }
        
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