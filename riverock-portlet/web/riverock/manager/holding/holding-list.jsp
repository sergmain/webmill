<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

                <t:dataTable id="holdingyDataTable"
                        var="holding"
                        value="#{holdingService.holdingList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_holding_name}" />
                       </f:facet>
                       <t:commandLink action="#{action.selectHolding}" immediate="true" >
                            <h:outputText value="#{holding.name}" />
                            <t:updateActionListener property="#{companySessionBean.currentHoldingId}" value="#{holding.id}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
