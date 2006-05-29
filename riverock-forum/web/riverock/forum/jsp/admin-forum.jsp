<%@page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />
<fmt:setBundle basename="org.riverock.forum.i18n.ForumResources"/>

<request:isUserInRole role="wm.forum-admin, webmill.root">


<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
<c:forEach var="category" items="${forumBean.forumCategories}">
<tr>
    <td colspan="5" class="forum-td2">
        <table border="0">
           <tr>
               <td nowrap= >
	   	<form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
           <input type="hidden" name="action" value="admin-forum"/>
           <input type="hidden" name="sub-action" value="update-forum-category"/>
           <input type="hidden" name="forumCategoryId" value="<c:out value='${category.forumCategoryId}'/>"/>
           <input type="text" name="forum-category-name" value="<c:out value='${category.categoryName}'/>" size="50"> &nbsp;
           <input type="submit" value="Update"/>
        </form>
               </td>
               <td>
           <c:choose>
               <c:when test="${category.deleted}">

	   	   	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	   	   	   	   <input type="hidden" name="action" value="admin-forum"/>
	   	   	   	   <input type="hidden" name="sub-action" value="permanent-delete-forum-category"/>
                   <input type="hidden" name="forumCategoryId" value="<c:out value='${category.forumCategoryId}'/>"/>
	   	   	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                       Category virtually deleted
                       <input type="checkbox" value="1" name="confirm-delete"/><input type="submit" value="Permanent delete category"/>
                   </td>
                   </form>
	   	   	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	   	   	   	   <input type="hidden" name="action" value="admin-forum"/>
	   	   	   	   <input type="hidden" name="sub-action" value="restore-forum-category"/>
                   <input type="hidden" name="forumCategoryId" value="<c:out value='${category.forumCategoryId}'/>"/>
	   	   	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                       <input type="submit" value="Restore category"/>
                   </td>
                   </form>
               </c:when>
               <c:otherwise>
	      	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	      	   	   <input type="hidden" name="action" value="admin-forum"/>
	      	   	   <input type="hidden" name="sub-action" value="delete-forum-category"/>
                   <input type="hidden" name="forumCategoryId" value="<c:out value='${category.forumCategoryId}'/>"/>
	      	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                   <input type="checkbox" value="1" name="confirm-delete"/><input type="submit" value="Delete category"/>
                   </td>
               </form>
               </c:otherwise>
           </c:choose>
               </td>
           </tr>
        </table>
    </td>
</tr>
<tr class="forum-th" align="center">
	<td nowrap  class="forum-th">::</td>
	<td width="80%" class="forum-th">Forum</td>
	<td nowrap class="forum-th">Topics</td>
	<td nowrap class="forum-th">Messsages</td>
	<td nowrap class="forum-th">Action</td>
</tr>
<c:forEach var="forum" items="${category.forums}">
<tr>
	<td class="forum-td">:.</td>
	<td class="forum-td2">
	   	<form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
           <input type="hidden" name="action" value="admin-forum"/>
           <input type="hidden" name="sub-action" value="update-forum-concrete"/>
           <input type="hidden" name="forumConcreteId" value="<c:out value='${forum.f_id}'/>"/>
           <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
           <table border="0">
              <tr>
                  <td>
                  <input type="text" name="forum-name" value="<c:out value='${forum.f_name}'/>" size="50"><br />
                  <input type="text" name="forum-desc" value="<c:out value='${forum.f_info}'/>" size="50">
                  </td>
                  <td>
                  <input type="submit" value="Update"/>
                  </td>
              </tr>
           </table>
        </form>

	</td>
	<td class="forum-td"><c:out value='${forum.f_topics}'/></td>
	<td class="forum-td2"><c:out value='${forum.f_messages}'/></td>
	<td nowrap class="forum-td" align="right">
        <table border="0">
           <tr>
<%--               <td width="30%">Delete forum:</td>--%>
               <td>&nbsp;&nbsp;&nbsp;</td>
               <td>
                   <table border="0">
                   <tr>
           <c:choose>
               <c:when test="${forum.deleted}">

	   	   	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	   	   	   	   <input type="hidden" name="action" value="admin-forum"/>
	   	   	   	   <input type="hidden" name="sub-action" value="permanent-delete-forum"/>
	   	   	   	   <input type="hidden" name="forumConcreteId" value="<c:out value='${forum.f_id}'/>"/>
	   	   	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                       Forum virtually deleted
                       <input type="checkbox" value="1" name="confirm-delete"/><input type="submit" value="Permanent delete forum"/>
                   </td>
                   </form>
	   	   	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	   	   	   	   <input type="hidden" name="action" value="admin-forum"/>
	   	   	   	   <input type="hidden" name="sub-action" value="restore-forum"/>
	   	   	   	   <input type="hidden" name="forumConcreteId" value="<c:out value='${forum.f_id}'/>"/>
	   	   	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                       <input type="submit" value="Restore forum"/>
                   </td>
                   </form>
               </c:when>
               <c:otherwise>
	      	   	   <form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	      	   	   <input type="hidden" name="action" value="admin-forum"/>
	      	   	   <input type="hidden" name="sub-action" value="delete-forum"/>
	      	   	   <input type="hidden" name="forumConcreteId" value="<c:out value='${forum.f_id}'/>"/>
	      	   	   <input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
                   <td width="70%">
                   <input type="checkbox" value="1" name="confirm-delete"/><input type="submit" value="Delete forum"/>
                   </td>
               </form>
               </c:otherwise>
           </c:choose>
                   </tr>
                   </table>
               </td>
           </tr>
        </table>
	</td>
</tr>

</c:forEach>
<tr>
	<td class="forum-td">:.</td>
	<form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	<td class="forum-td" colspan="4">
	<input type="hidden" name="action" value="admin-forum"/>
	<input type="hidden" name="sub-action" value="add-new-forum"/>
	<input type="hidden" name="forumCategoryId" value="<c:out value='${category.forumCategoryId}'/>"/>
	<input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
    <table border="0">
    <tr><td width="30%">Name of new forum: </td><td>&nbsp;&nbsp;&nbsp;</td><td width="70%"><input name="forum-name" type="text" value=""/></td></tr>
    <tr><td>Forum info:</td><td></td><td><input name="forum-info" type="text" value=""/></td></tr>
    <tr><td></td><td></td><td><input type="submit" value="Add new forum"></td></tr>
    </table>
	</td>
    </form>
</tr>
</c:forEach>

<tr>
	<td class="forum-td" colspan="5">
	<form name="FormPost" method="post" action="<c:out value='${genericBean.forumHomeUrl}'/>">
	<input type="hidden" name="action" value="admin-forum"/>
	<input type="hidden" name="sub-action" value="add-new-category"/>
	<input type="hidden" name="forumId" value="<c:out value='${forumBean.forumId}'/>"/>
    New forum category
	<input name="forum-category-name" type="text" value=""/>
    <input type="submit" value="Add new forum category">
    </form>
	</td>
</tr>
</table>


</request:isUserInRole>

<%@include file="inc/footer.jsp"%>