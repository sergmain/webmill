package org.riverock.commerce.bean;

import org.riverock.interfaces.portlet.member.ClassQueryItem;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2006
 * Time: 20:25:24
 * <p/>
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
