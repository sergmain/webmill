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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id: InternalHoldingDao.java 1056 2006-11-15 19:02:57Z serg_main $
 */
public interface InternalHoldingDao extends Serializable {
    
    public Holding loadHolding( Long id, AuthSession authSession );
    public Long processAddHolding( Holding holdingBean, AuthSession authSession );
    public void processSaveHolding( Holding holdingBean, AuthSession authSession );
    public void processDeleteHolding( Holding holdingBean, AuthSession authSession );

    public List<Holding> getHoldingList( AuthSession authSession );

    public void setRelateHoldingCompany(Long holdingId, Long companyId );
}
