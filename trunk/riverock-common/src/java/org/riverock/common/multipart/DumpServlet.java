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
 * 2003. Copyright (c) jSmithy. http://multipart.jSmithy.com
 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/
 *
 * $Id$
 */

package org.riverock.common.multipart;

import org.apache.log4j.Category;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DumpServlet extends javax.servlet.http.HttpServlet
{
    private static Category cat = Category.getInstance("org.riverock.multipart.DumpServlet");

    private static void writeToFile(File file, byte bytes[])
            throws Exception
    {

        FileOutputStream out = new FileOutputStream(file);

        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();

        out = null;

    }

    /** Dump the request input stream onto the response output, byte
     *  for byte, for analysis/debugging purposes. */
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/plain");
        int length = req.getContentLength();
        int read = 0;
        ServletInputStream in = req.getInputStream();
        ServletOutputStream out = resp.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;

        out.println("Content type: "+req.getContentType());
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        do
        {
            n = in.read(buffer, 0, 1024);

            read += n;

            if (n > 0)
            {

                String str = null;
                str = new String(buffer, 0, n);
                outBytes.write(buffer, 0, n);
                out.print( str );
            }
        }
        while (read < length && n > 0);

        File tempDir = (File)getServletContext().getAttribute("javax.servlet.context.tempdir");

        File file = File.createTempFile( "post-dump-", ".dat", tempDir);

        try
        {
            writeToFile(file, outBytes.toByteArray());
        }
        catch(Exception e)
        {
            throw (IOException)e;
        }

        out.println("\nThat's all, folks!!");

        out.flush();
    }
}
