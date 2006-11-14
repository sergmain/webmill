/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.manager.holding;

import java.io.Serializable;

/**
 * @author SergeMaslyukov
 *         Date: 06.01.2006
 *         Time: 11:26:35
 *         $Id$
 */
public class HoldingSessionBean implements Serializable {

    private HoldingBean holdingBean = null;
    private Long currentCompanyId = null;
    private Long currentHoldingId = null;

    private boolean isAdd = false;
    private boolean isEdit = false;
    private boolean isDelete = false;

    public HoldingSessionBean() {
    }

    public void resetStatus() {
        isAdd = false;
        isEdit = false;
        isDelete = false;
    }

    public void setAdd(boolean isAdd) {
        this.isAdd = isAdd;
        if (isAdd) {
            isEdit = false;
            isDelete = false;
        }
    }

    public boolean getAdd() {
        return isAdd;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            isAdd = false;
            isDelete = false;
        }
    }

    public boolean getEdit() {
        return isEdit;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
        if (isDelete) {
            isEdit = false;
            isAdd = false;
        }
    }

    public boolean getDelete() {
        return isDelete;
    }

    public void setHoldingBean(HoldingBean bean) {
        this.holdingBean = bean;
    }

    public HoldingBean getHoldingBean() {
        return holdingBean;
    }

    public Long getCurrentHoldingId() {
        return currentHoldingId;
    }

    public void setCurrentHoldingId( Long id ) {
        this.currentHoldingId = id;
    }

    public Long getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId( Long id ) {
        this.currentCompanyId = id;
    }
}
