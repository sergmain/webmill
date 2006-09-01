<%--
  ~ org.riverock.commerce - Commerce application
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
<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<f:view>
<h:form id="foo" rendered="#{isUserInRole['webmill.authentic']}">

    <f:loadBundle basename="org.riverock.commerce.resource.Manager" var="manager"/>

    <style type="text/css">
        TD {
            vertical-align: top;
        }

        .top-button-action {
            width: 120px;
            height: 20px;
        }

        .sub-top-button-action {
            width: 150px;
            height: 22px;
        }
    </style>

    <h:panelGrid columns="2">

        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="statdard-currency-action" action="standard-currency" value="#{manager.standard_currency_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.standard_currency_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>
        <h:panelGroup>
            <h:panelGrid rendered="#{isUserInRole['webmill.portal-manager,webmill.commerce-manager']}">
                <h:commandButton id="currency-action" action="currency" value="#{manager.currency_button}"
                                 styleClass="top-button-action"/>
                <h:outputText value="#{manager.currency_button_info}" styleClass="top-button-action"/>
            </h:panelGrid>
        </h:panelGroup>

    </h:panelGrid>


</h:form>
<h:outputText value="not logged" style="font-size:10px" rendered="#{!isUserInRole['webmill.authentic']}"/>
</f:view>