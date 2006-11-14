/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.              
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.interfaces.portal.bean;

import java.util.Date;
import java.io.Serializable;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 15:22:19
 */
public interface User extends Serializable {
    public Long getUserId();
    public Long getCompanyId();
    public String getFirstName();
    public String getMiddleName();
    public String getLastName();
    public Date getCreatedDate();
    public Date getDeletedDate();
    public String getAddress();
    public String getPhone();
    public String getEmail();
    public boolean isDeleted();
}
