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

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.riverock.common.tools.ServletTools;

import org.apache.log4j.Logger;

public final class ServletResponseWrapperIncludeV2 extends HttpServletResponseWrapper {

    private final static Logger log = Logger.getLogger( ServletResponseWrapperIncludeV2.class );

    private Writer writer= null;
    private OutputStream outputStream = null;

    private PrintWriter realWriter= null;
    private ServletOutputStream realOutputStream = null;
    private ServletTools.ContentType contentType = new ServletTools.ContentType( "text/html" );

    public void setContentType(String contentTypeString ) {
        if ( contentTypeString != null )
            this.contentType = new ServletTools.ContentType( contentTypeString );

        if ( log.isDebugEnabled() ) {
            log.debug( "set new content type to " + contentTypeString );
            log.debug( "contentType.charset " + contentType.getCharset() );
            log.debug( "contentType.contentType " + contentType.getContentType() );
        }
    }

    public String getCharacterEncoding() {
        if ( contentType.getCharset()!=null )
            return contentType.getCharset().toString();
        else
            return null;
    }

    protected void finalize() throws Throwable {
        writer = null;
        super.finalize();
    }

    public ServletResponseWrapperIncludeV2( final ServletResponse response, final Writer writer) {
        super( (HttpServletResponse)response );
        if ( log.isDebugEnabled() ) {
            log.debug( "ServletResponseWrapperInclude( ServletResponse response, Writer writer), " +
                "writer; " + (writer==null?"is null":writer.getClass().getName())
            );
        }

        this.writer = writer;
    }

    public ServletResponseWrapperIncludeV2( final ServletResponse response, final OutputStream outputStream) {
        super( (HttpServletResponse)response );
        this.outputStream = outputStream;
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

    public PrintWriter getWriter() {
        if (realOutputStream!=null) {
            throw new IllegalStateException( "getOutputStream() already invoked" );
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter(), writer; " +
                (writer==null?"is null":writer.getClass().getName()) +
                ", outputStream: " +
                (outputStream==null?"is null":outputStream.getClass().getName())
            );
        }
        if ( writer!=null ) {
            realWriter = new PrintWriterLogger( writer, true );
        }
        else {
            if ( log.isDebugEnabled() ) {
                log.debug( "charset: " + contentType.getCharset() );
            }

            if (contentType.getCharset()!=null) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "charset: "+contentType.getCharset());
                }
                realWriter = new PrintWriterLogger( new OutputStreamWriter( outputStream, contentType.getCharset() ), true);
            }
            else
                realWriter = new PrintWriterLogger( outputStream, true );
        }
        return realWriter;
    }

    public ServletOutputStream getOutputStream() {
        if (realWriter!=null) {
            throw new IllegalStateException( "getWriter() already invoked" );
        }
        if ( log.isDebugEnabled() ) {
            log.debug( "getOutputStream(), writer; " +
                (writer==null?"is null":writer.getClass().getName()) +
                ", outputStream: " +
                (outputStream==null?"is null":outputStream.getClass().getName())
            );
        }

        if ( writer!=null ) {
            realOutputStream = new ServletOutputStreamWithWriter( writer );
        }
        else {
            realOutputStream = new ServletOutputStreamWithOutputStream( outputStream );
        }

        return realOutputStream;
    }

    //Todo: uncomment and implement

//    public int getBufferSize() {
//        return response.getBufferSize();
//    }

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
    }

//    public void resetBuffer() {
//        response.resetBuffer();
//    }
//
//    public boolean isCommitted() {
//        return response.isCommitted();
//    }
//
//    public void reset() {
//        response.reset();
//    }

}
