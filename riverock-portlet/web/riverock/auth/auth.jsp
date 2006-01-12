<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>


<h:form id="foo">

    <t:panelTabbedPane bgcolor="#FFFFCC" >

        <t:panelTab id="tab1" label="#{msg['tabbed_tab1']}">
    
<f:verbatim><table borser="0" width="100%"><tr><td width="300" valign="top"></f:verbatim>


        <h:commandButton id="add-user-action" action="#{treeBacker.addUserAction}" 
		value="#{msg['add_user_action']}" 
		rendered="#{!treeBacker.currentUser.add}"
	>
        </h:commandButton>

    <t:tree2 id="serverTree" value="#{treeBacker.userTree}" var="node" varNodeToggler="t" clientSideToggle="false" showRootNode="false">
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
                <h:commandLink id="select-user-action" styleClass="#{t.nodeSelected ? 'documentSelected':''}" actionListener="#{treeBacker.selectUserAction}">
                    <t:graphicImage value="/images/user.png" border="0"/>
                    <h:outputText value="#{node.description}"/>
                    <f:param name="authUserId" value="#{node.identifier}"/>
                </h:commandLink>
            </h:panelGroup>
        </f:facet>
    </t:tree2>

<f:verbatim></td><td valign="top"></f:verbatim>


<h:panelGroup rendered="#{!empty treeBacker.currentUser}">


        <f:subview id="select-user-subview" rendered="#{!treeBacker.currentUser.edit && !treeBacker.currentUser.delete && !treeBacker.currentUser.add}">
            <jsp:include page="auth-user-select.jsp"/>
        </f:subview>

        <f:subview id="add-user-subview" rendered="#{treeBacker.currentUser.add}">
            <jsp:include page="auth-user-add.jsp"/>
        </f:subview>

        <f:subview id="edit-user-subview" rendered="#{treeBacker.currentUser.edit}">
            <jsp:include page="auth-user-edit.jsp"/>
        </f:subview>

        <f:subview id="delete-user-subview" rendered="#{treeBacker.currentUser.delete}">
            <jsp:include page="auth-user-delete.jsp"/>
        </f:subview>

</h:panelGroup>


<f:verbatim></td></tr></table></f:verbatim>


        </t:panelTab>

        <t:panelTab id="tab2" label="#{msg['tabbed_tab2']}">
        </t:panelTab>

    </t:panelTabbedPane>
</h:form>


</f:view>

