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
package org.riverock.forum;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import org.riverock.module.action.ActionMessage;
import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.forum.util.Constants;

/**
 * @author SMaslyukov
 *         Date: 19.04.2005
 *         Time: 17:59:04
 *         $Id$
 */
public final class ForumError {
    private final static Logger log = Logger.getLogger(ForumError.class);
    public static final String SYSTEM_ERROR = "system_error";
    public static final String USER_LIST_ERROR = "user_list_error";
    public static final String USER_EDIT_ERROR = "user_edit_error";
    public static final String NO_SUCH_USER_ERROR = "no_such_user";
    public static final String POST_ERROR = "post_error";
    public static final String EDIT_MESSAGE_ERROR = "edit_message_error";
    public static final String BLANK_ERROR = "blank_error";
    public static final String TOPIC_LOCKED = "topic_locked";
    public static final String FLOOD_ERROR = "flood_error";
    public static final String NOT_LOGGED = "not_logged";
    public static final String NO_SUCH_TOPIC = "no_such_topic";
    public static final String NO_SUCH_MESSAGE = "no_such_message";
    public static final String HOME_NO_FORUMS = "home_no_forums";
    public static final String NO_SUCH_FORUM = "no_such_forum";

    public static String noSuchForumError(ModuleActionRequest actionBean) {
        return error(actionBean, NO_SUCH_FORUM);
    }

    public static String homeNoForumsError(ModuleActionRequest actionBean) {
        return error(actionBean, HOME_NO_FORUMS);
    }

    public static String noSuchTopicError(ModuleActionRequest actionBean) {
        return error(actionBean, NO_SUCH_TOPIC);
    }

    public static String noSuchMessageError(ModuleActionRequest actionBean) {
        return error(actionBean, NO_SUCH_MESSAGE);
    }

    public static String notLoggedError(ModuleActionRequest actionBean) {
        return error(actionBean, NOT_LOGGED);
    }

    public static String floodError(ModuleActionRequest actionBean) {
        return error(actionBean, FLOOD_ERROR);
    }

    public static String topicLockedError(ModuleActionRequest actionBean) {
        return error(actionBean, TOPIC_LOCKED);
    }

    public static String postError(ModuleActionRequest actionBean) {
        return error(actionBean, POST_ERROR);
    }

    public static String editMessageError(ModuleActionRequest actionBean) {
        return error(actionBean, EDIT_MESSAGE_ERROR);
    }

    public static String blankError(ModuleActionRequest actionBean) {
        return error(actionBean, BLANK_ERROR);
    }

    public static String noSuchUserError(ModuleActionRequest actionBean) {
        return error(actionBean, NO_SUCH_USER_ERROR);
    }

    public static String userEditError(ModuleActionRequest actionBean) {
        return error(actionBean, USER_EDIT_ERROR);
    }

    public static String userListError(ModuleActionRequest actionBean) {
        return error(actionBean, USER_LIST_ERROR);
    }

    public static String systemError(ModuleActionRequest actionBean) {
        return error(actionBean, SYSTEM_ERROR);
    }

    public static String systemError(ModuleRequest request, ResourceBundle resourceBundle) {
        return error(request, resourceBundle, SYSTEM_ERROR);
    }

    public static String error(ModuleActionRequest actionBean, String errorCode) {
        return error(actionBean.getRequest(), actionBean.getResourceBundle(), errorCode);
    }

    public static String error(ModuleRequest request, ResourceBundle resourceBundle, String errorCode) {
        // hack to output where exception is created
        try {
            getException();
        }
        catch (Exception e) {
            log.error("error", e);
        }
        request.setAttribute(
            Constants.ACTION_MESSAGE,
            new ActionMessage(resourceBundle, errorCode)
        );
        return Constants.ERROR_EXECUTE_STATUS;
    }

    private static void getException() throws Exception {
        throw new Exception();
    }
}
