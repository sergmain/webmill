/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.tools.servlet;

import java.io.Writer;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 *  $Id: ServletOutputStreamWithWriter.java 1108 2006-11-29 19:12:24Z serg_main $
 *
 */
public class ServletOutputStreamWithWriter extends ServletOutputStream {
    private Writer out = null;
    private String encoding = null;

    public ServletOutputStreamWithWriter( Writer writer ) {
        super();
        this.out = writer;
        this.encoding = null;
    }

    public ServletOutputStreamWithWriter( Writer writer, String encoding) {
        super();
        this.out = writer;
        this.encoding = encoding;
    }

    public void write(int b) throws IOException {

        if (encoding!=null)
            out.write(new String(new byte[] {(byte)b}, encoding));
        else
            out.write( b );
    }
}