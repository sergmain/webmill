<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.riverock.portlet.resource.Webclip" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .webclip-button-action {
        width: 150px;
        height: 22px;
    }
</style>


<f:view>
    <h:form id="foo" rendered="#{isUserInRole['webmill.portal-manager,webmill.webclip-manager']}">


        <h:outputText value="#{msg.select_catalog_info}"/>

        <h:panelGrid columns="1">
            <h:outputText value="#{msg.catalog_list}"/>
            <h:panelGroup>
                <h:selectOneMenu value="#{webclipSessionBean.catalogLanguageId}"
                                 styleClass="selectOneMenu" required="true" style="width:400px"
                    >
                    <f:selectItems value="#{webclipService.catalogList}"/>
                </h:selectOneMenu>
            </h:panelGroup>
        </h:panelGrid>

        <h:panelGrid columns="2">
            <h:panelGroup>
                <h:panelGrid>
                    <h:commandButton action="#{webclipAction.bulkCreateMenus}" value="#{msg.bulk_create_menus_button}"
                                     styleClass="webclip-button-action"/>
                    <h:outputText value="#{msg.bulk_create_menus_info}" style="font-size:10px"/>
                </h:panelGrid>
            </h:panelGroup>
            <h:panelGroup>
                <h:panelGrid>
                    <h:commandButton action="webclip-manager" value="#{msg.cancel_bulk_create_menus_button}"
                                     styleClass="webclip-button-action"/>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>

        <h:panelGrid columns="1">
            <h:outputText value="#{msg.webclip_urls}"/>
            <h:inputTextarea value="#{webclipSessionBean.urls}" rows="50" cols="70" required="true" style="width:600px"/>
        </h:panelGrid>

        <h:panelGrid columns="2">
            <h:panelGroup>
                <h:panelGrid>
                    <h:commandButton action="#{webclipAction.bulkCreateMenus}" value="#{msg.bulk_create_menus_button}"
                                     styleClass="webclip-button-action"/>
                    <h:outputText value="#{msg.bulk_create_menus_info}" style="font-size:10px"/>
                </h:panelGrid>
            </h:panelGroup>
            <h:panelGroup>
                <h:panelGrid>
                    <h:commandButton action="webclip-manager" value="#{msg.cancel_bulk_create_menus_button}"
                                     styleClass="webclip-button-action"/>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>

    </h:form>
</f:view>


