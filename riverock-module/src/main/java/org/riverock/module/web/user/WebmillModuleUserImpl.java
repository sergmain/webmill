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

import java.security.Principal;

import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:56:56
 *         $Id$
 */
public class WebmillModuleUserImpl implements ModuleUser {

    private AuthSession authSession = null;
    public WebmillModuleUserImpl(Principal userPrincipal) {
        this.authSession = (AuthSession) userPrincipal;
    }

    public String getName() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getName();
        }
    }

    public String getAddress() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserInfo().getAddress();
        }
    }

    public Long getId() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserInfo().getUserId();
        }
    }

    public String getUserLogin() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserLogin();
        }
    }

    public boolean isCompany() {
        return authSession.getAuthInfo().isCompany();
    }

//    public boolean isGroupCompany() {
//        return authSession.getAuthInfo().isGroupCompany();
//    }

    public boolean isHolding() {
        return authSession.getAuthInfo().isHolding();
    }
}
