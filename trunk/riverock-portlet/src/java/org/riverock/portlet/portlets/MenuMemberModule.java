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



package org.riverock.portlet.portlets;



import java.sql.ResultSet;



import org.riverock.common.tools.RsetTools;



import org.apache.log4j.Logger;



/**

 *

 * $Author$

 *

 * $Id$

 *

 */

public class MenuMemberModule

//    implements Portlet, PortletGetList

{

    private static Logger cat = Logger.getLogger( MenuMemberModule.class );



    /**

     module - название модуля

     */

    public String moduleName = "";



    public String moduleCode = null;

    public String applicationCode = null;



    /**

     order - значение для порядка вывода модуля

     */

    public int order = 0;

    public int isNew = -1;

    public int modRecordNumber = 0;

    public int applRecordNumber = 0;

//    public PortletParameter param = null;



    protected void finalize() throws Throwable

    {

        moduleName = null;

//        param = null;



        applicationCode = null;

        moduleCode = null;



        super.finalize();

    }

/*

     public void setParameter(PortletParameter param_)

    {

        this.param = param_;

    }



    public boolean isXml(){ return false; }

    public boolean isHtml(){ return false; }

*/

    public MenuMemberModule()

    {

    }





    public MenuMemberModule(ResultSet rs, String applicationCode_)

            throws Exception

    {

        this.applicationCode= applicationCode_;

        try

        {

            moduleName = RsetTools.getString(rs, "NAME_OBJECT_ARM");

            moduleCode = RsetTools.getString(rs, "CODE_OBJECT_ARM");

            order = RsetTools.getInt(rs, "ORDER_FIELD", new Integer(0)).intValue();

            isNew = RsetTools.getInt(rs, "IS_NEW", new Integer(-1)).intValue();

        }

        catch (Exception e)

        {

            cat.error("Error get member module", e);

            throw e;

        }

    }

/*

    public byte[] getPlainHTML()

    {

        return null;

    }



    public byte[] getXml(String rootElement) throws Exception

    {

        return "".getBytes();

    }

    public byte[] getXml(){ return "".getBytes(); }



    public Vector getList(Long idSiteCtxLangCatalog, Long idContext)

    {

        return null;

    }

*/

}