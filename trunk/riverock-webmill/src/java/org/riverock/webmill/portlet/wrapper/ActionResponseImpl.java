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



/**

 * User: Admin

 * Date: Sep 20, 2003

 * Time: 1:02:30 AM

 *

 * $Id$

 */

package org.riverock.webmill.portal.impl;



import java.io.IOException;

import java.util.Map;



import javax.portlet.ActionResponse;

import javax.portlet.PortletMode;

import javax.portlet.PortletModeException;

import javax.portlet.WindowState;

import javax.portlet.WindowStateException;



public class ActionResponseImpl implements ActionResponse

{

    public void addProperty( String encoding, String encoding1 )

    {

    }



    public void setProperty( String encoding, String encoding1 )

    {

    }



    public String encodeURL( String encoding )

    {

        return null;

    }



    public void setWindowState( WindowState windowState ) throws WindowStateException

    {

    }



    public void setPortletMode( PortletMode portletMode ) throws PortletModeException

    {

    }



    public void sendRedirect( String encoding ) throws IOException

    {

    }



    public void setRenderParameters( Map map )

    {

    }



    public void setRenderParameter( String encoding, String encoding1 )

    {

    }



    public void setRenderParameter( String encoding, String[] strings )

    {

    }

}

