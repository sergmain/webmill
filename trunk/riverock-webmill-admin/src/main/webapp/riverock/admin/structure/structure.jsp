<%--
  ~ org.riverock.webmill.admin - Webmill portal admin web application
  ~ For more information about Webmill portal, please visit project site
  ~ http://webmill.askmore.info
  ~
  ~ Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
  ~ Riverock - The Open-source Java Development Community,
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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.webmill.admin.resource.Structure" var="msg"/>
<f:loadBundle basename="org.riverock.webmill.admin.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 24px;
    }

    .structure-button-action {
        width: 150px;
        height: 22px;
    }
    .styleFirstCol {
        width: 300px;
    }
    .styleSecondCol {
        width: 100px;
    }

    .styleSubFirstCol {
        width: 20px;
        border:none;
    }
    .styleSumSecondCol{
        border: 1px solid;
        width: 280px;
        border:none;
    }
    .styleSubThirdCol {
        width: 100px;
        border:none;
    }

</style>

<f:view>

    <h:form id="company_form">

        <h:commandButton id="company-list-action" action="company" value="#{manager.company_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="portal-user-list-action" action="portal-user" value="#{manager.portal_user_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="site-list-action" action="site" value="#{manager.site_button}"
                         styleClass="top-button-action"/>
        <h:commandButton id="db-list-action" action="db" value="#{manager.db_button}"
                         styleClass="top-button-action"/>

        <t:dataTable id="messagesTable"
                      var="message"
                      value="#{structureSessionBean.messages}"
                      preserveDataModel="false"
                      style="width:100%; border:none; ">
             <h:column>
                 <h:outputText value="#{message}"/>
             </h:column>
        </t:dataTable>

           <t:dataTable id="revisionModuleTable"
                         var="module"
                         value="#{structureService.manager.modules}"
                         preserveDataModel="false"
                         style="width:400px; border:1px none; ">
                <h:column>
                    <h:panelGrid columns="1" style="width:100%; border: 1px none;">

                        <h:panelGrid columns="2" style="width:100%; border: 1px none;" columnClasses="styleFirstCol, styleSecondCol">
                            <h:outputText value="#{module.description}"/>

                            <h:panelGroup>
                                <t:commandButton action="#{structureAction.applyModule}" value="#{msg.apply}" rendered="#{not module.complete}">
                                    <t:updateActionListener property="#{structureSessionBean.moduleName}" value="#{module.name}"/>
                                </t:commandButton>
                                <h:outputText value="#{msg.module_complete}" rendered="#{module.complete}"/>
                            </h:panelGroup>
                        </h:panelGrid>

                        <h:panelGroup>
                            <t:dataTable id="revisionVersionTable"
                                         var="version"
                                         value="#{module.versions}"
                                         preserveDataModel="false"
                                         style="width:100%; ; border:1px none; ">
                                <h:column>

                                    <h:panelGrid columns="3" columnClasses="styleSubFirstCol, styleSumSecondCol, styleSubThirdCol">
                                        <f:verbatim>&nbsp;</f:verbatim>

                                        <h:outputText value="#{version.versionName}"/>

                                        <h:panelGroup>
                                            <h:outputText value="#{msg.version_complete}" rendered="#{version.complete}"/>
                                            <t:commandButton action="#{structureAction.applyVersion}" value="#{msg.apply}" rendered="#{not version.complete}">
                                                <t:updateActionListener property="#{structureSessionBean.moduleName}" value="#{module.name}"/>
                                                <t:updateActionListener property="#{structureSessionBean.versionName}" value="#{version.versionName}"/>
                                            </t:commandButton>
                                        </h:panelGroup>
                                    </h:panelGrid>
                                </h:column>
                            </t:dataTable>
                        </h:panelGroup>

                    </h:panelGrid>
                </h:column>

            </t:dataTable>
            
     </h:form>
</f:view>
