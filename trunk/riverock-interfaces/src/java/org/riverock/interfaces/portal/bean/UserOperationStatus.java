package org.riverock.interfaces.portal.bean;

/**
 * @author Sergei Maslyukov
 *         Date: 22.06.2006
 *         Time: 20:15:27
 */
public interface UserOperationStatus {
    public boolean isError();
    public String getInternalErrorMessage();
    public int getOperationCode();
}
