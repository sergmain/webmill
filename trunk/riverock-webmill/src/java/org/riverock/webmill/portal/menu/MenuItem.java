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



package org.riverock.webmill.portal.menu;



import java.util.ArrayList;

import java.util.List;



import org.riverock.generic.port.LocalizedString;



/**

 *

 * $Id$

 *

 */

public class MenuItem implements MenuItemInterface

{

    private Long id = null;

    private Long id_top = null;

    private Long id_portlet = null;

    private String nameTemplate = null;

    private String type = "";

    private LocalizedString str = null;

    private List catalogItems = new ArrayList(0);  // List of MenuItem

    private String url = null;



    protected void finalize() throws Throwable

    {

        setStr(null);

        setType(null);

        setNameTemplate(null);

        setUrl(null);

        if (getCatalogItems() != null)

        {

            getCatalogItems().clear();

            setCatalogItems(null);

        }



        super.finalize();

    }



    public MenuItem(Long id_, Long id_top_, Long id_portlet_, String type_, LocalizedString str_,

                       String nameTemplate_, String url_ )

    {

        this.setId(id_);

        this.setIdTop(id_top_);

        this.setIdPortlet(id_portlet_);

        this.setType(type_);

        this.setStr(str_);

        this.setNameTemplate(nameTemplate_);

        this.setUrl(url_);

    }



    public Long getId()

    {

        return id;

    }



    public void setId(Long id)

    {

        this.id = id;

    }



    public Long getIdTop()

    {

        return id_top;

    }



    public void setIdTop(Long id_top)

    {

        this.id_top = id_top;

    }



    public Long getIdPortlet()

    {

        return id_portlet;

    }



    public void setIdPortlet(Long id_portlet)

    {

        this.id_portlet = id_portlet;

    }



    public String getNameTemplate()

    {

        return nameTemplate;

    }



    public void setNameTemplate(String nameTemplate)

    {

        this.nameTemplate = nameTemplate;

    }



    public String getType()

    {

        return type;

    }



    public void setType(String type)

    {

        this.type = type;

    }



    public LocalizedString getStr()

    {

        return str;

    }



    public void setStr(LocalizedString str)

    {

        this.str = str;

    }



    public List getCatalogItems()

    {

        return catalogItems;

    }



    public void setCatalogItems(List catalogItems)

    {

        this.catalogItems = catalogItems;

    }



    public String getUrl()

    {

        return url;

    }



    public void setUrl(String url)

    {

        this.url = url;

    }



}