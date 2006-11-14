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
package org.riverock.portlet.member;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * Author: mill
 * Date: Jan 10, 2003
 * Time: 2:20:21 PM
 *
 * $Id$
 */
public class ArticleXmlTemplateClassQuery extends BaseClassQuery {
//    private static Logger cat = Logger.getLogger(ArticleXmlTemplateClassQuery.class);

    public ArticleXmlTemplateClassQuery() {
    }

    /**
     * ¬озвращает текущее значение дл€ отображени€ на веб-странице
     *
     * @return String
     */
    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle ) throws Exception {
        return bundle.getString( "yesno.no" );
    }

    /**
     * ¬озвращает список возможных значений дл€ построени€ <select> элемента
     *
     * @return Vector of org.riverock.member.ClassQueryItem
     */
    public List<ClassQueryItem> getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
        throws Exception {
        List<ClassQueryItem> v = new ArrayList<ClassQueryItem>();
        ClassQueryItem item = new ClassQueryItemImpl( 0l, bundle.getString( "yesno.no" ) );

        item.setSelected( true );

        v.add( item );
        return v;
    }

    MemberQueryParameter param = null;

    public void setQueryParameter( MemberQueryParameter parameter ) throws Exception {
        this.param = parameter;
    }
}
