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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


public abstract class MaybeUploadServlet extends javax.servlet.http.HttpServlet
{
    /** file system local path to where I unpack files I have uploaded */
    protected String uploadDirPath = null;

    /** the path to my upload directory (work directory) within the
     *  document root of the web server, if it is within the document
     *  root of the web server, else null. For example, if
     *  <code>uploadDirPath</code> was
     *  <samp>"/home/httpd/htdocs/upload"</samp>, and the document
     *  root of the Web server was <samp>/home/httpd/htdocs</samp>,
     *  then it would make sense to have <code>uploadDirURL</code> set
     *  to <samp>"/upload/"</samp> */
    protected String uploadDirURL = null;

    /** whether to allow uploaded files to be overwritten when new
     *  files are uploaded; default is we don't */
    protected boolean allowOverwrite = false;

    /** whether or not to rename uploaded files to prevent name
     *  collisions; default is we do */
    protected boolean silentlyRename = true;

    /** the maximum upload size: by default, half a megabyte. */
    protected int maxUpload = 524288;

    /** the actual upload directory as a file object */
    protected File uploadDir;

    /** whether or not to save uploads directly to disk; default is we do */
    protected boolean saveUploadedFilesToDisk = true;

    /* private static final stuff copied from HttpServlet - shame it
       wasn't 'protected' */
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_TRACE = "TRACE";

    private static final String HEADER_IFMODSINCE = "If-Modified-Since";
    private static final String HEADER_LASTMOD = "Last-Modified";

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);	// superclass initialisation *first*

        uploadDirPath = getStringParameterValue("upload_dir_path", config,
            uploadDirPath);

        uploadDirURL = getStringParameterValue("upload_dir_url", config,
            uploadDirURL);

        allowOverwrite = getBooleanParameterValue("allow_overwrite", config,
            allowOverwrite);

        silentlyRename = getBooleanParameterValue("silently_rename", config,
            silentlyRename);

        saveUploadedFilesToDisk =
            getBooleanParameterValue("save_uploaded", config,
                saveUploadedFilesToDisk);

        maxUpload = getIntParameterValue("max_upload", config,
            maxUpload);

        if (uploadDirPath == null)
            uploadDir = (File)
                config.getServletContext().getAttribute(
                    "javax.servlet.context.tempdir");
        else
            uploadDir = new File(uploadDirPath);

        if (!uploadDir.isDirectory() || !uploadDir.canWrite())
            throw new
                UnavailableException("Cannot write to upload directory " +
                uploadDirPath);

    }


    /** Unpack a named parameter from the servlet config and return
     *  the value as a String. Context-wide values are less closely
     *  binding than servlet specific values.
     *
     *  @param name the name of the parameter to fetch
     *  @param config the configuration to fetch it from
     *  @param dflt the value to return if no parameter found
     */
    private String getStringParameterValue(String name, ServletConfig config,
                                           String dflt)
    {
        String result = dflt;

        String v = config.getServletContext().getInitParameter(name);
        if (v != null)
            result = v;		// context overrides hard-coded default

        v = config.getInitParameter(name);
        if (v != null)
            result = v;		// servlet specific value overrides context

        return result;
    }

    /** Unpack a named parameter from the servlet config and return
     *  the value as a boolean. Context-wide values are less closely
     *  binding than servlet specific values.
     *
     *  @param name the name of the parameter to fetch
     *  @param config the configuration to fetch it from
     *  @param dflt the value to return if no parameter found
     */
    private boolean getBooleanParameterValue(String name,
                                             ServletConfig config,
                                             boolean dflt)
    {
        boolean result = dflt;

        String v = config.getServletContext().getInitParameter(name);
        if (v != null)
            result = Boolean.valueOf(v).booleanValue();
        // context overrides hard-coded default

        v = config.getInitParameter(name);
        if (v != null)
            result = Boolean.valueOf(v).booleanValue();
        // servlet specific value overrides context

        return result;
    }

    /** Unpack a named parameter from the servlet config and return
     *  the value as an int. Context-wide values are less closely
     *  binding than servlet specific values.
     *
     *  @param name the name of the parameter to fetch
     *  @param config the configuration to fetch it from
     *  @param dflt the value to return if no parameter found
     */
    private int getIntParameterValue(String name,
                                     ServletConfig config,
                                     int dflt)
    {
        int result = dflt;

        String v = config.getServletContext().getInitParameter(name);
        if (v != null)
            result = Integer.parseInt(v);
        // context overrides hard-coded default

        v = config.getInitParameter(name);
        if (v != null)
            result = Integer.parseInt(v);
        // servlet specific value overrides context

        return result;
    }


    /** Service a request. This method heavily dependent on the
     *  reference implementation.
     *
     *  @param req the request to be serviced
     *  @param resp the response being constructed to this request
     */
    public void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        MaybeUploadRequestWrapper wrapper = new
            MaybeUploadRequestWrapper(req, saveUploadedFilesToDisk,
                uploadDir, allowOverwrite,
                silentlyRename, maxUpload);

        String method = req.getMethod();

        if (method.equals(METHOD_GET))
        {
            long lastModified = getLastModified(req);
            if (lastModified == -1)
            {
                // servlet doesn't support
                // if-modified-since, no reason to go
                // through further expensive logic
                doGet(wrapper, resp);
            }
            else
            {
                long ifModifiedSince =
                    req.getDateHeader(HEADER_IFMODSINCE);

                if (ifModifiedSince < (lastModified / 1000 * 1000))
                {
                    // If the servlet mod time is later,
                    // call doGet( ) Round down to the
                    // nearest second for a proper compare
                    // A ifModifiedSince of -1 will always
                    // be less
                    maybeSetLastModified(resp, lastModified);
                    doGet(wrapper, resp);
                }
                else
                {
                    resp.setStatus(
                        HttpServletResponse.SC_NOT_MODIFIED);
                }
            }

        }
        else if (method.equals(METHOD_HEAD))
        {
            long lastModified = getLastModified(req);
            maybeSetLastModified(resp, lastModified);
            doHead(wrapper, resp);

        }
        else if (method.equals(METHOD_POST))
        {
            doPost(wrapper, resp);
        }
        else if (method.equals(METHOD_PUT))
        {
            doPut(wrapper, resp);
        }
        else if (method.equals(METHOD_DELETE))
        {
            doDelete(wrapper, resp);
        }
        else if (method.equals(METHOD_OPTIONS))
        {
            doOptions(wrapper, resp);
        }
        else if (method.equals(METHOD_TRACE))
        {
            doTrace(wrapper, resp);
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED,
                new String("HTTP Method [" + method +
                "] is not implemented"));
        }
    }


    /** Sets the Last-Modified entity header field, if it has not
     *  already been set and if the value is meaningful.  Called before
     *  doGet, to ensure that headers are set before response data is
     *  written.  A subclass might have set this header already, so we
     *  check. Copied in it's entirety from the refernce implementation.
     */

    private void maybeSetLastModified(HttpServletResponse resp,
                                      long lastModified)
    {
        if (resp.containsHeader(HEADER_LASTMOD))
            return;
        if (lastModified >= 0)
            resp.setDateHeader(HEADER_LASTMOD, lastModified);
    }


    /** Simple wrapper round HttpServlet.doGet( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doGet(MaybeUploadRequestWrapper req,
                         HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doGet(req, resp);
    }


    /** Simple wrapper round HttpServlet.doPost( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doPost(MaybeUploadRequestWrapper req,
                          HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doPost(req, resp);
    }


    /** Simple wrapper round HttpServlet.doDelete( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doDelete(MaybeUploadRequestWrapper req,
                            HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doDelete(req, resp);
    }


    /** Simple wrapper round HttpServlet.doPut( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doPut(MaybeUploadRequestWrapper req,
                         HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doPut(req, resp);
    }


    /** Simple wrapper round HttpServlet.doOptions( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doOptions(MaybeUploadRequestWrapper req,
                             HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doOptions(req, resp);
    }


    /** Simple wrapper round HttpServlet.doTrace( ), so that you can
     *  depend on having a MaybeUploadRequestWrapper in your code.
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response
     */
    protected void doTrace(MaybeUploadRequestWrapper req,
                           HttpServletResponse resp)
        throws ServletException, IOException
    {
        super.doTrace(req, resp);
    }

    /** doHead is a bit more tricky. not really handled in this
     *  version, although I'll happily integrate code from anyone else
     *  who is sufficiently confident to write an
     *  implementation. Could just copy the HttpServlet version,
     *  but...
     *
     *  @param req a request wrapper which know how to handle upload
     *  @param resp a standard servlet response */
    protected void doHead(MaybeUploadRequestWrapper req,
                          HttpServletResponse resp)
        throws ServletException, IOException
    {
        String protocol = req.getProtocol();
        String msg = "HTTP method HEAD not supported";
        if (protocol.endsWith("1.1"))
        {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }

    }

    /** @return my upload directory. Note that the upload directory
     *  cannot be set dynamically at run-time for security reasons */
    public File getUploadDir()
    {
        return uploadDir;
    }

    /** @return as a String, the base URL for my upload directory if
     *  any. If returned, may be an absolute URL or a URL fragment
     *  relative to the document root of the HTTP server hosting the
     *  Servlet engine. In null, means that you cannot access the
     *  upload directory via HTTP */
    public String getUploadURL()
    {
        return uploadDirURL;
    }
}
