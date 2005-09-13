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

package org.riverock.portlet.member;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;



/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class MenuMemberModule
{
    private final static Log log = LogFactory.getLog( MenuMemberModule.class );

    /**
     * module - название модуля
     */
    public String moduleName = "";

    public String moduleCode = null;
    public String applicationCode = null;

    /**
     * order - значение для порядка вывода модуля
     */
    public int order = 0;
    public int isNew = -1;
    public int modRecordNumber = 0;
    public int applRecordNumber = 0;

    protected void finalize() throws Throwable
    {
        moduleName = null;

        applicationCode = null;
        moduleCode = null;

        super.finalize();
    }

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
            order = RsetTools.getInt( rs, "ORDER_FIELD", 0 );
            isNew = RsetTools.getInt( rs, "IS_NEW", -1 );
        }
        catch (Exception e)
        {
            log.error("Error get member module", e);
            throw e;
        }
    }
}