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
 * 2003. Copyright (c) jSmithy. http:// multipart.jSmithy.com
 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/
 *
 * $Id$
 */

package org.riverock.common.multipart;

import org.apache.log4j.Category;

import java.lang.*;
import java.io.ByteArrayInputStream;


/** An enhancement to ByteArrayInputStream that supports several
 *  additional fields concerning uploaded files.
 *
 *  @author Shawn Grunberger <shawn@oddpost.com>
 *  @version $Revision$
 *  This revision: $Author$
 *  <pre>
 *  $Log$
 *  Revision 1.2  2005/01/27 02:42:21  serg_main
 *  .
 *
 *  Revision 1.1  2004/04/16 19:28:24  smissan
 *  Initial import
 *
 *  Revision 1.1  2004/02/02 13:14:33  serg_main
 *  init commit
 *
 *  Revision 1.2  2003/05/05 14:18:36  admin
 *  no message
 *
 *  Revision 1.1  2002/10/08 09:22:48  simon
 *  A number of minor enhancements and some bug fixes proposed by Shawn Grunberger
 *
 *
 *  </pre>
 */

public class UploadedBytes extends ByteArrayInputStream
{
    private static Category cat = Category.getInstance("org.riverock.multipart.UploadedBytes");

    protected String clientPathname = null;
    protected String contentType = null;

    protected UploadedBytes(byte[] buf)
    {
        super(buf);  // hee
    }

    /**
     * Returns the client (remote) pathname for this file. This is
     * the full path as reported by the browser during the file
     * upload.
     */
    public String getClientPathname()
    {
        return clientPathname;
    }

    /**
     * Sets the client (remote) pathname for this file. This is
     * the full path as reported by the browser during the file
     * upload.
     */
    protected void setClientPathname(String pathname)
    {
        clientPathname = pathname;
    }

    /**
     * Returns the content type (MIME type) of the uploaded file.
     * This was reported by the browser during the file upload.
     * Returns null if no content type was specified.
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Sets the content type (MIME type) of the uploaded file.
     * This was reported by the browser during the file upload.
     */
    protected void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


}
