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

 * Date: Aug 21, 2003

 * Time: 11:45:29 PM

 *

 * $Id$

 */

package org.riverock.portlet.portlets;



import java.io.Writer;



import org.riverock.common.tools.ExceptionTools;



import org.apache.log4j.Logger;



public class WebmillErrorPage

{

    private static Logger log = Logger.getLogger( "org.riverock.portlet.WebmillErrorPage" );



    public static void process( Writer out, Throwable th, String errorMessage, String url, String urlMessage )

    {

        if ( out==null )

            return;



        try

        {

            out.write(

                "<html><head></head><body>\n"+

                errorMessage+"<br>\n"+

                "<p><a href=\""+url+"\">"+urlMessage+"</a></p>\n"

            );

            if ( th!=null )

            {

                out.write(

                    "<span style=\"font-family: verdana,arial,helvetica,sans-serif; font-size: 10px;\">\n"+

                    ExceptionTools.getStackTrace( th, 20, "<br>" )+"<br>"+

                    "</span>\n"

                );

            }

            out.write(

                "</body></html>"

            );

        }

        catch (Error e)

        {

            log.error( "Error while create error page", e );

        }

        catch (Exception e)

        {

            log.error( "Exception while create error page", e );

        }

    }



}

