<%@page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="jstl/core" %>
<%@ taglib prefix="fmt" uri="jstl/format" %>
<%@ taglib prefix="request" uri="/tld/jakarta-request" %>

<%@include file="inc/header.jsp" %>

<c:set var="locale"><request:attribute name="request-locale"/></c:set>
<fmt:setLocale value="${locale}" scope="request" />

<!--bar-->
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" >
	<tr>
		<td>
			<B><IMG SRC="<%= application.getRealPath("/") %>/riverock/forum/img/lb_icon.gif" WIDTH="16" HEIGHT="16" BORDER=0 valign="middle">&nbsp;<a href="<c:out value='${genericBean.forumHomeUrl}'/>"><c:out value="${genericBean.forumName}"/></A>&nbsp;&gt;&nbsp;Help</B>
		</td>
	</tr>
</table>

<BR />

<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5" class="forum-th">
	<tr class="forum-th">
		<td align="center" class="forum-th">Help</td>
	</tr>
	<tr class="forum-td">
		<td >


<TABLE width="600" align="center">
<TR>
	<TD>

	<p>
	<a href="#2">1. Do I have to register?</a><BR>
	<a href="#2">2. What are Moderators?</a><BR>
	<a href="#3">3. Are cookies used?</a><BR>
	<a href="#4">4. Can I edit my own posts?</a><BR>
	<a href="#5">5. Can I attach files?</a><BR>
	<a href="#6">6. Can I search?</a><BR>
	<a href="#7">7. Can I edit my profile?</a><BR>
	<a href="#8">8. Can I attach my own signature to my posts?</a><BR>
	<a href="#9">9. What do I do if I forget my UserName and/or Password?</a>

	</p>	

<a name="1" ></a>
<p>
1. Registering<BR>
Registration is not required to view current topics on the Forum; however, 
if you wish to post a new topic or reply to an existing topic registration is 
required.&nbsp; Registration is free and only takes a few minutes.
</p>
<a name="2" ></a>
<p>
2. Moderators<BR>
Moderators control individual forums. They may edit, delete, or prune any posts in their forums. If you have a question about a particular forum, you should direct it to your forum moderator. 
</p>
<a name="3" ></a>
<p>
3. Cookies<BR>
These Forums use cookies to store the following information: your UserName and your Password, if you set it in preferences. These cookies are stored on your hard drive. Cookies are not used to track your movement or perform any function other than to enhance your use of these forums. If you have not enabled cookies in your browser, many of these time-saving features will not work properly. Also, you need to have cookies enabled if you want to enter a private forum or post a topic/reply. You may delete all cookies set by these forums in selecting the "logout" button at the top of any page. 
</p>
<a name="4" ></a>
<p>
4. Editing Your Posts<BR>
You may edit or delete your own posts at any time. Just go to the topic where the post to be edited or deleted is located and you will see a edit or delete icon () on the line that begins "posted on..." Click on this icon to edit or delete the post. No one else can edit your post, except for the forum Moderator or the forum administrator. A note is generated at the bottom of each edited post displaying when and by whom the post was edited. 
</p>
<a name="5" ></a>
<p>
5. Attaching Files<BR>
You may attach files to any posts. But only one file can be uploaded in each post and the file's max  size is limited. 
</p>
<a name="6" ></a>
<p>
6. Searching For Specific Posts<BR>
You may search for specific posts based on a word or words found in the posts, user name, date, and particular forum(s). Simply click on the "search" link at the top of most pages. 
</p>
<a name="7" ></a>
<p>
7. Editing Your Profile<BR>
You may easily change any information stored in your registration profile by using the "Edit my profile" link.  You may edit any information (except your UserName).
</p>
<a name="8" ></a>
<p>
8. Signatures<BR>
You may attach signatures to the end of your posts when you post either a New Topic or Reply. Your signature is editable after login.
</p>
<a name="9" ></a>
<p>
9. Lost User Name and/or Password<BR>
Retrieving your UserName and Password is simple, assuming that email features are turned on for this forum. All of the pages that require you to identify yourself with your UserName and Password carry a "lost Password" link that you can use to have your UserName and Password mailed instantly to your email address of record. 
</p>
<BR>
</TD>
</TR>
</TABLE>

		</td>
	</tr>			
</table>


<%@include file="inc/footer.jsp"%>
