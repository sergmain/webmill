<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="session" uri="/tld/jakarta-session" %>


<table width="758" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#999999">
  <tr>
	<td bgcolor="#e2e3e4">
		<TABLE width="95%" border="0" align="center" height="80">
			<TR>
				<TD>
<%--                          <a href="<c:out value='${genericBean.forumHomeUrl}'/>">--%>
<%--                             <IMG SRC="/riverock/forum/img/lb_logo.gif" WIDTH="253" HEIGHT="32" BORDER="0" ALT="">--%>
<%--                          </a>--%>
                </TD>
				<TD align="right">
                     <c:choose>
                         <c:when test="${empty principal}">
                            <c:if test="${!empty genericBean.loginUrl}">
                            <A HREF="<c:out value='${genericBean.loginUrl}'/>">Login</A>&nbsp;|&nbsp;
                            </c:if>
                            <c:if test="${!empty genericBean.registerUrl}">
                            <A HREF="<c:out value='${genericBean.registerUrl}'/>">Register</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:when>
                         <c:otherwise>
                            <c:if test="${!empty genericBean.logoutUrl}">
                               <A HREF="<c:out value='${genericBean.logoutUrl}'/>">Logout</A>&nbsp;|&nbsp;
                            </c:if>
                         </c:otherwise>
                     </c:choose>

                     <c:if test="${!empty genericBean.membersUrl}">
                     <A HREF="<c:out value='${genericBean.membersUrl}'/>">Members</A>&nbsp;|&nbsp;
                     </c:if>

                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>&action=help">Help</A>&nbsp;|&nbsp;
                     <A HREF="<c:out value='${genericBean.forumHomeUrl}'/>">Home</A>
				</TD>
			</TR>
		</TABLE>


