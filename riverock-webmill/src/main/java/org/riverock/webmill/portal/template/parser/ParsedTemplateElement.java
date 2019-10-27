package org.riverock.webmill.portal.template.parser;

import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.template.schema.Dynamic;
import org.riverock.webmill.template.schema.Xslt;
import org.riverock.webmill.template.schema.Include;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 10:02:07
 */
public class ParsedTemplateElement {

    private Object o;
    private TemplateType type;

    protected void finalize() {
        o=null;
        type=null;
    }

    public ParsedTemplateElement(Object o) {
        if (o instanceof Xslt) {
            type = TemplateType.XSLT;
        }
        else if (o instanceof Portlet) {
            type = TemplateType.PORTLET;
        }
        else if (o instanceof Dynamic) {
            type = TemplateType.DYNAMIC;
        }
        else if (o instanceof String) {
            type = TemplateType.STRING;
        }
        else if (o instanceof Include) {
            type = TemplateType.INCLUDE;
        }
        else {
            throw new IllegalStateException("Wrong type of object. Must Portlet, Xslt, Dynamic or String. Real: " + o.getClass().getName());
        }
        this.o = o;
    }

    public TemplateType getType() {
        return type;
    }

    public boolean isPortlet() {
        return type== TemplateType.PORTLET;
    }

    public boolean isXslt() {
        return type== TemplateType.XSLT;
    }

    public boolean isDynamic() {
        return type== TemplateType.DYNAMIC;
    }

    public boolean isInclude() {
        return type== TemplateType.INCLUDE;
    }

    public boolean isString() {
        return type== TemplateType.STRING;
    }

    public Include getInclude() {
        if (type== TemplateType.INCLUDE) {
            return (Include)o;
        }
        throw new IllegalStateException("Object is not Include");
    }

    public Portlet getPortlet() {
        if (type== TemplateType.PORTLET) {
            return (Portlet)o;
        }
        throw new IllegalStateException("Object is not Portlet");
    }

    public Xslt getXslt() {
        if (type== TemplateType.XSLT) {
            return (Xslt)o;
        }
        throw new IllegalStateException("Object is not Xslt");
    }

    public Dynamic getDynamic() {
        if (type== TemplateType.DYNAMIC) {
            return (Dynamic)o;
        }
        throw new IllegalStateException("Object is not Dynamic");
    }

    public String getString() {
        if (type== TemplateType.STRING) {
            return (String)o;
        }
        throw new IllegalStateException("Object is not String");
    }
}
