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

<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.cms.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .news-button-action {
        width: 150px;
        height: 22px;
    }

    .news-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="news-top-actions-subview">
            <jsp:include page="news-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="news-tree-subview">
                <jsp:include page="news-tree.jsp"/>
            </f:subview>


            <h:panelGrid columns="1" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">

                <f:subview id="subview-news-group-info">
                    <jsp:include page="news-group-add-edit.jsp"/>
                </f:subview>

                <h:panelGroup id="operation-news-group-edit-panel">
                    <h:commandButton id="news-group-edit-process-action" action="#{newsGroupAction.processEditNewsGroupAction}"
                                     value="#{msg['process_edit_news_group_action']}"
                                     styleClass="news-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="news-group-edit-cancel-action" action="#{newsGroupAction.cancelEditNewsGroupAction}"
                                     value="#{msg['cancel_edit_news_group_action']}"
                                     styleClass="news-button-action"
                                     immediate="true"
                        >
                    </h:commandButton>
                </h:panelGroup>

            </h:panelGrid>
        </h:panelGrid>

    </h:form>
</f:view>
