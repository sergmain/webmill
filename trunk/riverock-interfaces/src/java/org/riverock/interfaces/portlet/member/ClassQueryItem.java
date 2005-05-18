package org.riverock.interfaces.portlet.member;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 14:02:11
 *         $Id$
 */
public interface ClassQueryItem {
    public boolean isSelected();
    public void setSelected(boolean selected);
    public Long getIndex();
    public void setIndex(Long index);
    public String getValue();
    public void setValue(String value);
}
