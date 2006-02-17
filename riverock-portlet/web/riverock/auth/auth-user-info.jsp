<%@ page session="false" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

    <f:loadBundle basename="org.riverock.portlet.auth.resource.Auth" var="msg"/>

<f:verbatim><table borser="0" width="100%" valign="top"><tr><td width="250"></f:verbatim>
	<h:outputText value="User name:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<h:outputText value="#{dataProvider.currentUser.userName}" />
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="User login:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<h:outputText value="#{dataProvider.currentUser.authInfo.userLogin}" />
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Company:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<t:graphicImage value="/images/yes.gif" rendered="#{dataProvider.currentUser.authInfo.company}" border="0"/>
	<t:graphicImage value="/images/no.gif" rendered="#{!dataProvider.currentUser.authInfo.company}" border="0"/>
	<h:outputText value="#{dataProvider.currentUser.companyName}" />
<f:verbatim></td></tr><tr><td valign="top"></f:verbatim>
	<h:outputText value="Holding:"/>
<f:verbatim></td><td valign="top"></f:verbatim>
	<t:graphicImage value="/images/yes.gif" rendered="#{dataProvider.currentUser.authInfo.holding}" border="0"/>
	<t:graphicImage value="/images/no.gif" rendered="#{!dataProvider.currentUser.authInfo.holding}" border="0"/>
	<h:outputText value="#{dataProvider.currentUser.holdingName}" />


<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>

    <h:outputText value="#{msg['role_list']}" styleClass="standard_bold" />
    <t:dataList id="role-list"
        styleClass="standardList"
        var="role"
        value="#{dataProvider.currentUser.roles}"
        layout="orderedList" forceId="true">
        <h:outputText value="#{role.name}" />
    </t:dataList>

<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>
<f:verbatim></td></tr><tr><td valign="top" colspan="2" align="left"></f:verbatim>
<f:verbatim></td></tr></table></f:verbatim>

