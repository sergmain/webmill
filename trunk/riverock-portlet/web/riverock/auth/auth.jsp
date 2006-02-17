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


        <f:subview id="auth-tree-subview" rendered="#{!userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
            <jsp:include page="auth-tree.jsp"/>
        </f:subview>


<f:verbatim></td><td valign="top"></f:verbatim>


<h:panelGroup rendered="#{!empty dataProvider.currentUser}">


        <f:subview id="select-user-subview" rendered="#{!userSessionBean.edit && !userSessionBean.delete && !userSessionBean.add}">
            <jsp:include page="auth-user-select.jsp"/>
        </f:subview>

</h:panelGroup>


<f:verbatim></td></tr></table></f:verbatim>


        </t:panelTab>

        <t:panelTab id="tab2" label="#{msg['tabbed_tab2']}">
        <f:subview id="role-subview">
        </f:subview>
        </t:panelTab>

    </t:panelTabbedPane>
</h:form>


</f:view>

