/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 *         $Id: ForumError.java 1119 2006-12-02 22:35:13Z serg_main $
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
