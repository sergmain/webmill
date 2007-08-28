package org.riverock.webmill.template;

import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.tools.ContainertStringUtils;
import org.riverock.webmill.template.schema.*;

import java.util.List;

/**
 * User: SMaslyukov
 * Date: 25.07.2007
 * Time: 14:33:39
 */
public class TemplateUtils {

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
