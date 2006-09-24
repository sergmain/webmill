/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.jsf;

import java.util.StringTokenizer;

import org.apache.myfaces.shared_tomahawk.util.el.TestsMap;

/**
 * @author Sergei Maslyukov
 *         Date: 30.08.2006
 *         Time: 17:39:48
 *         <p/>
 *         $Id$
 */
public class UserInRoleChecker  extends TestsMap {

    public boolean getTest(String role) {
        if (role==null) {
            return false;
        }
        StringTokenizer st =  new StringTokenizer(role, ", ",false);
        while (st.hasMoreTokens()) {
            if (FacesTools.isUserInRole(st.nextToken()) ) {
                return true;
            }
        }
        return false;
    }
}