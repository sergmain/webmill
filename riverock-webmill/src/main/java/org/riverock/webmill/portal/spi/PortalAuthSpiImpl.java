/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.portal.spi;

import org.riverock.interfaces.portal.spi.PortalAuthSpi;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 2:07:09
 *         $Id: PortalAuthSpiImpl.java 1396 2007-09-02 20:31:22Z serg_main $
 */
public class PortalAuthSpiImpl implements PortalAuthSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalAuthSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

}
