package org.riverock.update.webmill.v581.convert_template;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2007
 * Time: 23:22:22
 */
public class JaxbValidationEventHandler  implements ValidationEventHandler {

    public boolean handleEvent(ValidationEvent validationEvent) {
        return false;
    }
}
