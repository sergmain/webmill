package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameBeanImpl implements Serializable, PortletNameBean {
    private static final long serialVersionUID = 2057005507L;

    private Long id = null;
    private String name = null;

    public PortletNameBeanImpl() {
    }

    public PortletNameBeanImpl(PortletNameBean bean) {
        this.id = bean.getId();
        this.name = bean.getName();
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public boolean equals( PortletNameBean portletNameBean ) {
        if( portletNameBean == null || portletNameBean.getId()==null || id==null ) {
            return false;
        }
        return portletNameBean.getId().equals( id );
    }

    public String toString() {
        return "[name:" + name + ",id:" + id + "]";
    }
}
