package org.riverock.webmill.template;

import java.beans.PropertyDescriptor;
import java.lang.Object;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.tools.ContainertStringUtils;
import org.riverock.webmill.template.schema.*;

/**
 * User: SMaslyukov
 * Date: 25.07.2007
 * Time: 14:33:39
 */
public class TemplateUtils {
    private final static Logger log = Logger.getLogger( TemplateUtils.class );

    public static String getString( final List<ElementParameter> v, final String nameParam) throws IllegalArgumentException{
        return getString(v, nameParam, null);
    }

    public static String getFullPortletName(String shortPortletName) {
        if ( shortPortletName.indexOf( PortletContainer.PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            return PortletContainer.PORTLET_ID_NAME_SEPARATOR + shortPortletName;
        }
        else {
            return shortPortletName;
        }
    }

    public static String getString( final List<ElementParameter> templateParameters, final String nameParam, final String defValue ) {
        if ( templateParameters == null || ContainertStringUtils.isBlank(nameParam) )
            return defValue;

        for (ElementParameter portalTemplateParameter : templateParameters) {
            if (portalTemplateParameter.getName().equals(nameParam))
                return portalTemplateParameter.getValue();
        }
        return defValue;
    }

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

    public static Template convertTemplate(SiteTemplate siteTemplate) {
        Template t = new Template();
        if (siteTemplate==null) {
            return t;
        }

        for (SiteTemplateItem templateItem : siteTemplate.getSiteTemplateItem()) {
            switch (PortalTemplateItemTypeImpl.valueOf(templateItem.getType()).getType()) {
                case PortalTemplateItemType.CUSTOM_TYPE:
                    Xslt custom = new Xslt();
                    custom.setName( templateItem.getValue() );
                    t.getPortletOrDynamicOrXslt().add(custom);
                    break;

                case PortalTemplateItemType.DYNAMIC_TYPE:
                    t.getPortletOrDynamicOrXslt().add(new Dynamic());
                    break;

                case PortalTemplateItemType.PORTLET_TYPE:
                    Portlet portlet = new Portlet();
                    portlet.setCode(templateItem.getCode());
                    portlet.setName(templateItem.getValue());
                    portlet.setXmlRoot(templateItem.getXmlRoot());
                    for (Parameter parameter : templateItem.getParameter()) {
                        ElementParameter p = new ElementParameter();
                        p.setName(parameter.getName());
                        p.setValue(parameter.getValue());
                        portlet.getElementParameter().add(p);
                    }
                    t.getPortletOrDynamicOrXslt().add(portlet);
                    break;
            }
        }
        return t;
    }
}
