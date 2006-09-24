<%--
  ~ org.riverock.commerce - Commerce application
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

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


<f:loadBundle basename="org.riverock.commerce.resource.StandardCurrency" var="msg"/>
<f:loadBundle basename="org.riverock.commerce.resource.Manager" var="manager"/>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="add_standard_currency_form" rendered="#{isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="1" rendered="#{!empty standardCurrencySessionBean.standardCurrencyBean and isUserInRole['webmill.portal-manager']}">

            <h:panelGrid columns="2">
                <h:outputText value="#{msg.new_curs}"/>
                <h:inputText id="curs-field" value="#{standardCurrencySessionBean.currentCurs}" size="20"/>
            </h:panelGrid>

            <h:panelGroup>
                <h:commandButton value="#{msg.add_curs_process_action}" action="#{standardCurrencyAction.processAddCurs}"
                                 styleClass="sub-top-button-action"/>
                <h:commandButton value="#{msg.add_curs_cancel_action}" action="#{standardCurrencyAction.cancelAddCurs}"
                                 styleClass="sub-top-button-action"
                                 immediate="true"
                    />
            </h:panelGroup>

        </h:panelGrid>

    </h:form>
</f:view>
