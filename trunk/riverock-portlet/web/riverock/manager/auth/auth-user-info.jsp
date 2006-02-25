<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Auth" var="msg"/>

<h:panelGrid columns="2">

	<h:outputText value="User name:"/>
	<h:outputText value="#{dataProvider.currentUser.userName}" />

	<h:outputText value="User login:"/>
	<h:outputText value="#{dataProvider.currentUser.authInfo.userLogin}" />

	<h:outputText value="Company:"/>
    <h:panelGroup id="company-level-group">
	<t:graphicImage value="/images/yes.gif" rendered="#{dataProvider.currentUser.authInfo.company}" border="0"/>
	<t:graphicImage value="/images/no.gif" rendered="#{!dataProvider.currentUser.authInfo.company}" border="0"/>
	<h:outputText value="#{dataProvider.currentUser.companyName}" />
    </h:panelGroup>


	<h:outputText value="Holding:"/>
    <h:panelGroup id="holding-level-group">
	<t:graphicImage value="/images/yes.gif" rendered="#{dataProvider.currentUser.authInfo.holding}" border="0"/>
	<t:graphicImage value="/images/no.gif" rendered="#{!dataProvider.currentUser.authInfo.holding}" border="0"/>
	<h:outputText value="#{dataProvider.currentUser.holdingName}" />
    </h:panelGroup>
</h:panelGrid>

<h:panelGrid columns="1">

    <h:outputText value="#{msg['role_list']}" styleClass="standard_bold" />
    <t:dataList id="role-list"
        styleClass="standardList"
        var="role"
        value="#{dataProvider.currentUser.roles}"
        layout="orderedList" forceId="true">
        <h:outputText value="#{role.name}" />
    </t:dataList>

</h:panelGrid>

