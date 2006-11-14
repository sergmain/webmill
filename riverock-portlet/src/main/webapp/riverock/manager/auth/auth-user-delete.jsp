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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .auth-button-action {
        width: 150px;
        height: 22px;
    }

    .auth-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>

<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form id="foo" rendered="#{userSessionBean.delete and isUserInRole['webmill.authentic']}">

        <h:panelGrid columns="2" rendered="#{isUserInRole['webmill.portal-manager,webmill.auth']}">

            <f:subview id="select-user-subview">
                <jsp:include page="auth-tree.jsp"/>
            </f:subview>


            <h:panelGroup id="delete-user-panel" rendered="#{userSessionBean.delete}">

                <f:subview id="delete-user-info-subview">
                    <jsp:include page="auth-user-info.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:commandButton id="process-delete-user-action" action="#{authUserAction.processDeleteUserAction}"
                                 value="#{msg['process_delete_user_action']}" 
                                 styleClass="auth-button-action"
                    >
                </h:commandButton>
                <h:commandButton id="cancel-delete-user-action" action="#{authUserAction.cancelDeleteUserAction}"
                                 value="#{msg['cancel_delete_user_action']}" immediate="true"
                                 styleClass="auth-button-action"
                    >
                </h:commandButton>

            </h:panelGroup>

        </h:panelGrid>


    </h:form>
</f:view>

