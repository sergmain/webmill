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

 * 2003. Copyright (c) jSmithy. http:// multipart.jSmithy.com

 * 2001-2003. Copyright (c) Simon Brooke. http://www.weft.co.uk/library/maybeupload/

 *

 * $Id$

 */



package org.riverock.common.multipart;



import java.io.BufferedReader;

import java.io.File;

import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.util.Enumeration;

import java.util.Hashtable;

import java.util.Locale;

import java.util.Map;

import java.util.List;

import java.util.ArrayList;



import javax.servlet.RequestDispatcher;

import javax.servlet.ServletContext;

import javax.servlet.ServletInputStream;

import javax.servlet.ServletRequest;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;



import org.apache.log4j.Category;



public class MultipartRequestWrapper extends Hashtable

    implements HttpServletRequest

{

    private static Category cat = Category.getInstance("org.riverock.multipart.MultipartRequestWrapper");



    /** the standard identifying header of

     *  a multipart request */

    public static final String MFDHEADER = "multipart/form-data";



    /** the maximum upload size. */

    public int maxUpload;

    /** the request I wrap */

    private HttpServletRequest request = null;



    /** save uploads directly to disk? */

//    private boolean saveUploadedFilesToDisk;

    /** whether that request is multipart */

    private boolean isMultipart = false;



    /** the handler which decodes my input

     *  if I am multipart */

    private MultipartHandler multipartHandler = null;

    /** the directory in which I will save

     *  uploaded files */

//    private File workDir;





    /** we advise you do not use this! 'empty' constructor to make it

     *  easier to sub-class. As this constructor does not pass a

     *  request to wrap, it isn't, on it's own, going to work.  */

    public MultipartRequestWrapper()

        throws IllegalArgumentException, IOException, UploadException

    {

        throw new UploadException("do not use the no-args variant of the " +

            getClass().getName() + " constructor");

    }





    public MultipartRequestWrapper(HttpServletRequest req,

                                     boolean saveUploadedFilesToDisk,

                                     File workDir, boolean allowOverwrite,

                                     boolean silentlyRename, int max)

        throws IllegalArgumentException, IOException, UploadException

    {

        if (saveUploadedFilesToDisk && workDir==null)

            throw new IllegalArgumentException("Work dir is not specified");



        String contentType = req.getContentType();



        request = req;

        maxUpload = max;



        if (contentType != null &&

            contentType.toLowerCase().indexOf(MFDHEADER) > -1)

        {			// it's a multipart request

            int length = req.getContentLength();



            isMultipart = true;



//            this.workDir = workDir;

//            this.saveUploadedFilesToDisk = saveUploadedFilesToDisk;



            if (saveUploadedFilesToDisk)

            {

                // sanity checks: we have somewhere to

                // save work... This has been checked

                // at Servlet instantiation time but

                // might have changed since so check

                // it again

                if (!(workDir.isDirectory() && workDir.canWrite()))

                    throw new

                        IllegalArgumentException(

                            "Bad work directory: " +

                        workDir);

            }

            // what we're being sent is not

            // ridiculously large...

            if (length > maxUpload)

                throw new

                    RequestTooLargeException("Total upload data size " +

                    "too large");



            // protect from DoS attack with replace Content-length in request header

            maxUpload = length;



            // decode the request

            multipartHandler = new MultipartHandler(this, req.getInputStream(),

                length, contentType,

                workDir,

                saveUploadedFilesToDisk,

                allowOverwrite,

                silentlyRename);



        }

    }



    /** Construct a new multipart-aware wrapper around an HttpServletRequest

     *    This constructor maintains compatibility with 1.0.2pre3 code

     *    before direct handling of binary uploads (without saving to disk

     *    was added)

     *  @param req the HttpServletRequest to wrap around

     *  @param workDir the directory to store uploaded files to

     *  @param workDir a directory in which to save uploaded files

     *  @param allowOverwrite Overwrite existing files

     *  @param silentlyRename Create unique filenames to prevent overwriting

     *  @deprecated do not use. Not supported. Will be withdrawn soon. If you

     *  have any code which uses this class directly, please keep it up to

     *  date with upload package.

     */

    public MultipartRequestWrapper(HttpServletRequest req,

                                     File workDir, boolean allowOverwrite,

                                     boolean silentlyRename)

        throws IllegalArgumentException, IOException, UploadException

    {

        this(req, true, workDir, allowOverwrite, silentlyRename, 524288);

    }



    /** Construct a new multipart-aware wrapper around an HttpServletRequest

     *    This constructor maintains compatibility with 1.0.2pre3 code

     *    before direct handling of binary uploads (without saving to disk

     *    was added)

     *  @param req the HttpServletRequest to wrap around

     *  @deprecated do not use. Not supported. Will be withdrawn soon. If you

     *  have any code which uses this class directly, please keep it up to

     *  date with upload package.

     */

    public MultipartRequestWrapper(HttpServletRequest req)

        throws IllegalArgumentException, IOException, UploadException

    {

        this(req, true, null, false, false, 524288);

    }



    ///// Methods that actually do something /////



    /** The preferred method of accessing parameter values; use this

     *  in preference to either (@link getParameter) or (@link

     *  getParameterValues). Get the value associated with

     *  <code>name</code> in the request parameters.

     *

     *  <p>The object returned will be an instance of

     *  <ul>

     *    <li>a <code>java.lang.String</code>, or</li>

     *    <li>a <code>java.io.File</code>, or</li>

     *    <li>a <code>java.lang.Vector</code> containing only Strings

     *    and Files, or</li>

     *    <li>a <code>java.io.ByteArrayInputStream</code> containing the

     *    file submitted as a binary input stream, if

     *    saveUploadedFilesToDisk is false.

     * </ul>

     *

     *  <p>If the parameter has a single value, the array has a length

     *  of 1.</p>

     *

     *  @param name a <code>String</code> containing the name of

     *  the parameter whose value is requested

     *

     *  @return		an array of <code>String</code> or

     *                  <code>File</code> objects

     *			containing the parameter's values

     *

     *  @see		#getParameter

     * */



    public Object get(String name)

    {

        Object result = null;	// safe default.



        if (isMultipart)

        {

            result = super.get(name);

        }

        else

        {

            String[] array = request.getParameterValues(name);

            List vec = new ArrayList();



            if (array != null)

            {

                for (int i = 0; i < array.length; i++)

                    vec.add(array[i]);



                if (vec.size() == 1)

                    result = vec.get(0);

                else

                    result = vec;

            }

        }



        return result;

    }





    ///// Implementation of ServletRequest /////





    /**

     *

     * Returns an <code>Enumeration</code> of <code>String</code>

     * objects containing the names of the parameters contained

     * in this request. If the request has

     * no parameters, the method returns an

     * empty <code>Enumeration</code>.

     *

     * @return		an <code>Enumeration</code> of <code>String</code>

     *			objects, each <code>String</code> containing

     * 			the name of a request parameter; or an

     *			empty <code>Enumeration</code> if the

     *			request has no parameters

     *

     */



    public Enumeration getParameterNames()

    {

        if (isMultipart)

            return multipartHandler.getValues().keys();

        else

            return request.getParameterNames();

    }





    /** Return a String representation of the value of the named

     *  parameter if any, else null. Not useful if the value isn't

     *  inherently a String. This method is not formally deprecated,

     *  because it is part of the Servlet 2.2 API; nevertheless if

     *  you're using this package at all, you really want to use

     *  (@link get) instead.

     *

     *  @param name the name of the parameter to fetch

     *  @return the value of that parameter, as a String

     */

    public String getParameter(String name)

    {

        Object value = get(name);

        String result = null;



        if (value != null)

        {

            if (value instanceof List)

                value = ((List)value).get(0);



            result = value.toString();

        }



        return result;

    }





    /** Return a representation of the values of the named parameter

     *  if any, as an array of Strings; else null. Not useful if the

     *  values weren't inherently Strings. This method is not formally

     *  deprecated, because it is part of the Servlet 2.2 API;

     *  nevertheless if you're using this package at all, you really

     *  want to use (@link get) instead.

     *

     *  @param name the name of the parameter to fetch

     *  @return the value of that parameter, as a String

     */

    public String[] getParameterValues(String name)

    {

        if (isMultipart)

        {

            Object value = get(name);

            String[] result = null;



            if (value != null)

            {

                if (value instanceof List)

                {

                    int n = ((List) value).size();

                    // number of values to be returned

                    result = new String[n];



                    for (int i = 0; i < n; i++)

                    {

                        result[i] =

                            ((List) value).get(i).toString();

                    }

                }

                else

                {

                    result = new String[1];

                    result[0] = value.toString();

                }

            }



            return result;

        }

        else

        {

            return request.getParameterValues(name);

        }

    }



    public Map getParameterMap()

    {

        return null;

    }



    /**

     *  Occasionally a servlet will need access to the request

     *    wrapped within a MaybeUploadRequestWrapper. E.G., when

     *    forwarding a request on to a JSP display layer.

     *

     *  @return the HttpServletRequest wrapped within this

     #    MaybeUploadRequestWrapper

     */

    public HttpServletRequest getRequest()

    {

        return request;

    }



    ///// simple wrappers /////



    /**

     *

     * Returns the value of the named attribute as an <code>Object</code>,

     * or <code>null</code> if no attribute of the given name exists.

     *

     * <p> Attributes can be set two ways.  The servlet container may set

     * attributes to make available custom information about a request.

     * For example, for requests made using HTTPS, the attribute

     * <code>javax.servlet.request.X509Certificate</code> can be used to

     * retrieve information on the certificate of the client.  Attributes

     * can also be set programatically using

     * {@link ServletRequest#setAttribute}.  This allows information to be

     * embedded into a request before a {@link RequestDispatcher} call.

     *

     * <p>Attribute names should follow the same conventions as package

     * names. This specification reserves names matching <code>java.*</code>,

     * <code>javax.*</code>, and <code>sun.*</code>.

     *

     * @param name	a <code>String</code> specifying the name of

     *			the attribute

     *

     * @return		an <code>Object</code> containing the value

     *			of the attribute, or <code>null</code> if

     *			the attribute does not exist

     *

     */



    public Object getAttribute(String name)

    {

        return request.getAttribute(name);

    }





    /**

     * Returns an <code>Enumeration</code> containing the

     * names of the attributes available to this request.

     * This method returns an empty <code>Enumeration</code>

     * if the request has no attributes available to it.

     *

     *

     * @return		an <code>Enumeration</code> of strings

     *			containing the names

     * 			of the request's attributes

     *

     */



    public Enumeration getAttributeNames()

    {

        return request.getAttributeNames();

    }





    /**

     * Returns the name of the character encoding used in the body of this

     * request. This method returns <code>null</code> if the request

     * does not specify a character encoding

     *

     *

     * @return		a <code>String</code> containing the name of

     *			the chararacter encoding, or <code>null</code>

     *			if the request does not specify a character encoding

     *

     */



    public String getCharacterEncoding()

    {

        return request.getCharacterEncoding();

    }



    public void setCharacterEncoding(String s) throws UnsupportedEncodingException

    {

    }





    /**

     * Returns the length, in bytes, of the request body

     * and made available by the input stream, or -1 if the

     * length is not known. For HTTP servlets, same as the value

     * of the CGI variable CONTENT_LENGTH.

     *

     * @return		an integer containing the length of the

     * 			request body or -1 if the length is not known

     *

     */



    public int getContentLength()

    {

        return request.getContentLength();

    }





    /**

     * Returns the MIME type of the body of the request, or

     * <code>null</code> if the type is not known. For HTTP servlets,

     * same as the value of the CGI variable CONTENT_TYPE.

     *

     * @return		a <code>String</code> containing the name

     *			of the MIME type of

     * 			the request, or -1 if the type is not known

     *

     */



    public String getContentType()

    {

        return request.getContentType();

    }





    /**

     * Retrieves the body of the request as binary data using

     * a {@link ServletInputStream}.  Either this method or

     * {@link #getReader} may be called to read the body, not both.

     *

     * @return			a {@link ServletInputStream} object containing

     * 				the body of the request

     *

     * @exception IllegalStateException  if the {@link #getReader} method

     * 					 has already been called for this request

     *

     * @exception IOException    	if an input or output exception occurred

     *

     */



    public ServletInputStream getInputStream() throws IOException

    {

        return request.getInputStream();

    }





    /**

     * Returns the name and version of the protocol the request uses

     * in the form <i>protocol/majorVersion.minorVersion</i>, for

     * example, HTTP/1.1. For HTTP servlets, the value

     * returned is the same as the value of the CGI variable

     * <code>SERVER_PROTOCOL</code>.

     *

     * @return		a <code>String</code> containing the protocol

     *			name and version number

     *

     */



    public String getProtocol()

    {

        return request.getProtocol();

    }





    /**

     * Returns the name of the scheme used to make this request,

     * for example,

     * <code>http</code>, <code>https</code>, or <code>ftp</code>.

     * Different schemes have different rules for constructing URLs,

     * as noted in RFC 1738.

     *

     * @return		a <code>String</code> containing the name

     *			of the scheme used to make this request

     *

     */



    public String getScheme()

    {

        return request.getScheme();

    }





    /**

     * Returns the host name of the server that received the request.

     * For HTTP servlets, same as the value of the CGI variable

     * <code>SERVER_NAME</code>.

     *

     * @return		a <code>String</code> containing the name

     *			of the server to which the request was sent

     */



    public String getServerName()

    {

        return request.getServerName();

    }





    /**

     * Returns the port number on which this request was received.

     * For HTTP servlets, same as the value of the CGI variable

     * <code>SERVER_PORT</code>.

     *

     * @return		an integer specifying the port number

     *

     */



    public int getServerPort()

    {

        return request.getServerPort();

    }





    /**

     * Retrieves the body of the request as character data using

     * a <code>BufferedReader</code>.  The reader translates the character

     * data according to the character encoding used on the body.

     * Either this method or {@link #getReader} may be called to read the

     * body, not both.

     *

     *

     * @return					a <code>BufferedReader</code>

     *						containing the body of the request

     *

     * @exception UnsupportedEncodingException 	if the character set encoding

     * 						used is not supported and the

     *						text cannot be decoded

     *

     * @exception IllegalStateException   	if {@link #getInputStream} method

     * 						has been called on this request

     *

     * @exception IOException  			if an input or output exception occurred

     *

     * @see 					#getInputStream

     *

     */



    public BufferedReader getReader() throws IOException

    {

        return request.getReader();

    }





    /**

     * Returns the Internet Protocol (IP) address of the client

     * that sent the request.  For HTTP servlets, same as the value of the

     * CGI variable <code>REMOTE_ADDR</code>.

     *

     * @return		a <code>String</code> containing the

     *			IP address of the client that sent the request

     *

     */



    public String getRemoteAddr()

    {

        return request.getRemoteAddr();

    }





    /**

     * Returns the fully qualified name of the client that sent the

     * request, or the IP address of the client if the name cannot be

     * determined. For HTTP servlets, same as the value of the CGI variable

     * <code>REMOTE_HOST</code>.

     *

     * @return		a <code>String</code> containing the fully qualified name

     *			of the client

     *

     */



    public String getRemoteHost()

    {

        return request.getRemoteHost();

    }





    /**

     *

     * Stores an attribute in this request.

     * Attributes are reset between requests.  This method is most

     * often used in conjunction with {@link RequestDispatcher}.

     *

     * <p>Attribute names should follow the same conventions as

     * package names. Names beginning with <code>java.*</code>,

     * <code>javax.*</code>, and <code>com.sun.*</code>, are

     * reserved for use by Sun Microsystems.

     *

     *

     * @param name			a <code>String</code> specifying

     *					the name of the attribute

     *

     * @param o				the <code>Object</code> to be stored

     *

     */



    public void setAttribute(String name, Object o)

    {

        request.setAttribute(name, o);

    }





    /**

     *

     * Removes an attribute from this request.  This method is not

     * generally needed as attributes only persist as long as the request

     * is being handled.

     *

     * <p>Attribute names should follow the same conventions as

     * package names. Names beginning with <code>java.*</code>,

     * <code>javax.*</code>, and <code>com.sun.*</code>, are

     * reserved for use by Sun Microsystems.

     *

     *

     * @param name			a <code>String</code> specifying

     *					the name of the attribute to remove

     *

     */



    public void removeAttribute(String name)

    {

        request.removeAttribute(name);

    }





    /**

     *

     * Returns the preferred <code>Locale</code> that the client will

     * accept content in, based on the Accept-Language header.

     * If the client request doesn't provide an Accept-Language header,

     * this method returns the default locale for the server.

     *

     *

     * @return		the preferred <code>Locale</code> for the client

     *

     */



    public Locale getLocale()

    {

        return request.getLocale();

    }





    /**

     *

     * Returns an <code>Enumeration</code> of <code>Locale</code> objects

     * indicating, in decreasing order starting with the preferred locale, the

     * locales that are acceptable to the client based on the Accept-Language

     * header.

     * If the client request doesn't provide an Accept-Language header,

     * this method returns an <code>Enumeration</code> containing one

     * <code>Locale</code>, the default locale for the server.

     *

     *

     * @return		an <code>Enumeration</code> of preferred

     *                  <code>Locale</code> objects for the client

     *

     */



    public Enumeration getLocales()

    {

        return request.getLocales();

    }





    /**

     *

     * Returns a boolean indicating whether this request was made using a

     * secure channel, such as HTTPS.

     *

     *

     * @return		a boolean indicating if the request was made using a

     *                  secure channel

     *

     */



    public boolean isSecure()

    {

        return request.isSecure();

    }





    /**

     *

     * Returns a {@link RequestDispatcher} object that acts as a wrapper for

     * the resource located at the given path.

     * A <code>RequestDispatcher</code> object can be used to forward

     * a request to the resource or to include the resource in a response.

     * The resource can be dynamic or static.

     *

     * <p>The pathname specified may be relative, although it cannot extend

     * outside the current servlet context.  If the path begins with

     * a "/" it is interpreted as relative to the current context root.

     * This method returns <code>null</code> if the servlet container

     * cannot return a <code>RequestDispatcher</code>.

     *

     * <p>The difference between this method and {@link

     * ServletContext#getRequestDispatcher} is that this method can take a

     * relative path.

     *

     * @param path      a <code>String</code> specifying the pathname

     *                  to the resource

     *

     * @return          a <code>RequestDispatcher</code> object

     *                  that acts as a wrapper for the resource

     *                  at the specified path

     *

     * @see             RequestDispatcher

     * @see             ServletContext#getRequestDispatcher

     *

     */



    public RequestDispatcher getRequestDispatcher(String path)

    {

        return request.getRequestDispatcher(path);

    }





    /**

     *

     * @deprecated 	As of Version 2.1 of the Java Servlet API,

     * 			use {@link ServletContext#getRealPath} instead.

     *

     */



    public String getRealPath(String path)

    {

        return request.getRealPath(path);

    }



    ///// Implementation of HttpServletRequest -- may be redundent /////



    /**

     * Returns the name of the authentication scheme used to protect

     * the servlet, for example, "BASIC" or "SSL," or <code>null</code>

     * if the servlet was not protected.

     *

     * <p>Same as the value of the CGI variable AUTH_TYPE.

     *

     *

     * @return		a <code>String</code> specifying the name of

     *			the authentication scheme, or

     *			<code>null</code> if the request was not

     *			authenticated

     *

     */



    public String getAuthType()

    {

        return request.getAuthType();

    }





    /**

     *

     * Returns an array containing all of the <code>Cookie</code>

     * objects the client sent with this request.

     * This method returns <code>null</code> if no cookies were sent.

     *

     * @return		an array of all the <code>Cookies</code>

     *			included with this request, or <code>null</code>

     *			if the request has no cookies

     *

     *

     */



    public Cookie[] getCookies()

    {

        return request.getCookies();

    }





    /**

     *

     * Returns the value of the specified request header

     * as a <code>long</code> value that represents a

     * <code>Date</code> object. Use this method with

     * headers that contain dates, such as

     * <code>If-Modified-Since</code>.

     *

     * <p>The date is returned as

     * the number of milliseconds since January 1, 1970 GMT.

     * The header name is case insensitive.

     *

     * <p>If the request did not have a header of the

     * specified name, this method returns -1. If the header

     * can't be converted to a date, the method throws

     * an <code>IllegalArgumentException</code>.

     *

     * @param name		a <code>String</code> specifying the

     *				name of the header

     *

     * @return			a <code>long</code> value

     *				representing the date specified

     *				in the header expressed as

     *				the number of milliseconds

     *				since January 1, 1970 GMT,

     *				or -1 if the named header

     *				was not included with the

     *				reqest

     *

     * @exception	IllegalArgumentException	If the header value

     *							can't be converted

     *							to a date

     *

     */



    public long getDateHeader(String name)

    {

        return request.getDateHeader(name);

    }





    /**

     *

     * Returns the value of the specified request header

     * as a <code>String</code>. If the request did not include a header

     * of the specified name, this method returns <code>null</code>.

     * The header name is case insensitive. You can use

     * this method with any request header.

     *

     * @param name		a <code>String</code> specifying the

     *				header name

     *

     * @return			a <code>String</code> containing the

     *				value of the requested

     *				header, or <code>null</code>

     *				if the request does not

     *				have a header of that name

     *

     */



    public String getHeader(String name)

    {

        return request.getHeader(name);

    }





    /**

     *

     * Returns all the values of the specified request header

     * as an <code>Enumeration</code> of <code>String</code> objects.

     *

     * <p>Some headers, such as <code>Accept-Language</code> can be sent

     * by clients as several headers each with a different value rather than

     * sending the header as a comma separated list.

     *

     * <p>If the request did not include any headers

     * of the specified name, this method returns an empty

     * <code>Enumeration</code>.

     * The header name is case insensitive. You can use

     * this method with any request header.

     *

     * @param name		a <code>String</code> specifying the

     *				header name

     *

     * @return			a <code>Enumeration</code> containing the

     *				values of the requested

     *				header, or <code>null</code>

     *				if the request does not

     *				have any headers of that name

     *

     */



    public Enumeration getHeaders(String name)

    {

        return request.getHeaders(name);

    }





    /**

     *

     * Returns an enumeration of all the header names

     * this request contains. If the request has no

     * headers, this method returns an empty enumeration.

     *

     * <p>Some servlet containers do not allow do not allow

     * servlets to access headers using this method, in

     * which case this method returns <code>null</code>

     *

     * @return			an enumeration of all the

     *				header names sent with this

     *				request; if the request has

     *				no headers, an empty enumeration;

     *				if the servlet container does not

     *				allow servlets to use this method,

     *				<code>null</code>

     *

     */



    public Enumeration getHeaderNames()

    {

        return request.getHeaderNames();

    }





    /**

     *

     * Returns the value of the specified request header

     * as an <code>int</code>. If the request does not have a header

     * of the specified name, this method returns -1. If the

     * header cannot be converted to an integer, this method

     * throws a <code>NumberFormatException</code>.

     *

     * <p>The header name is case insensitive.

     *

     * @param name		a <code>String</code> specifying the name

     *				of a request header

     *

     * @return			an integer expressing the value

     * 				of the request header or -1

     *				if the request doesn't have a

     *				header of this name

     *

     * @exception	NumberFormatException		If the header value

     *							can't be converted

     *							to an <code>int</code>

     */



    public int getIntHeader(String name)

    {

        return request.getIntHeader(name);

    }





    /**

     *

     * Returns the name of the HTTP method with which this

     * request was made, for example, GET, POST, or PUT.

     * Same as the value of the CGI variable REQUEST_METHOD.

     *

     * @return			a <code>String</code>

     *				specifying the name

     *				of the method with which

     *				this request was made

     *

     */



    public String getMethod()

    {

        return request.getMethod();

    }





    /**

     *

     * Returns any extra path information associated with

     * the URL the client sent when it made this request.

     * The extra path information follows the servlet path

     * but precedes the query string.

     * This method returns <code>null</code> if there

     * was no extra path information.

     *

     * <p>Same as the value of the CGI variable PATH_INFO.

     *

     *

     * @return		a <code>String</code> specifying

     *			extra path information that comes

     *			after the servlet path but before

     *			the query string in the request URL;

     *			or <code>null</code> if the URL does not have

     *			any extra path information

     *

     */



    public String getPathInfo()

    {

        return request.getPathInfo();

    }





    /**

     *

     * Returns any extra path information after the servlet name

     * but before the query string, and translates it to a real

     * path. Same as the value of the CGI variable PATH_TRANSLATED.

     *

     * <p>If the URL does not have any extra path information,

     * this method returns <code>null</code>.

     *

     *

     * @return		a <code>String</code> specifying the

     *			real path, or <code>null</code> if

     *			the URL does not have any extra path

     *			information

     *

     *

     */



    public String getPathTranslated()

    {

        return request.getPathTranslated();

    }





    /**

     *

     * Returns the portion of the request URI that indicates the context

     * of the request.  The context path always comes first in a request

     * URI.  The path starts with a "/" character but does not end with a "/"

     * character.  For servlets in the default (root) context, this method

     * returns "".

     *

     *

     * @return		a <code>String</code> specifying the

     *			portion of the request URI that indicates the context

     *			of the request

     *

     *

     */



    public String getContextPath()

    {

        return request.getContextPath();

    }





    /**

     *

     * Returns the query string that is contained in the request

     * URL after the path. This method returns <code>null</code>

     * if the URL does not have a query string. Same as the value

     * of the CGI variable QUERY_STRING.

     *

     * @return		a <code>String</code> containing the query

     *			string or <code>null</code> if the URL

     *			contains no query string

     *

     */



    public String getQueryString()

    {

        return request.getQueryString();

    }





    /**

     *

     * Returns the login of the user making this request, if the

     * user has been authenticated, or <code>null</code> if the user

     * has not been authenticated.

     * Whether the user name is sent with each subsequent request

     * depends on the browser and type of authentication. Same as the

     * value of the CGI variable REMOTE_USER.

     *

     * @return		a <code>String</code> specifying the login

     *			of the user making this request, or <code>null</code

     *			if the user login is not known

     *

     */



    public String getRemoteUser()

    {

        return request.getRemoteUser();

    }





    /**

     *

     * Returns a boolean indicating whether the authenticated user is included

     * in the specified logical "role".  Roles and role membership can be

     * defined using deployment descriptors.  If the user has not been

     * authenticated, the method returns <code>false</code>.

     *

     * @param role		a <code>String</code> specifying the name

     *				of the role

     *

     * @return		a <code>boolean</code> indicating whether

     *			the user making this request belongs to a given role;

     *			<code>false</code> if the user has not been

     *			authenticated

     *

     */



    public boolean isUserInRole(String role)

    {

        return request.isUserInRole(role);

    }





    /**

     *

     * Returns a <code>java.security.Principal</code> object containing

     * the name of the current authenticated user. If the user has not been

     * authenticated, the method returns <code>null</code>.

     *

     * @return		a <code>java.security.Principal</code> containing

     *			the name of the user making this request;

     *			<code>null</code> if the user has not been

     *			authenticated

     *

     */



    public java.security.Principal getUserPrincipal()

    {

        return request.getUserPrincipal();

    }





    /**

     *

     * Returns the session ID specified by the client. This may

     * not be the same as the ID of the actual session in use.

     * For example, if the request specified an old (expired)

     * session ID and the server has started a new session, this

     * method gets a new session with a new ID. If the request

     * did not specify a session ID, this method returns

     * <code>null</code>.

     *

     *

     * @return		a <code>String</code> specifying the session

     *			ID, or <code>null</code> if the request did

     *			not specify a session ID

     *

     * @see		#isRequestedSessionIdValid

     *

     */



    public String getRequestedSessionId()

    {

        return request.getRequestedSessionId();

    }



    public String getRequestURI()

    {

        return request.getRequestURI();

    }



    public StringBuffer getRequestURL()

    {

        return null;

    }





    /**

     *

     * Returns the part of this request's URL that calls

     * the servlet. This includes either the servlet name or

     * a path to the servlet, but does not include any extra

     * path information or a query string. Same as the value

     * of the CGI variable SCRIPT_NAME.

     *

     *

     * @return		a <code>String</code> containing

     *			the name or path of the servlet being

     *			called, as specified in the request URL

     *

     *

     */



    public String getServletPath()

    {

        return request.getServletPath();

    }





    /**

     *

     * Returns the current <code>HttpSession</code>

     * associated with this request or, if if there is no

     * current session and <code>create</code> is true, returns

     * a new session.

     *

     * <p>If <code>create</code> is <code>false</code>

     * and the request has no valid <code>HttpSession</code>,

     * this method returns <code>null</code>.

     *

     * <p>To make sure the session is properly maintained,

     * you must call this method before

     * the response is committed.

     *

     *

     *

     *

     * @param	create	<code>true</code> to create

     *			a new session for this request if necessary;

     *			<code>false</code> to return <code>null</code>

     *			if there's no current session

     *

     *

     * @return 		the <code>HttpSession</code> associated

     *			with this request or <code>null</code> if

     * 			<code>create</code> is <code>false</code>

     *			and the request has no valid session

     *

     * @see	#getSession()

     *

     *

     */



    public HttpSession getSession(boolean create)

    {

        return request.getSession(create);

    }





    /**

     *

     * Returns the current session associated with this request,

     * or if the request does not have a session, creates one.

     *

     * @return		the <code>HttpSession</code> associated

     *			with this request

     *

     * @see	#getSession(boolean)

     *

     */



    public HttpSession getSession()

    {

        return request.getSession();

    }





    /**

     *

     * Checks whether the requested session ID is still valid.

     *

     * @return			<code>true</code> if this

     *				request has an id for a valid session

     *				in the current session context;

     *				<code>false</code> otherwise

     *

     * @see			#getRequestedSessionId

     * @see			#getSession

     *

     */



    public boolean isRequestedSessionIdValid()

    {

        return request.isRequestedSessionIdValid();

    }





    /**

     *

     * Checks whether the requested session ID came in as a cookie.

     *

     * @return			<code>true</code> if the session ID

     *				came in as a

     *				cookie; otherwise, <code>false</code>

     *

     *

     * @see			#getSession

     *

     */



    public boolean isRequestedSessionIdFromCookie()

    {

        return request.isRequestedSessionIdFromCookie();

    }





    /**

     *

     * Checks whether the requested session ID came in as part of the

     * request URL.

     *

     * @return			<code>true</code> if the session ID

     *				came in as part of a URL; otherwise,

     *				<code>false</code>

     *

     *

     * @see			#getSession

     *

     */



    public boolean isRequestedSessionIdFromURL()

    {

        return request.isRequestedSessionIdFromURL();

    }





    /**

     *

     * @deprecated		As of Version 2.1 of the Java Servlet

     *				API, use {@link #isRequestedSessionIdFromURL}

     *				instead.

     *

     */



    public boolean isRequestedSessionIdFromUrl()

    {

        return request.isRequestedSessionIdFromURL();

    }



    public MultipartHandler getMultipartHandler()

    {

        return multipartHandler;

    }



    public void setMultipartHandler( MultipartHandler multipartHandler )

    {

        this.multipartHandler = multipartHandler;

    }





}

