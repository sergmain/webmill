/*
 * org.riverock.module - Abstract layer for web module
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.module.factory.bean;

import org.riverock.module.action.ActionInstance;
import org.riverock.module.config.schema.Action;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 14:23:50
 *         $Id: ActionConfigurationBean.java 1439 2007-09-27 14:23:59Z serg_main $
 */
public class ActionConfigurationBean {
    private Action actionBean = null;
    private ActionInstance actionInstance = null;

    public Action getActionBean() {
        return actionBean;
    }

    public void setActionBean(Action actionBean) {
        this.actionBean = actionBean;
    }

    public ActionInstance getAction() {
        return actionInstance;
    }

    public void setAction(ActionInstance actionInstance) {
        this.actionInstance = actionInstance;
    }
}
