<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

    <f:verbatim><br /></f:verbatim>
    <h:outputText value="#{msg['role_list']}" styleClass="standard_bold" />
    <f:verbatim><br /><br /></f:verbatim>

    <h:selectOneMenu value="#{holdingDataProvider.currentUser.newRoleId}"
	styleClass="selectOneMenu" required="true" 
	>
            <f:selectItems value="#{holdingDataProvider.roleList}" />
        </h:selectOneMenu>

    <t:commandButton value="#{msg['add_role']}" action="#{holdingAction.addRoleAction}">
    </t:commandButton>

    <f:verbatim><br/></f:verbatim>

        <t:dataTable id="role-edit-list"
                styleClass="standardTable"
                headerClass="standardTable_Header"
                rowClasses="standardTable_Row1,standardTable_Row2"
                columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                var="role"
                value="#{holdingDataProvider.currentUser.roles}"
                preserveDataModel="true">

           <h:column>
               <f:facet name="header">
                    <h:outputText value="#{msg['role_name']}" />
               </f:facet>
               <h:outputText id="role-name" value="#{role.name}" />
           </h:column>

           <h:column>
                <t:commandButton value="#{msg['delete_role']}" actionListener="#{holdingAction.deleteRoleActionListener}">
                    <t:updateActionListener property="#{holdingSessionBean.currentRoleId}" value="#{role.roleId}" />
		</t:commandButton>
           </h:column>
        </t:dataTable>
