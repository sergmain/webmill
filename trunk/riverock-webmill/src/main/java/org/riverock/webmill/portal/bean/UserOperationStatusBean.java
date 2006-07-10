package org.riverock.webmill.portal.bean;

import org.riverock.interfaces.portal.bean.UserOperationStatus;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:43:44
 */
public class UserOperationStatusBean implements UserOperationStatus {
    private boolean isError;
    private String internalErrorMessage=null;
    private int operationCode;

    public UserOperationStatusBean() {
    }

    public UserOperationStatusBean(int operationCode) {
        this.operationCode=operationCode;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getInternalErrorMessage() {
        return internalErrorMessage;
    }

    public void setInternalErrorMessage(String internalErrorMessage) {
        this.internalErrorMessage = internalErrorMessage;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }
}
