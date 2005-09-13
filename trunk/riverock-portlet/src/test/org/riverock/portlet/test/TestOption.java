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

package org.riverock.portlet.test;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private final static Log log = LogFactory.getLog( TestOption.class );

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
