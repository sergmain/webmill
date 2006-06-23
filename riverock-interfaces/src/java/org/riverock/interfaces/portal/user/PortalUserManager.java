package org.riverock.interfaces.portal.user;

import java.util.Map;

import org.riverock.interfaces.portal.bean.UserRegistration;
import org.riverock.interfaces.portal.bean.UserOperationStatus;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:09:12
 */
public interface PortalUserManager {
    public static final int OK_OPERATION = 0;
    public static final int NO_SUCH_EMAIL = 1;
    public static final int USER_DELETED = 2;
    public static final int NOT_REGISTERED = 3;
    public static final int LOGIN_ALREADY_REGISTERED = 4;
    public static final int EMAIL_ALREADY_REGISTERED = 5;

    public static final String SEND_PASSWORD_SUBJECT_MESSAGE = "SEND_PASSWORD_SUBJECT_MESSAGE";
    public static final String SEND_PASSWORD_BODY_ONE_PASSWORD_MESSAGE = "SEND_PASSWORD_BODY_ONE_PASSWORD_MESSAGE";
    public static final String SEND_PASSWORD_START_BODY_MANY_PASSWORDS_MESSAGE = "SEND_PASSWORD_START_BODY_MANY_PASSWORDS_MESSAGE";
    public static final String SEND_PASSWORD_END_BODY_MANY_PASSWORDS_MESSAGE = "SEND_PASSWORD_END_BODY_MANY_PASSWORDS_MESSAGE";

    public static final String CREATE_ACCOUNT_BODY_MESSAGE = "CREATE_ACCOUNT_BODY_MESSAGE";
    public static final String CREATE_ACCOUNT_SUBJECT_MESSAGE = "CREATE_ACCOUNT_SUBJECT_MESSAGE";

    public UserOperationStatus sendPassword(String eMail, Map<String, String> messages);
    public UserOperationStatus registerNewUser(UserRegistration userRegistration, Map<String, String> messages);
}
