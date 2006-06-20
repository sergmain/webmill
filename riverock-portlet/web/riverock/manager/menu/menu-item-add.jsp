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

    .menu-button-action {
        width: 150px;
        height: 22px;
    }

    .menu-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>
<f:view>
    <h:outputText value="#{manager.not_logged}" style="font-size:12px" rendered="#{!isUserInRole['webmill.authentic']}"/>
    <h:form rendered="#{isUserInRole['webmill.authentic']}">

        <f:subview id="site-top-actions-subview">
            <jsp:include page="menu-top-actions.jsp"/>
        </f:subview>

        <h:panelGrid columns="2">

            <f:subview id="menu-tree-subview">
                <jsp:include page="menu-tree.jsp"/>
            </f:subview>

            <h:panelGroup id="menu-item-add-panel" rendered="#{isUserInRole['webmill.site-manager,webmill.menu']}">

                <h:panelGrid columns="1">

                    <f:subview id="subview-menu-item-info">
                        <h:outputText value="#{msg.menu_item_info}"/>
                        <jsp:include page="menu-item-add-concrete.jsp"/>
                    </f:subview>

                    <h:panelGroup id="operation-menu-item-add-panel">
                        <h:commandButton id="menu-item-add-process-action" action="#{menuAction.processAddMenuItemAction}"
                                         value="#{msg.process_add_menu_item_action}"
                                         styleClass="menu-button-action"
                            >
                        </h:commandButton>
                        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
                        <h:commandButton id="menu-item-add-cancel-action" action="#{menuAction.cancelAddMenuItemAction}"
                                         value="#{msg.cancel_add_menu_item_action}"
                                         styleClass="menu-button-action"
                            >
                        </h:commandButton>
                    </h:panelGroup>
                </h:panelGrid>
            </h:panelGroup>
        </h:panelGrid>

    </h:form>
</f:view>
