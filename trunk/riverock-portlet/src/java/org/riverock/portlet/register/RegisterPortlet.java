/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.register;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;

import org.riverock.webmill.portlet.GenericWebmillPortlet;
import org.riverock.webmill.portlet.PortletResultObject;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 22.02.2005
 * Time: 18:21:10
 * $Id$
 */
public final class RegisterPortlet extends GenericWebmillPortlet {

    private final static Logger log = Logger.getLogger( RegisterPortlet.class );

    public static final String NAME_REGISTER_ACTION_PARAM = "mill.register.action";
    public static final String REGISTER_PROCESS_PORTLET = "mill.register_process";
    public static final String REGISTER_PORTLET = "mill.register";
    public static final String DEFAULT_ROLE_METADATA = "register-default-role";
    static final String FIRST_NAME_PARAM = "first_name";
    static final String LAST_NAME_PARAM = "last_name";
    static final String MIDDLE_NAME_PARAM = "middle_name";
    static final String USERNAME_PARAM = "username";
    static final String PASSWORD1_PARAM = "password1";
    static final String PASSWORD2_PARAM = "password2";
    static final String EMAIL_PARAM = "email";
    static final String ADDRESS_PARAM = "addr";
    static final String PHONE_PARAM = "phone";
    static final String ERROR_TEXT = "REGISTER.ERROR_TEXT";
    static final String ERROR_URL_NAME = "REGISTER.ERROR_URL_NAME";
    static final String ERROR_URL = "REGISTER.ERROR_URL";

    public RegisterPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        RegisterUser registerUser = new RegisterUser();
        doRender( renderRequest, renderResponse, registerUser );
    }
}
