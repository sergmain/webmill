package org.riverock.webmill.template.parser;

import org.riverock.webmill.template.parser.ParsedTemplateElement;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 18:54:05
 */
public class ParsedTemplate {
    private ParsedTemplateElement[] elements;

    public ParsedTemplate(ParsedTemplateElement[] elements) {
        if (elements==null) {
            this.elements=new ParsedTemplateElement[]{};
        }
        this.elements = elements;
    }

    public ParsedTemplateElement[] getElements() {
        return elements;
    }
}
