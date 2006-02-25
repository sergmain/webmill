<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.manager.resource.Holding" var="msg"/>	

    <h:panelGrid columns="2">
	<h:outputText value="Holding short name:"/>
	<h:outputText value="#{holdingDataProvider.currentHolding.shortName}" />

	<h:outputText value="Holding name:"/>
	<h:outputText value="#{holdingDataProvider.currentHolding.name}" />

    </h:panelGrid>

    <h:panelGrid columns="1">
        <h:outputText value="#{msg.company_list}" styleClass="standard_bold" />
	<t:dataList id="company-list"         
        	styleClass="standardList"
        	var="company"
        	value="#{holdingDataProvider.currentHolding.companies}"
        	layout="orderedList" forceId="true">
        	<h:outputText value="#{company.name}" />
    	</t:dataList>
    </h:panelGrid>

