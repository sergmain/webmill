<%--
  ~ org.riverock.portlet - Portlet Library
  ~
  ~ Copyright (C) 2006, Riverock Software, All Rights Reserved.
  ~
  ~ Riverock - The Open-source Java Development Community
  ~ http://www.riverock.org
  ~
  ~
  ~ This program is free software; you can redistribute it and/or
  ~ modify it under the terms of the GNU General Public
  ~ License as published by the Free Software Foundation; either
  ~ version 2 of the License, or (at your option) any later version.
  ~
  ~ This library is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public
  ~ License along with this library; if not, write to the Free Software
  ~ Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
  --%>
<%--
  User: SergeMaslyukov
  Date: 10.09.2006
  Time: 19:17:21
  $Id$
--%>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<f:loadBundle basename="org.riverock.portlet.resource.Webclip" var="msg"/>

<style type="text/css">
    TD {
        vertical-align: top;
    }

    .top-button-action {
        width: 120px;
        height: 20px;
    }

    .weblip-button-action {
        width: 150px;
        height: 22px;
    }

    .site-sub-button-action {
        width: 80px;
        height: 22px;
    }
</style>

<f:view>
    <h:form rendered="#{isUserInRole['webmill.webclip-manager']}">

        <h:panelGrid columns="1">
            <h:panelGroup>
                <h:commandButton id="save_webclip-action" action="#{webclipDataProvider.saveWebclipData}"
                                 value="#{msg.save_webclip_action}"
                                 styleClass="weblip-button-action"
                    />

                <h:commandButton id="article-add-cancel-action" action="#{webclipDataProvider.refreshWebclipData}"
                                 value="#{msg.refresh_webclip_data_action}"
                                 styleClass="weblip-button-action"
                    />
            </h:panelGroup>

            <h:outputText value="#{msg.webclip_url}"/>
            <h:inputText id="url-name-field" value="#{webclipDataProvider.url}" size="50"/>
        </h:panelGrid>

    </h:form>

    <h:outputText value="#{webclipDataProvider.webclip.webclipData}" />


</f:view>
