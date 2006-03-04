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
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author Serge Maslyukov
 *         $Id$
 */
public class PriceUploadAction {
    private final static Logger log = Logger.getLogger( PriceUploadAction.class );

	private AuthSessionBean authSessionBean = null;
	private PriceUploadSessionBean priceUploadSessionBean = null;

    public PriceUploadAction() {
    }

	public AuthSessionBean getAuthSessionBean() {
		return authSessionBean;
	}

	public void setAuthSessionBean(AuthSessionBean authSessionBean) {
		this.authSessionBean = authSessionBean;
	}

	public PriceUploadSessionBean getPriceUploadSessionBean() {
		return priceUploadSessionBean;
	}

	public void setPriceUploadSessionBean(PriceUploadSessionBean priceUploadSessionBean) {
		this.priceUploadSessionBean = priceUploadSessionBean;
	}

   public String simpleAction() {
	return "ok";
   }

   public String counterAction() {
	priceUploadSessionBean.setCounter( priceUploadSessionBean.getCounter() + 1 );
	return "ok";
   }

   public String upload() {
       priceUploadSessionBean.setUploaded( true );
       FacesContext facesContext = FacesContext.getCurrentInstance();

      	Object obj = facesContext.getExternalContext().getRequest();
	if (log.isDebugEnabled()) {
	   log.error("Actual object type: " + obj.getClass().getName() );
	}

       if( !( obj instanceof ActionRequest ) ) {
	   log.error("obj is not intance of ActionRequest. Actual object type: " + obj.getClass().getName() );
           throw new IllegalStateException( "request is not ActionRequest type" );
       }

       ActionRequest actionRequest = ( ActionRequest ) obj;
       DatabaseAdapter db_ = null;
       try {
           boolean isMultiPart = PortletFileUpload.isMultipartContent( actionRequest );
           if( log.isDebugEnabled() ) {
               log.debug( "isMultiPart: " + isMultiPart );
           }

           if( !isMultiPart ) {
       		priceUploadSessionBean.setSuccess( false );
               return "error";
           }

           AuthSession auth_ = getAuthSessionBean().getAuthSession();
           if( auth_ == null || !auth_.isUserInRole( "webmill.upload_price_list" ) ) {
               throw new PortletSecurityException( "You have not right to upload price on this site" );
           }

           db_ = DatabaseAdapter.getInstance();

           PricesType prices = null;
           try {

		log.debug("Create DiskFileItemFactory()");
// Create a factory for disk-based file items
               FileItemFactory factory = new DiskFileItemFactory();

// Create a new file upload handler
               PortletFileUpload upload = new PortletFileUpload( factory );

// Set overall request size constraint
               upload.setSizeMax( 1024 * 1024 );

		log.debug("Start parse request");
// Parse the request
               List /* FileItem */ items = upload.parseRequest( actionRequest );

		log.debug("List with FileItem: "+items+", count items: "+ (items!=null?""+items.size():" is null"));
               Iterator iter = items.iterator();
// process only one file with data
               if( iter.hasNext() ) {
                   FileItem item = ( FileItem ) iter.next();

                   if( !item.isFormField() ) {
                       log.debug( "uploaded file founded" );
                       InputStream uploadedStream = item.getInputStream();

                       InputSource inSrc = new InputSource( uploadedStream );
                       prices = ( PricesType ) Unmarshaller.unmarshal( PricesType.class, inSrc );

                       uploadedStream.close();

                   }
               }

           }
           catch( Exception e ) {
               log.error( "Exception parse of uploaded file with data", e );
       		priceUploadSessionBean.setSuccess( false );
               return "error";
           }
           log.debug("Done parse uploaded xml file");

           try {
               Long siteId = new Long(
                   actionRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
               if( log.isDebugEnabled() ) {
                   log.debug( "#55.01.15 idSite -  " + siteId );
               }

               ImportPriceList.process( prices, siteId, db_ );
               // reinit Shop in cache. need for correct output date/time of upload price
               Shop.reinit();
           }
           catch( Exception e ) {
               log.error( "Exception store price data in DB", e );
       		priceUploadSessionBean.setSuccess( false );
               return "error";
           }

           return "ok";
       }
       catch( Exception e ) {
           log.error( "General exception import price-list", e );
       		priceUploadSessionBean.setSuccess( false );
           return "error";
       }
       finally {
           DatabaseAdapter.close( db_ );
           db_ = null;
       }
   }
}
