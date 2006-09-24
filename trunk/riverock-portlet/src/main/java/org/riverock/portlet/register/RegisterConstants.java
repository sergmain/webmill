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
package org.riverock.portlet.register;

/**
 * @author SergeMaslyukov
 *         Date: 30.11.2005
 *         Time: 20:28:28
 *         $Id$
 */
public class RegisterConstants {

    public static final String REGISTER_BEAN = "registerBean";


    public static final String REQUEST_LOCALE_VALUE = "request-locale";

    public static final String NOT_LOGGED = "not_logged";
    public static final String EMAIL_IS_EMPTY = "email_is_empty";

    public static final String OK_EXECUTE_STATUS = "ok";
    public static final String ERROR_EXECUTE_STATUS = "error";

    // resourcebundle key
    public static final String NAME_EMAIL = "email";
    public static final String NO_SUCH_EMAIL = "no_such_email";
    public static final String ROLE_IS_NULL = "role_is_null";
    public static final String LOGIN_ALREADY_REGISTERED = "reg.error-login-already-registered";
    public static final String USER_LOGIN_IS_NULL = "reg.error-user-login-is-null";
    public static final String USER_PASSWORD_IS_NULL = "reg.error-user-password-is-null";
    public static final String USER_PASSWORD_IS_MISMATCH = "reg.error-user-password-is-mismatch";
    public static final String USER_EMAIL_IS_NULL = "reg.error-user-email-is-null";
    public static final String CAPTCHA_WRONG = "reg.error-captcha-wrong";



    public static final String SYSTEM_ERROR = "system_error";

    public static final String CAPTCHA_ID = "captchaId";


    public static final String FIRST_NAME_PARAM = "first_name";
    public static final String LAST_NAME_PARAM = "last_name";
    public static final String MIDDLE_NAME_PARAM = "middle_name";
    public static final String USERNAME_PARAM = "username";
    public static final String PASSWORD1_PARAM = "password1";
    public static final String PASSWORD2_PARAM = "password2";
    public static final String EMAIL_PARAM = "email";
    public static final String ADDRESS_PARAM = "addr";
    public static final String PHONE_PARAM = "phone";


    public final static int UNKNOWN_STATUS = -1;
    public final static int OK_STATUS = 0;
    public final static int USERNAME_IS_NULL_STATUS = 1;
    public final static int PASSWORD_NOT_EQUALS_STATUS = 2;
    public final static int PASSWORD1_IS_NULL_STATUS = 3;
    public final static int PASSWORD2_IS_NULL_STATUS = 4;
    public final static int EMAIL_IS_NULL_STATUS = 5;
    public final static int EMAIL_IS_WRONG_STATUS = 6;
    public final static int ROLE_IS_NULL_STATUS = 7;
    public final static int USERNAME_ALREADY_EXISTS_STATUS = 8;

    public static final String REGISTER_PORTLET = "mill.register";
    public static final String DEFAULT_ROLE_METADATA = "register-default-role";
    public static final String ERROR_TEXT = "REGISTER.ERROR_TEXT";
    public static final String ERROR_URL_NAME = "REGISTER.ERROR_URL_NAME";
    public static final String ERROR_URL = "REGISTER.ERROR_URL";
}
