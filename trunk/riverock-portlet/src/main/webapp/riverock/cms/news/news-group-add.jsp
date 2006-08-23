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

        <f:subview id="site-top-actions-subview">
            <jsp:include page="news-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="news-tree-subview">
                <jsp:include page="news-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="news-group-add-panel" rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">

                <h:panelGrid columns="1">

                    <f:subview id="subview-news-group-info">
                        <jsp:include page="news-group-add-edit.jsp"/>
                    </f:subview>

                    <h:panelGroup id="operation-news-group-add-panel">
                        <h:commandButton id="news-group-add-process-action" action="#{newsGroupAction.processAddNewsGroupAction}"
                                         value="#{msg.process_add_news_group_action}"
                                         styleClass="news-button-action"
                            >
                        </h:commandButton>
                        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                        <h:commandButton id="news-group-add-cancel-action" action="#{newsGroupAction.cancelAddNewsGroupAction}"
                                         value="#{msg.cancel_add_news_group_action}"
                                         styleClass="news-button-action"
                                         immediate="true"
                            >
                        </h:commandButton>
                    </h:panelGroup>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>

    </h:form>
</f:view>
