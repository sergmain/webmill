<%--
  ~ org.riverock.portlet - Portlet Library
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

<f:loadBundle basename="org.riverock.portlet.cms.resource.Article" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.cms.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .article-button-action {
        width: 150px;
        height: 22px;
    }

    .article-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="article-top-actions-subview">
            <jsp:include page="article-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="article-tree-subview">
                <jsp:include page="article-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="article-add-panel" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.article-manager']}">

                <h:panelGrid columns="1">

                    <f:subview id="subview-article-info">
                        <h:outputText value="#{msg.article_info}"/>
                        <jsp:include page="article-add-concrete.jsp"/>
                    </f:subview>

                    <h:panelGroup id="operation-article-add-panel">
                        <h:commandButton id="article-add-process-action" action="#{articleAction.processAddArticleAction}"
                                         value="#{msg.process_add_article_action}"
                                         styleClass="article-button-action"
                            >
                        </h:commandButton>
                        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                        <h:commandButton id="article-add-cancel-action" action="#{articleAction.cancelAddArticleAction}"
                                         value="#{msg.cancel_add_article_action}"
                                         styleClass="article-button-action"
                                         immediate="true"
                            >
                        </h:commandButton>
                    </h:panelGroup>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>

    </h:form>
</f:view>
