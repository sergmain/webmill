package org.riverock.update.webmill.v581.convert_template;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2007
 * Time: 23:14:18
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
