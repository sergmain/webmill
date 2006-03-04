/*
 * org.riverock.webmill -- Portal framework implementation
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
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.PortletSecurityException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.price.ImportPriceList;
import org.riverock.portlet.price.Shop;
import org.riverock.portlet.schema.import_price.PricesType;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Serge Maslyukov
 *         $Id$
 */
public class PriceUploadSessionBean {
    private final static Logger log = Logger.getLogger( PriceUploadSessionBean.class );

    private boolean isSuccess = true;
    private boolean isUploaded = false;
    private UploadedFile upFile;
private String name = null;
private int counter = 0;

    public PriceUploadSessionBean() {
	log.debug("Create instance of PriceUploadSessionBean class");
    }

    public int getCounter() {
	return counter;
	}

public void setCounter( int counter ) {
	this.counter = counter;
}	


    public UploadedFile getUpFile() {
        return upFile;
    }

    public void setUpFile( UploadedFile upFile ) {
        this.upFile = upFile;
    }

public String getName() {
	return this.name;
}

public void setName( String name ) {
	this.name=name;
}

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess( boolean success ) {
        isSuccess = success;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded( boolean uploaded ) {
        isUploaded = uploaded;
    }
}
