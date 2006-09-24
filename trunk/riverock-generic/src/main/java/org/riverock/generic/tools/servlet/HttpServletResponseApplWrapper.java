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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.tools.servlet;

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
