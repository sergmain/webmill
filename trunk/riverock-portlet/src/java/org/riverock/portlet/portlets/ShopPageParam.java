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



package org.riverock.portlet.portlets;



import org.riverock.generic.tools.StringManager;

import org.riverock.generic.site.SiteListSite;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;



/**

 *

 * $Author$

 *

 * $Id$

 *

 */

public class ShopPageParam

{

//    private static Logger cat = Logger.getLogger("org.riverock.portlet.ShopPageParam" );



    public Long id_shop = null;

    public Long id_group = null;

    public String nameTemplate = null;

    private String serverName = null;

    public Long idSite = null;

    public Long id_currency;

    public String currencyURL = null;

    public StringManager sm = null;

    public boolean  isProcessInvoice =  false;



    public CurrencyPrecisionType precision = null;



    public String sortBy = null;

    public int sortDirect = 0;



    protected void finalize() throws Throwable

    {

        nameTemplate = null;

        setServerName(null);

        currencyURL = null;

        precision = null;

        sm  = null;



        super.finalize();

    }



    public ShopPageParam()

    {

    }



    public void setServerName( String serverName_ )

        throws Exception

    {

        this.serverName = serverName_;

        this.idSite = SiteListSite.getIdSite( this.serverName );

    }



    public String getServerName()

    {

        return serverName;

    }

}