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
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.webmill.site;

import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.SiteTemplate;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.apache.log4j.Logger;

public class SiteTemplateService
{
    private static Logger log = Logger.getLogger( SiteTemplateService.class );

//    public SiteTemplate siteTemplate = new SiteTemplate();
//    public String nameTemplate = null;
//    public long idSiteTemplate = -1;

//    protected void finalize() throws Throwable
//    {
//        nameTemplate = null;
//
//        super.finalize();
//    }

    public static boolean isDynamic(SiteTemplate siteTemplate)
    {
        if (siteTemplate == null)
            return false;

        for (int i = 0; i < siteTemplate.getSiteTemplateItemCount(); i++)
        {
            TemplateItemType item = siteTemplate.getSiteTemplateItem(i);

            if (item.getType().getType() == TemplateItemTypeTypeType.DYNAMIC_TYPE)
                return true;
        }
        return false;
    }

    public static boolean isFullXml(SiteTemplate siteTemplate)
    {
        if (siteTemplate == null)
            return true;

        for (int i = 0; i < siteTemplate.getSiteTemplateItemCount(); i++)
        {
            TemplateItemType item = siteTemplate.getSiteTemplateItem(i);

            if (item.getType().getType() == TemplateItemTypeTypeType.FILE_TYPE)
                return false;
        }
        return true;
    }

//    public SiteTemplateService()
//    {
//    }

}