<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>
<f:loadBundle basename="org.riverock.portlet.manager.resource.Manager" var="manager"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .site-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="site-top-actions-subview">
            <jsp:include page="site-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="site-tree-subview">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGrid columns="1" rendered="#{isUserInRole['webmill.portal-manager,webmill.site-manager,webmill.template']}">

                <f:subview id="subview-template-info">
                    <jsp:include page="template-add-edit.jsp"/>
                </f:subview>

                <h:panelGroup id="operation-template-add-panel">
                    <h:commandButton id="template-add-process-action" action="#{templateAction.processAddTemplateAction}"
                                     value="#{msg['process_add_template_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="template-add-cancel-action" action="#{templateAction.cancelAddTemplateAction}"
                                     value="#{msg['cancel_add_template_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                </h:panelGroup>
            </h:panelGrid>

        </h:panelGrid>

    </h:form>
</f:view>
