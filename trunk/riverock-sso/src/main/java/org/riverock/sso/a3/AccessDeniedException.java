/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
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
package org.riverock.sso.a3;

/**
 * Exception for authenticate of user
 */
/**
 * Author: mill
 * Date: Mar 12, 2003
 * Time: 3:26:06 PM
 *
 * $Id$
 */
public class AccessDeniedException extends Exception
{
    public AccessDeniedException(String description)
    {
        super(description);
    }

    public AccessDeniedException()
    {
        super();
    }
}
