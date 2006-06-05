/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.dao.PortalHoldingDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalHoldingDaoImpl implements PortalHoldingDao {
    private AuthSession authSession = null;

    PortalHoldingDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Holding loadHolding(Long id) {
        return InternalDaoFactory.getInternalHoldingDao().loadHolding( id, authSession );
    }

    public List<Holding> getHoldingList() {
        return InternalDaoFactory.getInternalHoldingDao().getHoldingList( authSession );
    }

    public Long processAddHolding(Holding holdingBean ) {
        return InternalDaoFactory.getInternalHoldingDao().processAddHolding( holdingBean, authSession );
    }

    public void processSaveHolding(Holding holdingBean) {
        InternalDaoFactory.getInternalHoldingDao().processSaveHolding( holdingBean, authSession );
    }

    public void processDeleteHolding(Holding holdingBean) {
        InternalDaoFactory.getInternalHoldingDao().processDeleteHolding( holdingBean, authSession );
    }
}
