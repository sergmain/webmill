/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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