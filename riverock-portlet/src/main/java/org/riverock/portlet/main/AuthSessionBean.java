/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.main;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;

public class AuthSessionBean implements Serializable {
    private static final long serialVersionUID = 2055005503L;

    private AuthSession authSession = null;

    public AuthSessionBean() {
        this.authSession = ( AuthSession ) FacesTools.getUserPrincipal();
    }

    public AuthSession getAuthSession() {
        return authSession;
    }

    public String getName() {
        return authSession!=null?authSession.getName():null;
    }

    public String getAddress() {
        return authSession!=null?authSession.getUserInfo().getAddress():null;
    }

    public Long getId() {
        return authSession!=null?authSession.getUserInfo().getUserId():null;
    }

    public String getUserLogin() {
        return authSession!=null?authSession.getUserLogin():null;
    }

    public boolean isCompany() {
        return authSession != null && authSession.getAuthInfo().isCompany();
    }

    public boolean isHolding() {
        return authSession != null && authSession.getAuthInfo().isHolding();
    }

    public Long getHoldingId() {
        return authSession!=null?authSession.getAuthInfo().getHoldingId():null;
    }

}
