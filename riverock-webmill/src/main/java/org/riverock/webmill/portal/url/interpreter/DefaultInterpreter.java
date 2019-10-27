package org.riverock.webmill.portal.url.interpreter;

import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 14:23:43
 */
public class DefaultInterpreter implements UrlInterpreter{

    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {
        return new UrlInterpreterResult();
    }
}
