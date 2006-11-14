/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.commerce.shop.manager;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.log4j.Logger;

/**
 * @author Sergei Maslyukov
 *         Date: 16.06.2006
 *         Time: 17:28:07
 */
public class PriceUploadForm {
    private final static Logger log = Logger.getLogger(PriceUploadForm.class);

    private boolean success=false;
    private UploadedFile _upFile=null;
    private String _name=null;

    public PriceUploadForm() {
        _name = "";
    }

    public UploadedFile getUpFile() {
        return _upFile;
    }

    public void setUpFile(UploadedFile upFile) {
        _upFile = upFile;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @SuppressWarnings({"unchecked"})
    public String upload() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Start upload file");
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getApplicationMap().put("fileupload_bytes", _upFile.getBytes());
        facesContext.getExternalContext().getApplicationMap().put("fileupload_type", _upFile.getContentType());
        facesContext.getExternalContext().getApplicationMap().put("fileupload_name", _upFile.getName());

        success=true;

        if (log.isDebugEnabled()) {
            log.debug("Done upload file");
            log.debug("uploaded file: " + _upFile);
            if (_upFile!=null) {
                log.debug("    bytes: " + _upFile.getBytes().length);
                log.debug("    type: " + _upFile.getContentType());
                log.debug("    name: " + _upFile.getName());
            }
        }

        return "ok";
    }

    public boolean isUploaded() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getExternalContext().getApplicationMap().get("fileupload_bytes") != null;
    }
}