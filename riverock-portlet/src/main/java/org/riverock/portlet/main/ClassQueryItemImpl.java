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
package org.riverock.portlet.main;

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:16:42 PM
 *
 * $Id: ClassQueryItemImpl.java 1068 2006-11-23 18:26:08Z serg_main $
 */
public class ClassQueryItemImpl implements ClassQueryItem {
    private boolean isSelected = false;
    private Long index = null;
    private String value = "";

    public ClassQueryItemImpl(Long idx, String val)
    {
        setIndex(idx);
        setValue(val);
    }
    public ClassQueryItemImpl(long idx, String val)
    {
        setIndex(new Long(idx));
        setValue(val);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
