<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.manager.resource.Site" var="msg"/>

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
    <h:form>

        <f:subview id="site-top-actions-subview">
            <jsp:include page="site-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="site-tree-subview" rendered="#{!siteSessionBean.edit}">
                <jsp:include page="site-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="css-delete-panel">

                <f:subview id="delete-css-subview">
                    <jsp:include page="css-description.jsp"/>
                </f:subview>

                <h:outputText value="#{msg['confirm_delete_css_action']}"/>
                <f:verbatim><br/></f:verbatim>

                <h:panelGroup id="operation-css-delete-panel">
                    <h:commandButton id="css-delete-process-action" action="#{cssAction.processDeleteCssAction}"
                                     value="#{msg['process_delete_css_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                    <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                    <h:commandButton id="css-delete-cancel-action" action="#{cssAction.cancelDeleteCssAction}"
                                     value="#{msg['cancel_delete_css_action']}"
                                     styleClass="site-button-action"
                        >
                    </h:commandButton>
                </h:panelGroup>
            </h:panelGroup>


        </h:panelGrid>

    </h:form>
</f:view>
