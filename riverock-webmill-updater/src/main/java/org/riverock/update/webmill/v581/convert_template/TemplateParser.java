package org.riverock.update.webmill.v581.convert_template;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.JAXBException;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import org.riverock.update.webmill.v581.convert_template.schema.Template;
import org.riverock.dbrevision.utils.Utils;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2007
 * Time: 23:16:49
 */
public class TemplateParser {
    private final static NamespacePrefixMapper namespacePrefixMapper = new NamespacePrefixMapperImpl();
    private final static ValidationEventHandler validationEventHandler = new JaxbValidationEventHandler();

    public static ParsedTemplate parse(InputStream inputStream) throws JAXBException, UnsupportedEncodingException {
            Template t = Utils.getObjectFromXml(Template.class, inputStream, validationEventHandler);


            byte[] templateBytes = Utils.getXml(t, "Template", "utf-8", true,
                new NamespacePrefixMapper[] {namespacePrefixMapper}
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
            List<Object> list = TemplateUtils.getElements(t);

            int startText = startTemplate;
            int endText;
            int ptr=0;
            while ((endText=s.indexOf("<element:", startText))!=-1) {
                o.add( new ParsedTemplateElement(s.substring(startText, endText-1)));
                startText = s.indexOf('>', endText)+1;
                o.add( new ParsedTemplateElement(list.get(ptr++)) );
            }
            if (ptr!=list.size()) {
                throw new ParseTemplatePortalException("Error processing template. Count of elements mismatch.");
            }
            o.add( new ParsedTemplateElement(s.substring(startText, endTemplate)) );

            return new ParsedTemplate(o.toArray(new ParsedTemplateElement[o.size()]));
    }
}
