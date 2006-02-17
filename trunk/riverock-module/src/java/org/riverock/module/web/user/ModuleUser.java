package org.riverock.module.web.user;

import org.riverock.module.exception.ModuleException;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:50:32
 *         $Id$
 */
public interface ModuleUser {
    public String getName();

    public String getAddress();

    public Long getId();

    public String getUserLogin();

    public boolean isCompany() throws ModuleException ;

//    public boolean isGroupCompany() throws ModuleException ;

    public boolean isHolding() throws ModuleException ;

}
