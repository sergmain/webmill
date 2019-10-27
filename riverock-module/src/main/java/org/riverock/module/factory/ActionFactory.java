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
package org.riverock.module.factory;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.exception.ActionException;
import org.riverock.module.factory.bean.ActionConfigurationBean;

/**
 * @author Serge Maslyukov
 *         Date: 24.04.2005
 *         Time: 20:53:55
 *         $Id: ActionFactory.java 1445 2007-09-28 15:57:11Z serg_main $
 */
public interface ActionFactory {
    public ActionConfigurationBean getAction(String actionName);
    public ActionConfigurationBean getDefaultAction();
    public void destroy();
    public String doAction(ModuleActionRequest moduleActionRequest) throws ActionException;
    public String getForwardPath(String actionName, String forwardName);
}
