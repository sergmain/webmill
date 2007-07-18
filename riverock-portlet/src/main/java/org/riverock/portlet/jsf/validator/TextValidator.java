package org.riverock.portlet.jsf.validator;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * User: SMaslyukov
 * Date: 17.07.2007
 * Time: 17:13:02
 */
public class TextValidator implements Validator, StateHolder {
    private static Logger log = Logger.getLogger(TextValidator.class);

    private String type;

    private boolean transientValue = false;
    final static ErrorHandler ERROR_HANDLER = new ErrorHandler() {
        public void warning(SAXParseException saxParseException) throws SAXException {
            throw saxParseException;
        }

        public void error(SAXParseException saxParseException) throws SAXException {
            throw saxParseException;
        }

        public void fatalError(SAXParseException saxParseException) throws SAXException {
            throw saxParseException;
        }
    };

    final static ErrorListener ERROR_LISTENER = new ErrorListener() {
        public void warning(TransformerException transformerException) throws TransformerException {
            throw transformerException;
        }

        public void error(TransformerException transformerException) throws TransformerException {
            throw transformerException;
        }

        public void fatalError(TransformerException transformerException) throws TransformerException {
            throw transformerException;
        }
    };

    public TextValidator() {
        log.debug("Exec TextValidator()");
    }

    public TextValidator(String type) {
        log.debug("Exec TextValidator(type), type: " + type);
        this.type = type;
    }

    public void validate(FacesContext context, UIComponent component, Object toValidate) {
        log.debug("Start org.riverock.portlet.jsf.validator.TextValidator.validate(), type: "+type);

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (!(component instanceof UIInput)) {
            return;
        }
        if (toValidate == null) {
            return;
        }

        String result;
        if (StringUtils.equals(type, "xml")) {
            result = validateAsXml(toValidate.toString());
        }
        else if (StringUtils.equals(type, "xslt")) {
            result = validateAsXslt(toValidate.toString());
        }
        else {
            throw new IllegalStateException("Wrong value of 'type' attribute: "+type);
        }
        if (StringUtils.isNotBlank(result)) {
            FacesMessage errMsg = new FacesMessage("Text not validated as '"+type+"'. Error: " + result);
            throw new ValidatorException(errMsg);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        log.debug("new type: " + type);
        this.type = type;
    }

    static String validateAsXslt(String xslt) {
        try {
            Source xslSource = new StreamSource(
                new StringReader(xslt)
            );

            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setErrorListener( ERROR_LISTENER );
            factory.newTemplates(xslSource);
        }
        catch (Throwable e) {
            return e.toString();
        }
        return null;
    }

    static String validateAsXml(String value) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            factory.setValidating(false);
            factory.setValidating(false);

            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            docBuilder.setErrorHandler( ERROR_HANDLER );
            ByteArrayInputStream is = new ByteArrayInputStream(value.getBytes());
            docBuilder.parse(is);
        }
        catch (Throwable e) {
            return e.toString();
        }
        return null;
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[1];
        values[0] = type;
        return (values);
    }


    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        type = (String) values[0];
    }

    public boolean isTransient() {
        return this.transientValue;
    }

    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextValidator)) {
            return false;
        }

        final TextValidator lengthValidator = (TextValidator)o;

        return !(type != null ? !type.equals(lengthValidator.type) : lengthValidator.type != null);
    }
}
