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

 * Time: 3:58:34 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import java.util.ArrayList;

import java.util.List;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.webmill.core.GetSiteTemplateItem;

import org.riverock.webmill.core.GetSiteTemplateWithIdSiteSupportLanguageList;

import org.riverock.webmill.schema.core.SiteTemplateItemType;

import org.riverock.webmill.schema.core.SiteTemplateListType;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.PortletTools;



import org.apache.log4j.Logger;



public class TemplateMemberClassQuery extends BaseClassQuery

{

    private static Logger log = Logger.getLogger( "org.riverock.member.TemplateMemberClassQuery" );



    private Long idSiteTemplate = null;



    public final static String nameModule = "site.mem.lang";

    public final static String nameField = "ID_SITE_SUPPORT_LANGUAGE";



    MemberQueryParameter param = null;



    public void setIdSiteTemplate(Long param)

    {

        idSiteTemplate = param;



        if (log.isDebugEnabled())

            log.debug("idSiteTemplate - "+idSiteTemplate.longValue());

    }



    /**

     * ¬озвращает текущее значение дл€ отображени€ на веб-странице

     * @return String

     */

    public String getCurrentValue(CtxInstance ctxInstance)

        throws Exception

    {

//        SiteTemplateList templateList = param.webPage.p.templates;

//        SiteTemplate template = (SiteTemplate)templateList.hashId.get(idSiteTemplate);



        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            SiteTemplateItemType templateItem =

                GetSiteTemplateItem.getInstance(db_, idSiteTemplate).item;



            if (templateItem!=null)

                return templateItem.getNameSiteTemplate();



            return "";

        }

        finally

        {

            DatabaseAdapter.close( db_ );

            db_ = null;

        }

    }



    /**

     *  ¬озвращает список возможных значений дл€ построени€ <select> элемента

     * @return Vector of org.riverock.member.ClassQueryItem

     */

    public List getSelectList(CtxInstance ctxInstance)

        throws Exception

    {

//        SiteTemplateList templateList = param.webPage.p.templates;

        DatabaseAdapter db_ = null;

        try

        {

            db_ = DatabaseAdapter.getInstance(false);

            List v = new ArrayList();



//        SiteTemplateDescListType list = templateList.templateList;

            Long id = PortletTools.getLong(ctxInstance.getPortletRequest(), nameModule+'.'+nameField);

            SiteTemplateListType templateList = GetSiteTemplateWithIdSiteSupportLanguageList.getInstance(db_, id).item;



            if (log.isDebugEnabled())

            {

                log.debug("parameter "+nameModule+'.'+nameField+" is "+ctxInstance.getPortletRequest().getParameter(nameModule+'.'+nameField));

                log.debug("id "+id);

            }

            for (int i=0; i<templateList.getSiteTemplateCount(); i++)

            {

                SiteTemplateItemType templateItem = templateList.getSiteTemplate(i);

                ClassQueryItem item = new ClassQueryItem(

                    templateItem.getIdSiteTemplate(),

                    templateItem.getNameSiteTemplate()

                );

                if (item.index.equals(idSiteTemplate))

                    item.isSelected = true;

                v.add( item );

            }

/*

        for (int i=0; i<list.getTemplateDescriptionCount(); i++)

        {

            SiteTemplateDescriptionType desc = list.getTemplateDescription( i );



            if (log.isDebugEnabled())

                log.debug("idTemplate "+desc.getIdTemplateLanguage()+" is "+ctxInstance.getPortletRequest().getParameter(nameModule+'.'+nameField));



            if (desc.getIdTemplateLanguage()==id)

            {

                ClassQueryItem item = new ClassQueryItem(

                    desc.getIdTemplate(),

                    desc.getTemplate().getNameTemplate()

                );

                if (item.index==idSiteTemplate.longValue())

                    item.isSelected = true;

                v.add( item );

            }

        }

*/

            return v;

        }

        finally

        {

            DatabaseAdapter.close(db_);

            db_ = null;

        }

    }



    public void setQueryParameter(MemberQueryParameter parameter) throws Exception

    {

        this.param = parameter;

    }

}

