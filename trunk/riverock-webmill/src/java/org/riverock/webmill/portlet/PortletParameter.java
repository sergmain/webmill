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



package org.riverock.webmill.portlet;



import java.util.List;



import javax.servlet.http.HttpServletResponse;

import javax.portlet.PortletRequest;



import org.riverock.generic.tools.StringManager;

import org.riverock.webmill.schema.site.SiteTemplateParameterType;

import org.riverock.webmill.schema.site.TemplateItemType;





/**

 *

 * $Author$

 *

 * $Id$

 *

 */

public class PortletParameter

{

    // global parameters for page

//    private HttpServletRequest request = null;

    private HttpServletResponse response = null;

    private String ctxType = null;



    private String nameTemplate = null;



    // parameters for current portlet

    private StringManager sm  = null;

    private String portletCode = null;

    private List templateParameters = null;

    private PortletRequest portletRequest = null;;





    protected void finalize() throws Throwable

    {



        sm = null;



        super.finalize();

    }



    public PortletParameter(

        CtxInstance ctxInstance,

        String localePackage_,

        TemplateItemType templateItem

        )

    {

//        this.request = ctxInstance.req ;

        this.response = ctxInstance.getResponse() ;

        this.ctxType = ctxInstance.getDefaultPortletType();

        this.portletRequest = ctxInstance.getPortletRequest();

        this.nameTemplate = ctxInstance.getNameTemplate();



        this.portletCode = templateItem.getCode();

        if (localePackage_ != null)

            this.sm = StringManager.getManager(localePackage_, portletRequest.getLocale() );

        else

            this.sm = ctxInstance.sCustom;



        if (templateItem.getParameterAsReference()!=null) {



            List parameters_ = templateItem.getParameterAsReference();

            this.templateParameters = null;

            if (templateItem!=null && parameters_.size()!=0 &&

                parameters_.get(0) instanceof SiteTemplateParameterType

            )

                this.templateParameters = parameters_;

        }

    }



//    public HttpServletRequest getRequest() {

//        return request;

//    }



    public HttpServletResponse getResponse() {

        return response;

    }



    public String getCtxType() {

        return ctxType;

    }



    public StringManager getSm() {

        return sm;

    }



    public String getPortletCode() {

        return portletCode;

    }



    public List getTemplateParameters() {

        return templateParameters;

    }



    public PortletRequest getPortletRequest()

    {

        return portletRequest;

    }



    public String getNameTemplate()

    {

        return nameTemplate;

    }



}