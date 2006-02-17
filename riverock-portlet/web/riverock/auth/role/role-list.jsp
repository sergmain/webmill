<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>	

                <t:dataTable id="roleDataTable"
                        var="role"
                        value="#{roleBean.roleList}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{msg.header_table_role_name}" />
                       </f:facet>
                       <t:commandLink action="#{roleAction.selectRole}" immediate="true" >
                            <h:outputText value="#{role.name}" />
                            <t:updateActionListener property="#{roleSessionBean.currentRoleId}" value="#{role.id}" />
                       </t:commandLink>
                   </h:column>

                </t:dataTable>
