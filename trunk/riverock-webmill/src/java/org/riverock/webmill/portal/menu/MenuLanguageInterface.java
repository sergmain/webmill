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



/**

 * User: serg_main

 * Date: 10.04.2004

 * Time: 19:24:57

 * @author Serge Maslyukov

 * $Id$

 */

public interface MenuLanguageInterface

{

    int getMenuCount();



    MenuInterface getMenu(int index)

            throws IndexOutOfBoundsException;



    String getIndexTemplate();



    MenuInterface getDefault();



    MenuInterface getCatalogByCode( String code );



    MenuItemInterface searchMenuItem(Long id);



    String getLocaleStr();



    String getLang();



    void reinit();

}

