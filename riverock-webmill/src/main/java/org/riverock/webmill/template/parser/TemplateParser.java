package org.riverock.webmill.template.parser;

import java.io.InputStream;

import org.riverock.webmill.exception.ParseTemplatePortalException;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 18:56:47
 */
public interface TemplateParser {
    ParsedTemplate parse(byte[] bytes) throws ParseTemplatePortalException;
    
    ParsedTemplate parse(InputStream inputStream) throws ParseTemplatePortalException;
}
