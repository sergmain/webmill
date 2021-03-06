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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import org.riverock.common.contenttype.ContentTypeManager;

public class ServletResponseWrapperInclude implements ServletResponse {

    private final static int BUFFER_INITIAL_SIZE = 10000;

    private PrintWriter realWriter= null;
    private ServletOutputStream realOutputStream = null;
    private ContentTypeManager contentTypeManager = null;
    private boolean isCommited = false;
    private Locale locale = null;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream( BUFFER_INITIAL_SIZE );

    private static class PrintWriterLogger extends PrintWriter {

        public PrintWriterLogger( Writer out ) {
            this( out, false );
        }

        public PrintWriterLogger( Writer out, boolean autoFlush ) {
            super( out, autoFlush );
        }

        public PrintWriterLogger(OutputStream out) {
            super(out, false);
        }

        public PrintWriterLogger(OutputStream out, boolean autoFlush) {
            super(out, autoFlush);
        }

        public void flush() {
            super.flush();
        }

        public void write(String s) {
            super.write(s);
        }

        public void print(boolean b) {
            super.print(b);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(char c) {
            super.print(c);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(double d) {
            super.print(d);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(float f) {
            super.print(f);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(int i) {
            super.print(i);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(long l) {
            super.print(l);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(Object obj) {
            super.print(obj);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(char s[]) {
            super.print(s);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void print(String s) {
            super.print(s);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public PrintWriter printf(String format, Object... args) {
            return super.printf(format, args);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public PrintWriter printf(Locale l, String format, Object... args) {
            return super.printf(l, format, args);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println() {
            super.println();    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(boolean x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(char x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(char x[]) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(double x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(float x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(int x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(long x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(Object x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void println(String x) {
            super.println(x);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void write(char buf[]) {
            super.write(buf);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void write(char buf[], int off, int len) {
            super.write(buf, off, len);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void write(int c) {
            super.write(c);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public void write(String s, int off, int len) {
            super.write(s, off, len);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    public ServletResponseWrapperInclude( Locale locale) {
        this.locale = locale;
        this.contentTypeManager = ContentTypeManager.getInstance( locale );
    }

    public byte[] getBytes() {
        if (realWriter!=null) {
            realWriter.flush();
            realWriter.close();
        }
        return outputStream.toByteArray();
    }

    public void setContentType(String contentTypeString ) {
        // if writer or stream returned, dont change contentType
        if ( realWriter != null || realOutputStream != null || contentTypeString == null ) {
            return;
        }

        contentTypeManager.setContentType( contentTypeString );
    }

    public void setCharacterEncoding( String charset ) {
        // if writer or stream returned, dont change contentType
        if (realWriter!=null || realOutputStream!=null )
            return;

        contentTypeManager.setCharacterEncoding( charset );
    }

    public PrintWriter getWriter() throws IOException {
        if (realOutputStream!=null) {
            throw new IllegalStateException( "getOutputStream() already invoked" );
        }

        if (contentTypeManager==null) {
            contentTypeManager = ContentTypeManager.getInstance( locale );
        }

        realWriter = new PrintWriterLogger(
            new OutputStreamWriter( outputStream, contentTypeManager.getCharacterEncoding() ), true
        );

        return realWriter;
    }

    public void setContentLength( int i ) {
    }

    public String getCharacterEncoding() {
        return contentTypeManager.getCharacterEncoding();
    }

    public String getContentType() {
        return contentTypeManager.getContentType();
    }

    public ServletOutputStream getOutputStream() {
        if (realWriter!=null) {
            throw new IllegalStateException( "getWriter() already invoked" );
        }

        realOutputStream = new ServletOutputStreamWithOutputStream( outputStream );

        return realOutputStream;
    }

    public void flushBuffer() throws IOException {
        if (realWriter!=null) {
            realWriter.flush();
        }
        else if (realOutputStream!=null) {
            realOutputStream.flush();
        }
        isCommited = true;
    }

    /**
     * Clears the content of the underlying buffer in the response without
     * clearing headers or status code. If the
     * response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#isCommitted
     * @see 		#reset
     *
     * @since 2.3
     */
    public void resetBuffer() {
        if (isCommited)
            throw new IllegalStateException("Response already commited.");

        try {
            if (realWriter!=null) {
                realWriter.close();
                realWriter = new PrintWriterLogger(
                    new OutputStreamWriter( outputStream, contentTypeManager.getCharacterEncoding() ), true
                );

            }
            else if (realOutputStream!=null) {
                realOutputStream.close();
                realOutputStream = new ServletOutputStreamWithOutputStream( outputStream );
            }
        } catch (IOException e) {
            String es = "Error invoke resetBuffer().";
            throw new IllegalStateException(es, e);
        }
    }

    /**
     * Clears any data that exists in the buffer as well as the status code and
     * headers.  If the response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @exception IllegalStateException  if the response has already been
     *                                   committed
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     *
     */
    public void reset() {
        // reset of status code and headers not supported
        resetBuffer();
    }

    public void setLocale( final Locale locale ) {
        if (realWriter==null && realOutputStream==null ) {
            this.locale = locale;
            contentTypeManager = ContentTypeManager.getInstance( locale );
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isCommitted() {
        return isCommited;
    }

    public int getBufferSize() {
        return 0;
    }

    public void setBufferSize( int i ) {
    }

}
