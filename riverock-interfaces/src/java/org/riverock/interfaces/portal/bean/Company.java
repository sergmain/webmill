package org.riverock.interfaces.portal.bean;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:12:01
 *         $Id$
 */
public interface Company {
    public String getName();
    public Long getId();
    public String getShortName();
    public String getAddress();
    public String getCeo();
    public String getCfo();
    public String getWebsite();
    public String getInfo();
    public boolean isDeleted();
}
