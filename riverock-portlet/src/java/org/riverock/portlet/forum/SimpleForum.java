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



package org.riverock.portlet.forum;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.schema.db.CustomSequenceType;

import org.riverock.webmill.port.InitPage;



public class SimpleForum extends Forum

{



    public SimpleForum(javax.servlet.http.HttpServletRequest request,

                       javax.servlet.http.HttpServletResponse response,

                       InitPage jspPage)

            throws ForumException

    {

        super(request, response, jspPage);

    }



    public SimpleForum()

    {

    }



    public ForumMessage getForumMessage(DatabaseAdapter db__, Long id__)

            throws ForumException

    {

        try

        {

            return SimpleForumMessage.getInstance(db__, id__);

        }

        catch (Exception e)

        {

            throw new ForumException(e.toString());

        }

    }



}

