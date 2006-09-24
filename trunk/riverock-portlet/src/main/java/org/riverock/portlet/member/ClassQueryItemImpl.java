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

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: Admin
 * Date: Nov 24, 2002
 * Time: 3:16:42 PM
 *
 * $Id$
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
