package org.riverock.webmill.template.parser;

import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.template.schema.Dynamic;
import org.riverock.webmill.template.schema.Xslt;
import org.riverock.webmill.template.schema.Template;

/**
 * User: SMaslyukov
 * Date: 09.08.2007
 * Time: 10:02:07
 */
public class ParsedTemplateElement {
    public static enum Type {XSLT, PORTLET, STRING, DYNAMIC, TEMPLATE}

    private Object o;
    private Type type;

    public ParsedTemplateElement(Object o) {
        if (o instanceof Xslt) {
            type = Type.XSLT;
        }
        else if (o instanceof Portlet) {
            type = Type.PORTLET;
        }
        else if (o instanceof Dynamic) {
            type = Type.DYNAMIC;
        }
        else if (o instanceof String) {
            type = Type.STRING;
        }
        else {
            throw new IllegalStateException("Wrong type of object. Must Portlet, Xslt, Dynamic or String. Real: " + o.getClass().getName());
        }
        this.o = o;
    }

    public Type getType() {
        return type;
    }

    public boolean isPortlet() {
        return type== Type.PORTLET;
    }

    public boolean isXslt() {
        return type== Type.XSLT;
    }

    public boolean isDynamic() {
        return type== Type.DYNAMIC;
    }

    public boolean isTemplate() {
        return type== Type.TEMPLATE;
    }

    public boolean isString() {
        return type== Type.STRING;
    }

    public Template getTemplate() {
        if (type== Type.TEMPLATE) {
            return (Template)o;
        }
        throw new IllegalStateException("Object is not Template");
    }

    public Portlet getPortlet() {
        if (type== Type.PORTLET) {
            return (Portlet)o;
        }
        throw new IllegalStateException("Object is not Portlet");
    }

    public Xslt getXslt() {
        if (type== Type.XSLT) {
            return (Xslt)o;
        }
        throw new IllegalStateException("Object is not Xslt");
    }

    public Dynamic getDynamic() {
        if (type== Type.DYNAMIC) {
            return (Dynamic)o;
        }
        throw new IllegalStateException("Object is not Dynamic");
    }

    public Dynamic getString() {
        if (type== Type.STRING) {
            return (Dynamic)o;
        }
        throw new IllegalStateException("Object is not String");
    }
}
