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
<%@ page session="false" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>


<f:loadBundle basename="org.riverock.portlet.cms.resource.News" var="msg"/>

<h:panelGroup rendered="#{isUserInRole['webmill.portal-manager,webmill.cms-manager,webmill.news-manager']}">
    <h:panelGrid columns="1">

        <h:outputText value="#{msg.news_header}"/>
        <h:outputText value="#{newsSessionBean.news.newsHeader}"/>

        <h:outputText value="#{msg.news_anons}"/>
        <h:inputTextarea 
            value="#{newsSessionBean.news.newsAnons}" style="height: 150px; width: 100%;font-size:8pt; border:1px solid;margin:0;padding:5px;"
            readonly="true" />

        <h:outputText value="#{msg.news_text}"/>
        <h:inputTextarea
            value="#{newsSessionBean.news.newsText}" style="height: 300px; width: 100%;font-size:8pt; border:1px solid;margin:0;padding:5px;"
            readonly="true" />

    </h:panelGrid>
</h:panelGroup>

