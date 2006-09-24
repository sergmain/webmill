/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.interfaces.common;

import java.util.List;
import java.io.Serializable;

/**
 * User: serg_main
 * Date: 07.10.2004
 * Time: 14:45:47
 * @author Serge Maslyukov
 * $Id$
 */
public interface TreeItem extends Serializable {
    public Long getTopId();
    public Long getId();
    public List<TreeItem> getSubTree();
    public void setSubTree(List<TreeItem> list);
}