package org.riverock.webmill.portal.template.parser;

import org.riverock.webmill.template.schema.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.Object;
import java.beans.PropertyDescriptor;

/**
 * User: SergeMaslyukov
 * Date: 28.08.2007
 * Time: 22:09:53
 */
public class TemplateParserUtils {
    private final static Logger log = Logger.getLogger( TemplateParserUtils.class );

    public static List<Object> getElements(Template template) {
        List<Object> elements = new ArrayList<Object>();
        try {
            getElements(template, elements);
        }
        catch (Exception e) {
            throw new RuntimeException("Error process Html object", e);
        }
        return elements;
    }

    private static void getElements(Object obj, List<Object> elements) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (obj==null) {
            return;
        }
        if  ((obj instanceof Portlet) || (obj instanceof Xslt) || (obj instanceof Dynamic) || (obj instanceof Include)) {
            elements.add(obj);
            return;
        }

        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object o = PropertyUtils.getProperty(obj, propertyDescriptor.getName());
            if (o==null) {
                continue;
            }
            if (o.getClass()==null) {
                log.debug("Object o: " + o);
                log.debug("Object o class: " + o.getClass());
                continue;
            }
            if (o.getClass().getPackage()==null) {
                log.debug("Object o: " + o);
                log.debug("Object o class: " + o.getClass());
                log.debug("Object o package: " + o.getClass().getPackage());
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("o.getClass().getPackage().getName(): " + o.getClass().getPackage().getName());
            }

            if (o instanceof List) {
                for (Object o1 : (List)o) {
                    getElements(o1, elements);
                }
                continue;
            }
            if (!(o.getClass().getPackage().getName().equals(Html.class.getPackage().getName()))) {
                continue;
            }
            if  ((o instanceof Portlet) || (o instanceof Xslt) || (o instanceof Dynamic) || (o instanceof Include)) {
                elements.add(o);
            }
            else {
                getElements(o, elements);
            }
        }
    }
}
