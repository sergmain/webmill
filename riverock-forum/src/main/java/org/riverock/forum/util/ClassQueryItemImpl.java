/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.forum.util;

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 13:57:10
 *         $Id$
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
        setIndex(idx);
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
