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
 * Author: mill
 * Date: Apr 2, 2003
 * Time: 4:37:39 PM
 *
 * $Id$
 */

package org.riverock.portlet.test;

import org.riverock.common.html.AcceptLanguageWithLevel;
import org.riverock.common.html.Header;

import org.apache.log4j.Logger;

public class TestAcceptLanguage
{
    private static Logger cat = Logger.getLogger( "org.riverock.portlet.test.TestAcceptLanguage" );

    public TestAcceptLanguage()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        AcceptLanguageWithLevel[] accept =
            Header.getAcceptLanguageArray("ru,en;q=0.8,en-gb;q=0.5,ja;q=0.33");

        for (int i=0; i<accept.length; i++)
        {
            System.out.println( "Locale - "+accept[i].locale+" level - "+accept[i].level );
        }
    }
}
