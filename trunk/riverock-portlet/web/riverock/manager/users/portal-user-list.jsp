<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortalUser" var="msg"/>

    <h:commandButton value="#{msg.portal_user_add_portal_user_action}" action="#{portalUserAction.addPortalUser}" style=" width : 106px; height : 22px;"/>

                <t:dataTable id="portalUserDataTable"
                        var="portalUser"
                        value="#{portalUserService.portalUserList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_portal_user_name}" />
                       </f:facet>
                       <t:commandLink action="#{portalUserAction.selectPortalUser}" immediate="true" >
                            <h:outputText value="#{portalUser.name}" />
                            <t:updateActionListener property="#{portalUserSessionBean.currentPortalUserId}" value="#{portalUser.userId}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
