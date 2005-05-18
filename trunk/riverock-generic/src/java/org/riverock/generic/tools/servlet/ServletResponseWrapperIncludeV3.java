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
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import org.riverock.common.tools.ServletTools;

import org.apache.log4j.Logger;

public final class ServletResponseWrapperIncludeV3 implements ServletResponse {

    private final static Logger log = Logger.getLogger( ServletResponseWrapperIncludeV3.class );

    private final static int BUFFER_INITIAL_SIZE = 10000;

    private static CharsetMapper charsetMapper = new CharsetMapper();
    private PrintWriter realWriter= null;
    private ServletOutputStream realOutputStream = null;
    private ServletTools.ContentType contentType = null;
    private boolean isCommited = false;
    private Locale locale = null;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream( BUFFER_INITIAL_SIZE );
    private static final String DEFAULT__CONTENT__TYPE = "text/html";

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

        ServletTools.ContentType type = new ServletTools.ContentType( contentTypeString );
        if ( log.isDebugEnabled() ) {
            log.debug( "parsed contentType: " + type );
            if (type!=null) {
                log.debug( "contentType.getContentType(): " + type.getContentType() );
                log.debug( "contentType.getCharset(): " + type.getCharset() );
            }
        }
        if ( type == null )
            return;

        StringBuffer contentTypeTemp = null;
        if ( type.getContentType() != null )
            contentTypeTemp = type.getContentTypeStringBuffer();
        else
            contentTypeTemp = new StringBuffer( DEFAULT_CONTENT_TYPE );

        Charset charsetTemp = null;
        if ( type.getCharset() != null ) {
            charsetTemp = type.getCharset();
        } else {
            charsetTemp = DEFAULT_CHARSET;
        }

        this.contentType = new ServletTools.ContentType( contentTypeTemp, charsetTemp );

        if ( log.isDebugEnabled() ) {
            log.debug( "result contentType.charset " + contentType.getCharset() );
            log.debug( "result contentType.contentType " + contentType.getContentType() );
        }
    }

    public void setCharacterEncoding( String charset ) {
        // if writer or stream returned, dont change contentType
        if (realWriter!=null || realOutputStream!=null )
            return;

        contentType.setCharset( charset );
    }

    public String getContentType() {
        if ( contentType==null )
            contentType = initContentType();

        StringBuffer content = contentType.getContentTypeStringBuffer();
        if (content==null)
            return null;

        String charset = getCharacterEncoding();
        if (charset==null) {
            return content.toString();
        }
        else {
            return  content.append( "; charset=" ).append( charset ).toString();
        }
    }

    public String getCharacterEncoding() {
        if ( contentType==null )
            contentType = initContentType();

        if ( log.isDebugEnabled()) {
            log.debug("start getCharacterEncoding");
            log.debug("contentType: " + contentType );
            if (contentType!=null ) {
                log.debug("contentType: " + contentType.getCharset().toString() );
            }
            else {
                log.debug( "contentType is null" );
            }
        }

        if ( contentType.getCharset()!=null )
            return contentType.getCharset().toString();
        else
            return null;
    }

    // todo
    // If the deployment descriptor contains a locale-encoding-mapping-list element,
    // and that element provides a mapping for the given locale, that mapping is used.
    // Otherwise, the mapping from locale to character encoding is container dependent.
    final static Charset DEFAULT_CHARSET = Charset.forName( "ISO-8859-1" );
    final static String DEFAULT_CONTENT_TYPE = "text/html";
    private ServletTools.ContentType initContentType() {
        if ( log.isDebugEnabled()) {
            log.debug("start initContentType");
            log.debug("locale: " + locale );
        }
        if ( locale == null ) {
            return new ServletTools.ContentType( DEFAULT__CONTENT__TYPE, DEFAULT_CHARSET );
        }

        String charset = charsetMapper.getCharset( locale );
        if ( log.isDebugEnabled()) {
            log.debug("mapped charset: " + charset );
        }
        if ( charset == null )
            return new ServletTools.ContentType( "text/html", DEFAULT_CHARSET );
        else
            return new ServletTools.ContentType( "text/html", Charset.forName( charset ) );
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    public ServletResponseWrapperIncludeV3( Locale locale) {
        this.locale = locale;
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
            log.debug( "contentType: " + contentType );
            if (contentType!=null)
                log.debug( "charset: " + contentType.getCharset() );
        }

        realWriter = new PrintWriterLogger(
            new OutputStreamWriter( outputStream, getCharacterEncoding() ), true
        );

        return realWriter;
    }

    public void setContentLength( int i ) {
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
        this.locale = locale;
        if (realWriter==null && realOutputStream==null )
            initContentType();
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
