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
package org.riverock.generic.tools.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 2:07:23
 * $Id$
 */
public class ServletOutputStreamWithOutputStream extends ServletOutputStream {
    private OutputStream outputStream = null;

    public ServletOutputStreamWithOutputStream( OutputStream outputStream ) {
        super();
        this.outputStream = outputStream;
    }

    public void write(int b) throws IOException {
        outputStream.write( b );
    }
}