/*
 * org.riverock.module - Abstract layer for web module
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
package org.riverock.module.web.user;

import org.riverock.module.exception.ModuleException;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:50:32
 *         $Id$
 */
public interface ModuleUser {
    public String getName();

    public String getAddress();

    public Long getId();

    public String getUserLogin();

    public boolean isCompany() throws ModuleException ;

//    public boolean isGroupCompany() throws ModuleException ;

    public boolean isHolding() throws ModuleException ;

}
