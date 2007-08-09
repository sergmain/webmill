package org.riverock.webmill.template.parser;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 18:57:58
 */
public class TemplateParserFactory {
    private static final TemplateParser TEMPLATE_PARSER = new TemplateParserImpl();

    public static TemplateParser getTemplateParser() {
        return TEMPLATE_PARSER;
    }
}
