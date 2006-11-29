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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletOutputStream;

/**
 * @deprecated
 *
 * Author: mill
 * Date: Feb 19, 2003
 * Time: 1:45:08 PM
 *
 * $Id$
 */
public class HttpServletResponseApplWrapper implements HttpServletResponse {

    public HttpServletResponseApplWrapper() {
    }

    public void addCookie( Cookie cookie ) {
    }

    public boolean containsHeader( String s ) {
        return false;
    }

    public String encodeURL( String s ) {
        return s;
    }

    public String encodeRedirectURL( String s ) {
        return s;
    }

    /**
     * @deprecated
     */
    public String encodeUrl( String s ) {
        return s;
    }

    /**
     * @deprecated
     */
    public String encodeRedirectUrl( String s ) {
        return s;
    }

    public void sendError( int i, String s ) throws IOException {
    }

    public void sendError( int i ) throws IOException {
    }

    public void sendRedirect( String s ) throws IOException {
    }

    public void setDateHeader( String s, long id_item ) {
    }

    public void addDateHeader( String s, long id_item ) {
    }

    public void setHeader( String s, String s1 ) {
    }

    public void addHeader( String s, String s1 ) {
    }

    public void setIntHeader( String s, int i ) {
    }

    public void addIntHeader( String s, int i ) {
    }

    public void setStatus( int i ) {
    }

    /**
     * @deprecated
     */
    public void setStatus( int i, String s ) {
    }

    public String getCharacterEncoding() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        return null;
    }

    public void setContentLength( int i ) {
    }

    public void setContentType( String s ) {
    }

    public void setBufferSize( int i ) {
    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
    }

    public void setLocale( Locale locale ) {
    }

    public Locale getLocale() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public void setCharacterEncoding( String s ) {
    }

}
