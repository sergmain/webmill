/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.portlet.shop.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.price.ImportPriceList;
import org.riverock.portlet.price.Shop;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.webmill.container.ContainerConstants;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 10:22:19 PM
 * <p/>
 * $Id$
 */
public final class UploadPricePortlet implements Portlet {
    private final static Logger log = Logger.getLogger(UploadPricePortlet.class);

    public final static String UPLOAD_FILE_PARAM_NAME = "f";
    public static final String ERROR_TEXT = "ERROR_TEXT";
    public static final String ERROR_URL = "ERROR_URL";

    public void init(PortletConfig portletConfig) {
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {

        DatabaseAdapter db_ = null;
        try {
            boolean isMultiPart = PortletFileUpload.isMultipartContent(actionRequest);
            if (log.isDebugEnabled()) {
                log.debug("isMultiPart: " + isMultiPart);
            }


            if (!isMultiPart) {
                actionResponse.setRenderParameter( ERROR_TEXT, "Request is not multi-part");
                return;
            }

            db_ = DatabaseAdapter.getInstance();

            PricesType prices = null;
            try {

// Create a factory for disk-based file items
                FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
                PortletFileUpload upload = new PortletFileUpload(factory);

// Set overall request size constraint
                upload.setSizeMax(1024 * 1024);

// Parse the request
                List /* FileItem */ items = upload.parseRequest(actionRequest);

                Iterator iter = items.iterator();
// process only one file with data
                if (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    if (!item.isFormField()) {
                        log.debug("uploaded file founded");
                        InputStream uploadedStream = item.getInputStream();

                        InputSource inSrc = new InputSource(uploadedStream);
                        prices = (PricesType) Unmarshaller.unmarshal(PricesType.class, inSrc);

                        uploadedStream.close();

                    }
                }

            }
            catch (Exception e) {
                log.error("Exception parse of uploaded file with data", e);
                actionResponse.setRenderParameter(
                    ERROR_TEXT,
                    "Exception parse uploaded file with data " + getErrorMessage(e)
                );
                actionResponse.setRenderParameter(ERROR_URL, "загрузить повторно");

                return;
            }

            try {
                Long siteId = new Long(actionRequest.getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
                if (log.isDebugEnabled()) {
                    log.debug("#55.01.15 idSite -  " + siteId);
                }

                ImportPriceList.process(prices, siteId, db_);
                // reinit Shop in cache. need for correct output date/time of upload price
                Shop.reinit();
            }
            catch (Exception e) {
                log.error("Exception store price data in DB", e);

                actionResponse.setRenderParameter(
                    ERROR_TEXT,
                    "Exception store price data in DB " + getErrorMessage(e)
                );
                actionResponse.setRenderParameter(ERROR_URL, "загрузить повторно");
            }
        }
        catch (Exception e) {
            log.error("General exception import price-list", e);
            actionResponse.setRenderParameter(
                ERROR_TEXT,
                "General exception inport price-list " + getErrorMessage(e)
            );
            actionResponse.setRenderParameter(ERROR_URL, "загрузить повторно");
        }
        finally {
            DatabaseAdapter.close(db_);
        }
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        ContentTypeTools.setContentType(renderResponse, ContentTypeTools.CONTENT_TYPE_UTF8);
        Writer out = renderResponse.getWriter();
        if (renderRequest.getParameter(ERROR_TEXT) != null) {
            out.write(renderRequest.getParameter(ERROR_TEXT) );
        }

        PortletURL url = renderResponse.createActionURL();

        out.write(
            "<form method=\"POST\" action=\"" + renderResponse.encodeURL(url.toString()) + "\" ENCTYPE=\"multipart/form-data\">" +
                "Внимание! Файл импорта прайс-листа должен быть в корректном XML формате<br>" +
                "Если загрузка прайс-листа прошла успешно, то дополнительных сообщений выдано не будет" +
                "<p>" +
                "<input type=\"FILE\" name=\"" + UPLOAD_FILE_PARAM_NAME + "\" size=\"50\">" +
                "<br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</p>" +
                "</form>");

    }

    public void destroy() {
    }

    private String getErrorMessage( Throwable th ) {
        return
            "<br><span style=\"font-family: verdana,arial,helvetica,sans-serif; font-size: 10px;\">\n"+
            ExceptionTools.getStackTrace( th, 40, "<br>" )+"\n"+
            "</span><br>\n";
    }

}
