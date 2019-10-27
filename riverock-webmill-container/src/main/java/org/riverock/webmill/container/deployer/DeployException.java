package org.riverock.webmill.container.deployer;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:26:52
 */
public class DeployException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 50434672384237154L;

    public DeployException(){
        super();
    }

    public DeployException(String s){
        super(s);
    }

    public DeployException(String s, Throwable cause){
        super(s, cause);
    }
}
