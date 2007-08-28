package org.riverock.webmill.template.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.riverock.common.tools.XmlTools;
import org.riverock.webmill.exception.ParseTemplatePortalException;
import org.riverock.webmill.template.schema.Template;
import org.riverock.webmill.utils.SingletonFactory;

/**
 * User: SMaslyukov
 * Date: 08.08.2007
 * Time: 18:56:47
 */
public class TemplateParser {
    private final static Logger log = Logger.getLogger(TemplateParser.class);

    public ParsedTemplateElement[] parse(byte[] bytes) throws ParseTemplatePortalException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream( bytes );
        return parse(inputStream);
    }

    public ParsedTemplateElement[] parse(InputStream inputStream) throws ParseTemplatePortalException {
        try {
            Template t = XmlTools.getObjectFromXml(Template.class, inputStream, SingletonFactory.getValidationEventHandler());

            byte[] templateBytes = XmlTools.getXml(t, "Template", "utf-8", true,
                new NamespacePrefixMapper[] {SingletonFactory.getNamespacePrefixMapper()}
            );
            String s = new String(templateBytes, "utf-8");

            int idx = s.indexOf("<Template");
            if (idx==-1) {
                throw new ParseTemplatePortalException("Invalid template format, <Template> element not found.");
            }
            idx = s.indexOf('>', idx+1);
            if (idx==-1) {
                throw new ParseTemplatePortalException("Invalid template format, closed bracket for <Template> element not found.");
            }
            int startTemplate = idx+1;

            idx = s.indexOf("</Template>");
            if (idx==-1) {
                throw new ParseTemplatePortalException("Invalid template format, closed element </Template> not found.");
            }

            int endTemplate = idx;

            List<ParsedTemplateElement> o = new ArrayList<ParsedTemplateElement>();
            List<Object> list = TemplateParserUtils.getElements(t);

            int startText = startTemplate;
            int endText;
            int ptr=0;
            while ((endText=s.indexOf("<element:", startText))!=-1) {
                o.add( new ParsedTemplateElement(s.substring(startText, endText-1)));
                startText = s.indexOf('>', endText)+1;
                if (s.charAt(startText-2)!='/') {
                    int i = s.indexOf(' ', endText+1);
                    int j = s.indexOf('>', endText+1);
                    if (i==-1) {
                        i=j;
                    }
                    //recursive include of element in current time not supported
                    startText = s.indexOf(s.substring(endText+1, i), startText);
                    if (s.charAt(startText-2)!='<' || s.charAt(startText-1)!='/') {
                        throw new ParseTemplatePortalException("Error processing template. Not valid xml format."); 
                    }
                    startText = s.indexOf('>', startText)+1;
                }
                o.add( new ParsedTemplateElement(list.get(ptr++)) );
            }
            if (ptr!=list.size()) {
                throw new ParseTemplatePortalException("Error processing template. Count of elements mismatch.");
            }
            o.add( new ParsedTemplateElement(s.substring(startText, endTemplate)) );

            return o.toArray(new ParsedTemplateElement[o.size()]);
        }
        catch (Exception e) {
            String es = "Error parse template";
            log.error(es, e);
            throw new ParseTemplatePortalException(es, e);
        }
    }
}
