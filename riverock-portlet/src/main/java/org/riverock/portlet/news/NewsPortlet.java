/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.news;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.interfaces.portlet.PortletResultObject;
import org.riverock.common.portlet.GenericWebmillPortlet;

/**
 * User: SergeMaslyukov
 * Date: 18.12.2004
 * Time: 18:21:10
 * $Id: NewsPortlet.java 1230 2007-07-02 15:27:45Z serg_main $
 */
public final class NewsPortlet extends GenericWebmillPortlet {

    public NewsPortlet(){
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        
        String newsType = renderRequest.getParameter( NewsSite.NEWS_TYPE );
        PortletResultObject resultObject ;
        if (newsType==null || !NewsSite.NEWS_TYPE_ITEM.equals( newsType ) )
            resultObject = new NewsSite();
        else
            resultObject = new NewsItemSimple();

        doRender( renderRequest, renderResponse, resultObject );
    }
}
