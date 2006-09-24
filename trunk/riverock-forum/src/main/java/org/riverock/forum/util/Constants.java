/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.forum.util;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:12:48
 *         $Id$
 */
public class Constants {

    public static final String OK_EXECUTE_STATUS = "ok";
    public static final String ERROR_EXECUTE_STATUS = "error";

    public static final String WM_FORUM_PORTLET_NAME = "riverock.forum";
    public static final String WM_FORUM_LIST_MANAGER_PORTLET_NAME = "riverock.forum-list-manager";

    public static final String ACTION_CONFIG_PARAM_NAME = "wm-forum-action-config";
    public static final String MESSAGES_PER_PAGE_INIT_PARAM = "messages-per-page";
    public static final String TOPICS_PER_PAGE_INIT_PARAM = "topics-per-page";
    public static final String FLOOD_TIME_INIT_PARAM = "flood-time";
    // portlet metadata
    public static final String LOGIN_URL_METADATA = "login-url";
    public static final String LOGOUT_URL_METADATA = "logout-url";
    public static final String REGISTER_URL_METADATA = "register-url";
    public static final String MEMBERS_URL_METADATA = "members-url";
    public static final String ACTION_NAME_PARAM = "action";
    public static final String GENERIC_BEAN = "genericBean";

    public static final String PRINCIPAL_BEAN = "principal";
    // name of request parameters
    public static final String NAME_FORUM_ID = "forum_id";
    public static final String NAME_FORUM_CONCRETE_ID = "f_id";
    public static final String NAME_FORUM_TOPIC_ID = "t_id";
    public static final String NAME_FORUM_MESSAGE_ID = "m_id";
    public static final String NAME_FORUM_REPLY = "reply";
    public static final String NAME_SUB_ACTION = "sub-action";
    public static final String FORWARD_PAGE_ACTION = "forward-page";
    public static final String PAGE_PROCESS_TIME_ATTRIBUTE = "process-time";
    public static final String ACTION_MESSAGE = "actionMessage";
    public static final String LAST_POST_TIME = "lastPostTime";
    public static final String FORUM_ACTION = "forum";
    public static final String TOPIC_ACTION = "topic";
    public static final String USER_ACTION = "user";
    public static final String USERLIST_ACTION = "userList";
    public static final String LOGIN = "login";
    public static final String REGISTER_ACTION = "register";
    public static final String POST_ACTION = "post";
    public static final String POSTP_ACTION = "postp";
    public static final String USEREDIT_ACTION = "userEdit";
    public static final String USEREDITP_ACTION = "userEditP";
    public static final String LOGOUT_ACTION = "logout";
    public static final String HELP_ACTION = "help";
    public static final String ADMIN_FORUM_ACTION = "admin-forum";
    public static final String REQUEST_LOCALE_VALUE = "request-locale";
}
