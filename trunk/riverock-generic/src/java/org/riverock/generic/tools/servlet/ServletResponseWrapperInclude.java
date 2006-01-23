/*
 * org.riverock.generic -- Database connectivity classes
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
package org.riverock.generic.tools.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import org.riverock.common.contenttype.ContentTypeManager;

import org.apache.log4j.Logger;

public final class ServletResponseWrapperInclude implements ServletResponse {

    private final static Logger log = Logger.getLogger( ServletResponseWrapperInclude.class );

    private final static int BUFFER_INITIAL_SIZE = 10000;

    private PrintWriter realWriter= null;
    private ServletOutputStream realOutputStream = null;
    private ContentTypeManager contentTypeManager = null;
    private boolean isCommited = false;
    private Locale locale = null;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream( BUFFER_INITIAL_SIZE );

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    public void setContentType(String contentTypeString ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "set new contentType: " + contentTypeString );
        }

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


    protected void finalize() throws Throwable {
        super.finalize();
    }

    public ServletResponseWrapperInclude( Locale locale) {
        this.locale = locale;
        this.contentTypeManager = ContentTypeManager.getInstance( locale );
    }

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
            if ( log.isDebugEnabled() ) {
                log.debug( "flush content" );
            }
            super.flush();
        }

        public void write(String s) {
            if ( log.isDebugEnabled() ) {
                log.debug( "write string: " + s );
            }
            super.write(s);
        }
    }

    public PrintWriter getWriter() throws IOException {
        if (realOutputStream!=null) {
            throw new IllegalStateException( "getOutputStream() already invoked" );
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter(), outputStream: " +
                (outputStream==null?"is null":outputStream.getClass().getName())
            );
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "contentType: " + contentTypeManager );
            if (contentTypeManager!=null)
                log.debug( "charset: " + contentTypeManager.getCharacterEncoding() );
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
        if ( log.isDebugEnabled() ) {
            log.debug( "getOutputStream(), outputStream: " +
                (outputStream==null?"is null":outputStream.getClass().getName())
            );
        }

        realOutputStream = new ServletOutputStreamWithOutputStream( outputStream );

        return realOutputStream;
    }

    public void flushBuffer() throws IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "flushBuffer(), realWriter; " +
                (realWriter==null?"is null":realWriter.getClass().getName()) +
                ", realOutputStream: " +
                (realOutputStream==null?"is null":realOutputStream.getClass().getName())
            );
        }

        if (realWriter!=null) {
            realWriter.flush();
            if ( log.isDebugEnabled() ) {
                log.debug( "flushBuffer(). writer: "+realWriter.toString());
            }
        }
        else if (realOutputStream!=null) {
            realOutputStream.flush();
            if ( log.isDebugEnabled() ) {
                log.debug( "flushBuffer(). outputStream: "+realOutputStream.toString() );
            }
        }
        isCommited = true;
    }

    public void resetBuffer() {
        if (isCommited)
            throw new IllegalStateException("response already commited");
    }

    public void reset() {
        if (isCommited)
            throw new IllegalStateException("response already commited");
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
        throw new IllegalStateException("response already commited");
    }

}
