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

 * Date: Jan 10, 2003

 * Time: 2:20:21 PM

 *

 * $Id$

 */



package org.riverock.portlet.member;



import java.util.List;

import java.util.ArrayList;



import javax.servlet.http.HttpServletRequest;



import org.apache.log4j.Logger;



import org.riverock.generic.tools.StringManager;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.webmill.port.PortalInfo;



public class ArticleXmlTemplateClassQuery  extends BaseClassQuery

{

    private static Logger cat = Logger.getLogger("org.riverock.member.ArticleXmlTemplateClassQuery");



    public ArticleXmlTemplateClassQuery()

    {

    }



    /**

     * ���������� ������� �������� ��� ����������� �� ���-��������

     * @return String

     */

    public String getCurrentValue(HttpServletRequest request) throws Exception

    {

        DatabaseAdapter db_ = DatabaseAdapter.getInstance( false );

        PortalInfo p = PortalInfo.getInstance(db_, request.getServerName() );

        StringManager sm = StringManager.getManager("mill.locale.main", p.defaultLocale);



        return sm.getStr("yesno.no");

    }



    /**

     *  ���������� ������ ��������� �������� ��� ���������� <select> ��������

     * @return Vector of org.riverock.member.ClassQueryItem

     */

    public List getSelectList(HttpServletRequest request)

        throws Exception

    {

        List v = new ArrayList();

        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance( false );

            PortalInfo p = PortalInfo.getInstance(db_, request.getServerName() );

            StringManager sm = StringManager.getManager("mill.locale.main", p.defaultLocale);



            ClassQueryItem item = new ClassQueryItem(new Long(0), sm.getStr("yesno.no") );



            item.isSelected = true;



            v.add( item );

            return v;

        }

        finally

        {

            DatabaseManager.close(db_);

            db_ = null;

        }

    }



    MemberQueryParameter param = null;



    public void setQueryParameter(MemberQueryParameter parameter) throws Exception

    {

        this.param = parameter;

    }

}

