package org.riverock.portlet.manager.users;

import java.util.Date;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:47:30
 *         $Id$
 */
public interface PortalUserBean {
    public String getName();
    public Long getId();
    public Long getCompanyId();
    public String getFirstName();
    public String getMiddleName();
    public String getLastName();
    public Date getCreatedDate();
    public Date getClosedDate();
    public String getAddress();
    public String getPhone();
    public String getEmail();
}
