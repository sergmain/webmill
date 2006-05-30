<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.PortletName" var="msg"/>

    <h:commandButton value="#{msg.add_portlet_name_action}" action="#{portletNameAction.addPortletName}" style=" width : 106px; height : 22px;"/>

                <t:dataTable id="portletNameDataTable"
                        var="portletName"
                        value="#{portletNameService.portletNameList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_portlet_name_name}" />
                       </f:facet>
                       <t:commandLink action="#{portletNameAction.selectPortletName}" immediate="true" >
                            <h:outputText value="#{portletName.portletName}" />
                            <t:updateActionListener property="#{portletNameSessionBean.currentPortletNameId}" value="#{portletName.portletId}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
