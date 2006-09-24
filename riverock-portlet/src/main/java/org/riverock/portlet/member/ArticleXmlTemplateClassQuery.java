/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
