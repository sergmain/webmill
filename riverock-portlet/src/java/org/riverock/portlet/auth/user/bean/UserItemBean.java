package org.riverock.portlet.auth.user.bean;

import java.io.Serializable;

/**
 * @author SMaslyukov
 *         Date: 22.05.2005
 *         Time: 16:35:58
 *         $Id$
 */
public class UserItemBean implements Serializable {
    private static final long serialVersionUID = 2043005509L;

    private Long userId = null;
    private String userName = null;

    public Long getUserId() {
        return userId;
    }

    public void setUserId( Long userId ) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }
}