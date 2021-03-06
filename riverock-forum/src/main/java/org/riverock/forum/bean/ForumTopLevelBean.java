/*
 * org.riverock.forum - Forum portlet
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
package org.riverock.forum.bean;

import java.util.List;
import java.util.ArrayList;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 18:32:41
 *         $Id: ForumTopLevelBean.java 1119 2006-12-02 22:35:13Z serg_main $
 */
public class ForumTopLevelBean {
    private List forums = new ArrayList();

    public List getForums() {
        return forums;
    }

    public void setForums(List forums) {
        this.forums = forums;
    }
}
