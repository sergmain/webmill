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