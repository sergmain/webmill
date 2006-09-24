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

package org.riverock.portlet.test;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.PortletRequest;



import org.apache.log4j.Logger;

import org.riverock.portlet.member.BaseClassQuery;
import org.riverock.portlet.member.ClassQueryItemImpl;
import org.riverock.portlet.member.MemberQueryParameter;
import org.riverock.portlet.member.ClassQueryItemImpl;



/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 1:11:16 AM
 *
 * $Id$
 */
public final class TestOption extends BaseClassQuery
{
    private final static Logger log = Logger.getLogger( TestOption.class );

    public String type = null;
    public Long idLang = null;
    public Long idTest = null;

    public TestOption(){}

    public void setType(String param)
    {

        if (log.isDebugEnabled())
            log.debug("type - "+param);

        type = param;

    }

    public void setIdLang(java.lang.Long param)
    {
        idLang = param;

        if (log.isDebugEnabled())
            log.debug("type - "+param.longValue());

    }

    public void setIdTest(java.lang.Long param)
    {
        idTest = param;

        if (log.isDebugEnabled())
            log.debug("type - "+param.longValue());

    }

    public String getCurrentValue( PortletRequest renderRequest, ResourceBundle bundle )
    {
        return type;
    }

    public List getSelectList( PortletRequest renderRequest, ResourceBundle bundle )
    {
        List v = new ArrayList();
        v.add( new ClassQueryItemImpl(1, "aaa") );
        v.add( new ClassQueryItemImpl(2, "bbb") );
        v.add( new ClassQueryItemImpl(3, "ccc") );
        v.add( new ClassQueryItemImpl(4, "ddd") );
        return v;
    }

    MemberQueryParameter param = null;

    public void setQueryParameter(MemberQueryParameter parameter) throws Exception
    {
        this.param = parameter;
    }
}
