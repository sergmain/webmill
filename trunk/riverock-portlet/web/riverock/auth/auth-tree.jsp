<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>


        <h:commandButton id="add-user-action" action="#{authUserAction.addUserAction}"
		value="#{msg['add_user_action']}" 
		rendered="#{!userSessionBean.add}"
	>
        </h:commandButton>

    <t:tree2 id="serverTree" value="#{userTree.userTree}" var="node" varNodeToggler="t" clientSideToggle="false" showRootNode="false">
        <f:facet name="company">
            <h:panelGroup>
                <t:graphicImage value="/images/company-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/company-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="foo-folder">
            <h:panelGroup>
                <t:graphicImage value="/images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
                <t:graphicImage value="/images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
                <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
                <h:outputText value=" (#{node.childCount})" styleClass="childCount" rendered="#{!empty node.children}"/>
            </h:panelGroup>
        </f:facet>
        <f:facet name="user">
            <h:panelGroup>
                <h:commandLink id="select-user-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}" 
			actionListener="#{authUserAction.selectUserAction}">

                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>

                    <t:updateActionListener property="#{userSessionBean.currentAuthUserId}" value="#{node.identifier}" />
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
    </t:tree2>




