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



/**

 * User: Admin

 * Date: Sep 20, 2003

 * Time: 1:00:54 AM

 *

 * $Id$

 */

package org.riverock.portlet.portlets.wrapper;



import java.io.IOException;

import java.io.OutputStream;

import java.io.PrintWriter;

import java.util.Locale;



import javax.portlet.RenderResponse;

import javax.portlet.PortletURL;



public class RenderResponseWrapper implements RenderResponse

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



    public String getContentType()

    {

        return null;

    }



    public PortletURL createRenderURL()

    {

        return null;

    }



    public PortletURL createActionURL()

    {

        return null;

    }



    public String getNamespace()

    {

        return null;

    }



    public void setTitle( String encoding )

    {

    }



    public void setContentType( String encoding )

    {

    }



    public String getCharacterEncoding()

    {

        return null;

    }



    public PrintWriter getWriter() throws IOException

    {

        return null;

    }



    public Locale getLocale()

    {

        return null;

    }



    public void setBufferSize( int i )

    {

    }



    public int getBufferSize()

    {

        return 0;

    }



    public void flushBuffer() throws IOException

    {

    }



    public void resetBuffer()

    {

    }



    public boolean isCommitted()

    {

        return false;

    }



    public void reset()

    {

    }



    public OutputStream getPortletOutputStream() throws IOException

    {

        return null;

    }

}

