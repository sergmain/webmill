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

 * Date: Nov 24, 2002

 * Time: 3:06:57 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import java.util.List;



import javax.servlet.http.HttpServletRequest;



import org.riverock.portlet.member.MemberQueryParameter;



public abstract class BaseClassQuery

{

/**

 * ¬озвращает текущее значение дл€ отображени€ на веб-странице

 * @return String

 */

    public abstract String getCurrentValue(HttpServletRequest request) throws Exception;



/**

 *  ¬озвращает список возможных значений дл€ построени€ <select> элемента

  * @return Vector of org.riverock.member.ClassQueryItem

 */

    public abstract List getSelectList(HttpServletRequest request) throws Exception;



    public abstract void setQueryParameter(MemberQueryParameter parameter) throws Exception;

}

