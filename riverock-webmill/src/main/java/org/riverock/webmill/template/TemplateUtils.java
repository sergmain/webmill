package org.riverock.webmill.template;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import org.riverock.webmill.template.schema.Html;
import org.riverock.webmill.template.schema.Portlet;
import org.riverock.webmill.template.schema.Custom;
import org.riverock.webmill.template.schema.Dynamic;
import org.riverock.webmill.template.schema.Template;
import org.riverock.webmill.template.schema.SiteTemplate;
import org.riverock.webmill.template.schema.SiteTemplateItem;
import org.riverock.webmill.template.schema.Parameter;
import org.riverock.webmill.template.schema.ElementParameter;
import org.riverock.webmill.container.tools.ContainertStringUtils;
import org.riverock.webmill.container.portlet.PortletContainer;

/**
 * User: SMaslyukov
 * Date: 25.07.2007
 * Time: 14:33:39
 */
public class TemplateUtils {

    public static String getString( final List<PortalTemplateParameter> v, final String nameParam) throws IllegalArgumentException{
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

    public static String getString( final List<PortalTemplateParameter> templateParameters, final String nameParam, final String defValue ) {
        if ( templateParameters == null || ContainertStringUtils.isBlank(nameParam) )
            return defValue;

        for (PortalTemplateParameter portalTemplateParameter : templateParameters) {
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
        if  ((obj instanceof Portlet) || (obj instanceof Custom) || (obj instanceof Dynamic)) {
            elements.add(obj);
            return;
        }

        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(obj);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object o = PropertyUtils.getProperty(obj, propertyDescriptor.getName());
            if (o==null) {
                continue;
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
            if  ((o instanceof Portlet) || (o instanceof Custom) || (o instanceof Dynamic)) {
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
                    Custom custom = new Custom();
                    custom.setValue( templateItem.getValue() );
                    t.getPortletOrDynamicOrCustom().add(custom);
                    break;

                case PortalTemplateItemType.DYNAMIC_TYPE:
                    t.getPortletOrDynamicOrCustom().add(new Dynamic());
                    break;

                case PortalTemplateItemType.PORTLET_TYPE:
                    Portlet portlet = new Portlet();
                    portlet.setCode(templateItem.getCode());
                    portlet.setValue(templateItem.getValue());
                    portlet.setXmlRoot(templateItem.getXmlRoot());
                    for (Parameter parameter : templateItem.getParameter()) {
                        ElementParameter p = new ElementParameter();
                        p.setName(parameter.getName());
                        p.setValue(parameter.getValue());
                        portlet.getElementParameter().add(p);
                    }
                    t.getPortletOrDynamicOrCustom().add(portlet);
                    break;
            }
        }
        return t;
    }
}
