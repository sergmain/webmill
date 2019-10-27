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
package org.riverock.interfaces.common;

import java.util.List;
import java.io.Serializable;

/**
 * User: serg_main
 * Date: 07.10.2004
 * Time: 14:45:47
 * @author Serge Maslyukov
 * $Id: TreeItem.java 1043 2006-11-14 14:29:22Z serg_main $
 */
public interface TreeItem extends Serializable {
    public Long getTopId();
    public Long getId();
    public List<TreeItem> getSubTree();
    public void setSubTree(List<TreeItem> list);
}