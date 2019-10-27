package org.riverock.webmill.utils;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 15:03:24
 */
public class JaxbValidationEventHandler implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent validationEvent) {
        return false;
    }
}
