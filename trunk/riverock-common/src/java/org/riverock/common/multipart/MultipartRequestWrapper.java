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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


/**
 * 2003. Copyright (c) riverock.org. http://www.riverock.org
 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/
 *
 * $Id$
 */
public class MultipartRequestWrapper extends HashMap<String, Object> {

    /**
     * the standard identifying header of
     * a multipart request
     */
    public static final String MFDHEADER = "multipart/form-data";

    /**
     * the maximum upload size.
     */
    public int maxUpload;

    /**
     * the handler which decodes my input
     * if I am multipart
     */
    private MultipartHandler multipartHandler = null;
    /** the directory in which I will save
     *  uploaded files */
//    private File workDir;


    /**
     */
    public MultipartRequestWrapper(InputStream inputStream,
        String contentType,
        int contentLength,
        boolean saveUploadedFilesToDisk,
        File workDir, boolean allowOverwrite,
        boolean silentlyRename, int max)
        throws IllegalArgumentException, IOException, UploadException {
        if (saveUploadedFilesToDisk && workDir == null)
            throw new IllegalArgumentException("Work dir is not specified");

        maxUpload = max;

        if (contentType != null &&
            contentType.toLowerCase().indexOf(MFDHEADER) > -1) {
            // it's a multipart request
            int length = contentLength;

            if (saveUploadedFilesToDisk) {
                // sanity checks: we have somewhere to
                // save work... This has been checked
                // at Servlet instantiation time but
                // might have changed since so check
                // it again
                if (!(workDir.isDirectory() && workDir.canWrite()))
                    throw new IllegalArgumentException("Bad work directory: " + workDir);
            }
            // what we're being sent is not
            // ridiculously large...
            if (length > maxUpload)
                throw new
                    RequestTooLargeException("Total upload data size too large");

            // protect from DoS attack with replace Content-length in request header
            maxUpload = length;

            // decode the request
            multipartHandler = new MultipartHandler(this, inputStream,
                length, contentType,
                workDir,
                saveUploadedFilesToDisk,
                allowOverwrite,
                silentlyRename);

        }
    }

    public MultipartHandler getMultipartHandler() {
        return multipartHandler;
    }

    public void setMultipartHandler(MultipartHandler multipartHandler) {
        this.multipartHandler = multipartHandler;
    }
}
