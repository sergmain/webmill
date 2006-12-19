/*
 * org.riverock.webmill.container - Webmill portlet container implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.container.portlet_definition;

import org.riverock.webmill.container.portlet.bean.*;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet_definition.portlet_api_v1.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 10.09.2006
 * Time: 0:35:43
 * <p/>
 * $Id$
 */
public class ProcessAs_v1_0 {
    public static PortletApplication processAs_v1_0(File portletFile) throws PortletContainerException {
        try {
            FileInputStream inputStream = new FileInputStream(portletFile);
            return processInputStream(inputStream);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new PortletContainerException("Error", e);
        }
    }

    public static PortletApplication processAs_v1_0(InputStream inputStream) throws PortletContainerException {
        try {
            return processInputStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PortletContainerException("Error", e);
        }
    }

    private static PortletApplication processInputStream(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance ("org.riverock.webmill.container.portlet_definition.portlet_api_v1");

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Source source =  new StreamSource(inputStream);
        JAXBElement<PortletAppType> bookingElement = unmarshaller.unmarshal( source, PortletAppType.class);

        PortletAppType portletApplication = bookingElement.getValue();

        return initPortletApplication(portletApplication);
    }

    private static PortletApplication initPortletApplication(PortletAppType portletApp) {
        PortletApplication portletApplication = new PortletApplication();
        portletApplication.setId(portletApp.getId());
        portletApplication.setVersion(portletApp.getVersion());
        portletApplication.setCustomPortletMode(initCustomPortletMode(portletApp));
        portletApplication.setCustomWindowState(initCustomWindowState(portletApp));
        portletApplication.setPortlet(initPortlet(portletApp));
        portletApplication.setSecurityConstraint(initSecurityConstraint(portletApp));
        portletApplication.setUserAttribute(initUserAttribute(portletApp));
        return portletApplication;
    }

    private static List<UserAttribute> initUserAttribute(PortletAppType portletApp) {
        List<UserAttribute> attributes = new ArrayList<UserAttribute>();
        for (UserAttributeType userAttributeType : portletApp.getUserAttribute()) {
            UserAttribute attribute =  new UserAttribute();
            attribute.setId(userAttributeType.getId());
            attribute.setName(userAttributeType.getName().getValue());
            attribute.setDescription(initDescription(userAttributeType.getDescription()));
            attributes.add(attribute);
        }
        return attributes;
    }

    private static List<SecurityConstraint> initSecurityConstraint(PortletAppType portletApp) {
        List<SecurityConstraint> securityConstraints = new ArrayList<SecurityConstraint>();
        for (SecurityConstraintType securityConstraintType : portletApp.getSecurityConstraint()) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setId(securityConstraintType.getId());
            PortletCollection portletCollection = new PortletCollection();
            List<String> portletNames = new ArrayList<String>();
            for (PortletNameType portletNameType : securityConstraintType.getPortletCollection().getPortletName()) {
                portletNames.add(portletNameType.getValue());
            }
            portletCollection.setPortletName(portletNames);
            securityConstraint.setPortletCollection(portletCollection);
            securityConstraints.add(securityConstraint);
        }
        return securityConstraints;
    }

    private static List<PortletDefinition> initPortlet(PortletAppType portletApp) {
        List<PortletDefinition> portletDefinitions = new ArrayList<PortletDefinition>();
        for (PortletType portletType : portletApp.getPortlet()) {
            PortletDefinition def = new PortletDefinition();
            def.setId(portletType.getId());
            def.setDescription(initDescription(portletType.getDescription()));
            def.setDisplayName(initDisplayName(portletType.getDisplayName()));
            if (portletType.getExpirationCache()!=null) {
                def.setExpirationCache(portletType.getExpirationCache().getValue());
            }
            def.setInitParam(initInitParam(portletType.getInitParam()));
            def.setPortletClass(portletType.getPortletClass());
            def.setPortletInfo(initPortletInfo(portletType.getPortletInfo()));
            def.setPortletName(portletType.getPortletName().getValue());
            def.setPreferences(initPreferences(portletType.getPortletPreferences()));
            if (portletType.getResourceBundle()!=null) {
                def.setResourceBundle(portletType.getResourceBundle().getValue());
            }
            def.setSecurityRoleRef(initSecurityRoleRef(portletType.getSecurityRoleRef()));
            def.setSupportedLocale(initSupportedLocale(portletType.getSupportedLocale()));
            def.setSupports(initSupports(portletType.getSupports()));

            portletDefinitions.add(def);
        }

        return portletDefinitions;
    }

    private static List<Supports> initSupports(List<SupportsType> supportsTypes) {
        List<Supports> list = new ArrayList<Supports>();
        for (SupportsType supportsType : supportsTypes) {
            Supports supports =  new Supports();
            supports.setId(supportsType.getId());
            supports.setMimeType(supportsType.getMimeType().getValue());
            supports.setPortletMode(initPortletMode(supportsType.getPortletMode()));
            list.add(supports);
        }
        return list;
    }

    private static List<String> initPortletMode(List<PortletModeType> portletMode) {
        List<String> list = new ArrayList<String>();
        for (PortletModeType portletModeType : portletMode) {
            list.add(portletModeType.getValue());
        }
        return list;
    }

    private static List<String> initSupportedLocale(List<SupportedLocaleType> supportedLocale) {
        List<String> list = new ArrayList<String>();
        for (SupportedLocaleType supportedLocaleType : supportedLocale) {
            list.add(supportedLocaleType.getValue());
        }
        return list;
    }

    private static List<SecurityRoleRef> initSecurityRoleRef(List<SecurityRoleRefType> securityRoleRef) {
        List<SecurityRoleRef> roleRefs = new ArrayList<SecurityRoleRef>();
        for (SecurityRoleRefType securityRoleRefType : securityRoleRef) {
            SecurityRoleRef roleRef =  new SecurityRoleRef();
            roleRef.setId(securityRoleRefType.getId());
            if (securityRoleRefType.getRoleLink()!=null) {
                roleRef.setRoleLink(securityRoleRefType.getRoleLink().getValue());
            }
            roleRef.setRoleName(securityRoleRefType.getRoleName());
            roleRef.setDescription(initDescription(securityRoleRefType.getDescription()));
            roleRefs.add(roleRef);
        }
        return roleRefs;
    }

    private static Preferences initPreferences(PortletPreferencesType portletPreferences) {
        if (portletPreferences==null) {
            return null;
        }
        Preferences preferences = new Preferences();
        preferences.setId(portletPreferences.getId());
        preferences.setPreferencesValidatorClass(portletPreferences.getPreferencesValidator());
        List<Preference> list = new ArrayList<Preference>();
        for (PreferenceType preferenceType : portletPreferences.getPreference()) {
            Preference preference = new Preference();
            preference.setId(preferenceType.getId());
            if (preferenceType.getReadOnly()!=null) {
                preference.setReadOnly(Boolean.parseBoolean(preferenceType.getReadOnly().value()));
            }
            List<String> values = new ArrayList<String>();
            for (ValueType valueType : preferenceType.getValue()) {
                values.add(valueType.getValue());
            }
            preference.setValue(values);
            preference.setName(preferenceType.getName().getValue());
            list.add(preference);
        }
        preferences.setPreferenceList(list);
        return preferences;
    }

    private static PortletInfo initPortletInfo(PortletInfoType portletInfo) {
        PortletInfo info = new PortletInfo();
        info.setId(portletInfo.getId());
        if (portletInfo.getShortTitle()!=null) {
            info.setShortTitle(portletInfo.getShortTitle().getValue());
        }
        info.setTitle(portletInfo.getTitle().getValue());
        if (portletInfo.getKeywords()!=null) {
            info.setKeywords(portletInfo.getKeywords().getValue());
        }
        return info;
    }

    private static List<InitParam> initInitParam(List<InitParamType> initParam) {
        List<InitParam> params = new ArrayList<InitParam>();
        for (InitParamType initParamType : initParam) {
            InitParam param =  new InitParam();
            param.setId(initParamType.getId());
            param.setName(initParamType.getName().getValue());
            param.setValue(initParamType.getValue().getValue());
            param.setDescription(initDescription(initParamType.getDescription()));
            params.add(param);
        }
        return params;
    }

    private static List<CustomPortletMode> initCustomPortletMode(PortletAppType portletApp) {
        List<CustomPortletMode> customPortletModes = new ArrayList<CustomPortletMode>();
        for (CustomPortletModeType customPortletModeType : portletApp.getCustomPortletMode()) {
            CustomPortletMode mode =  new CustomPortletMode();
            mode.setId(customPortletModeType.getId());
            mode.setPortletMode(customPortletModeType.getPortletMode().getValue());
            mode.setDescription(initDescription(customPortletModeType.getDescription()));
            customPortletModes.add(mode);
        }
        return customPortletModes;
    }

    private static List<CustomWindowState> initCustomWindowState(PortletAppType portletApp) {
        List<CustomWindowState> customWindowStates = new ArrayList<CustomWindowState>();
        for (CustomWindowStateType customWindowStateType : portletApp.getCustomWindowState()) {
            CustomWindowState state = new CustomWindowState();
            state.setId(customWindowStateType.getId());
            state.setWindowState(customWindowStateType.getWindowState().getValue());
            state.setDescription(initDescription(customWindowStateType.getDescription()));
        }
        return customWindowStates;
    }

    private static List<Description> initDescription(List<DescriptionType> descriptionTypes) {
        List<Description> descriptions = new ArrayList<Description>();
        for (DescriptionType descriptionType : descriptionTypes) {
            Description description = new Description();
            description.setLang(descriptionType.getLang());
            description.setContent(descriptionType.getValue());
            descriptions.add(description);
        }
        return descriptions;
    }

    private static List<DisplayName> initDisplayName(List<DisplayNameType> displayName) {
        List<DisplayName> names = new ArrayList<DisplayName>();
        for (DisplayNameType displayNameType : displayName) {
            DisplayName name = new DisplayName();
            name.setLang(displayNameType.getLang());
            name.setContent(displayNameType.getValue());
            names.add(name);
        }
        return names;
    }

}
