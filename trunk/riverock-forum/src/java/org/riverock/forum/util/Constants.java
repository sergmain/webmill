/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
