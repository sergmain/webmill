<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" omit-xml-declaration="no"/>
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="node()"/>
	</xsl:template>
	
	<xsl:template match="Separator">
		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;
		</xsl:text>
	</xsl:template>

	<xsl:template match="SeparatorDynStart">
		<xsl:text disable-output-escaping="yes">&lt;table border="0"&gt;&lt;tr&gt;&lt;td valign="top"&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="SeparatorDynImage">
		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template match="SeparatorDynEnd">
		<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;/td&gt;&lt;/table&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="Footer">
		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;</xsl:text>
		<div align="center" class="copyright">
			<a href="http://me.askmore.info/">Powered by Mill Engine</a>
		</div>
		<xsl:text disable-output-escaping="yes">&lt;/body&gt;&lt;/html&gt;
</xsl:text>
	</xsl:template>
	<xsl:template match="HeaderStart">
		<xsl:text disable-output-escaping="yes">&lt;html></xsl:text>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<meta http-equiv="pragma" content="no-cache"/>
			<META http-equiv="cache-control" content="no-cache"/>
			<link rel="stylesheet" href="/css"/>
		</head>
		<xsl:text disable-output-escaping="yes">&lt;body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#FFFFFF" text="#0"></xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;div class="path"></xsl:text>
	</xsl:template>
	<xsl:template match="HeaderTextIndex">
		<xsl:text disable-output-escaping="yes">&amp;</xsl:text>nbsp;
</xsl:template>
	<xsl:template match="HeaderText">
		<xsl:text disable-output-escaping="yes">&amp;</xsl:text>nbsp;
</xsl:template>

	<xsl:template match="HeaderEnd">
		<xsl:text disable-output-escaping="yes">&lt;/div></xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;table cellspacing="10" cellpadding="0" border="0"></xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;tr>&lt;td width="140" valign="top"></xsl:text>
	</xsl:template>

	<xsl:template match="Article">
		<div class="frame">
		<div class="label">
		<xsl:value-of select="ArticleName"/>
		</div>
		<div class="content">
		<xsl:apply-templates select="ArticleText"/>
		</div>
		</div>
	</xsl:template>

	<xsl:template match="ArticleText">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="ArticlePara">
		<p>
			<xsl:value-of select="."/>
		</p>
	</xsl:template>

<xsl:template match="ArticleImage">
	<img>
	<xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
	<xsl:attribute name="border"><xsl:value-of select="@border"/></xsl:attribute>
	<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
	<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
	<xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute>
	</img>
</xsl:template>

<xsl:template match="NewsBlock">
<xsl:for-each select="NewsGroup">
<div class="frame">
<div class="label">
<xsl:value-of select="NewsGroupName"/>
</div>
<div class="content">
<xsl:apply-templates select="NewsItem"/>
</div>
</div>
</xsl:for-each>
</xsl:template>

	<xsl:template match="FaqBlock">
		<xsl:for-each select="FaqGroup">
			<div class="frame">
				<div class="label">
					<xsl:value-of select="FaqGroupName"/>
				</div>
				<div class="content">
					<xsl:apply-templates/>
				</div>
			</div>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="FaqGroupName"/>
	<xsl:template match="FaqItem">
		<div class="by">
			<xsl:value-of select="FaqItemDate"/>
		</div>
		<div>Q: <xsl:value-of select="FaqItemQuestion"/>
		</div>
		<div>A: <xsl:value-of select="FaqItemAnswer"/>
		</div>
	</xsl:template>
	
	<xsl:template match="NewsGroupName"/>
	<xsl:template match="NewsItem">
		<div class="news">
			<div class="by">
				<xsl:value-of select="NewsDate"/>
			</div>
			<div class="title">
				<xsl:value-of select="NewsHeader"/>
			</div>
			<div class="text">
<xsl:value-of select="NewsAnons"/>
<xsl:call-template name="nbsp-ref"/>
<xsl:call-template name="nbsp-ref"/>
<xsl:call-template name="nbsp-ref"/>
<a href="{UrlToFullNewsItem}&amp;mill.xmlroot=XmlNewsItem" class="newsNext">
					<xsl:value-of select="ToFullItem"/>
				</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="NewsItemSimple">
		<div class="frame">
			<div class="label">
				<xsl:value-of select="NewsHeader"/>
			</div>
			<div class="news">
				<div class="by">
					<xsl:value-of select="NewsDate"/>
				</div>
				<div class="text">
					<xsl:value-of select="NewsAnons"/>
				</div>
				<div class="text">
<xsl:apply-templates/>
<!--
<xsl:value-of select="NewsText"/>
-->
				</div>
			</div>
		</div>
	</xsl:template>

<xsl:template match="XmlNewsItem">
	<div class="frame">
	<div class="label">
	<xsl:value-of select="NewsHeader"/>
	</div>
	<div class="news">
	<div class="by">
	<xsl:value-of select="NewsDate"/>
	</div>
	<div class="text">
	<xsl:value-of select="NewsAnons"/>
	</div>
	<div class="text">
	<xsl:for-each select="NewsText">
	<xsl:apply-templates/>
	</xsl:for-each>
	</div>
	</div>
	</div>
</xsl:template>

<xsl:template match="PreText">
<pre>
	<xsl:value-of select="."/>
</pre>
</xsl:template>

<xsl:template match="Link">
<a>
<xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
	<xsl:attribute name="border"><xsl:value-of select="@border"/></xsl:attribute>
<xsl:value-of select="."/>
</a>
</xsl:template>

<xsl:template match="NewsItemPara">
	<div class="frame">
	<div class="label">
	<xsl:value-of select="NewsHeader"/>
	</div>
	<div class="news">
	<div class="by">
	<xsl:value-of select="NewsDate"/>
	</div>
	<div class="text">
	<xsl:value-of select="NewsAnons"/>
	</div>
	<div class="text">

	<xsl:apply-templates select="NewsText"/>
	</div>
	</div>
	</div>
</xsl:template>

	<xsl:template match="NewsText">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="NewsTextPara">
		<p>
			<xsl:value-of select="."/>
		</p>
	</xsl:template>
	
	<xsl:template match="MenuMember">
		<xsl:for-each select="MenuMemberApplication">
			<div class="frame">
				<div class="label">
					<xsl:value-of select="ApplicationName"/>
				</div>
				<table border="0">
					<xsl:apply-templates select="MenuMemberModule"/>
				</table>
			</div>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="MenuMemberModule">
		<tr>
			<td height="15">
				<a href="{ModuleUrl}" class="menuMain">
					<xsl:value-of select="ModuleName"/>
				</a>
			</td>
		</tr>
		<xsl:apply-templates select="MenuMemberModule"/>
	</xsl:template>

<xsl:template match="TopMenu">
<div class="frame">
<div class="label">
<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
</div>
<table border="0">
<tr>
<xsl:for-each select="MenuModule">
<td height="15" align="center">

	<a href="{ModuleUrl}" class="menuMain">
	<xsl:value-of select="ModuleName"/>
	</a>

</td>
</xsl:for-each>
</tr>
</table>
</div>
</xsl:template>

	<xsl:template match="MenuSimple">
		<div class="frame">
			<div class="label">
				<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
			</div>
			<table border="0">
				<xsl:apply-templates select="MenuModule"/>
			</table>
		</div>
	</xsl:template>

<xsl:template match="MenuModule">
	<tr>
      <xsl:choose>
        <xsl:when test="@includeLevel=1"/>
        <xsl:when test="@includeLevel=2">
<td width="5">
		<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
</td>
        </xsl:when>
        <xsl:otherwise>
<td width="5">
		<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
</td>
<td width="5">
		<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
</td>
        </xsl:otherwise>
      </xsl:choose>
<td height="20">
      <xsl:choose>
        <xsl:when test="@includeLevel=1">
<xsl:attribute name="colspan">3</xsl:attribute>
        </xsl:when>
        <xsl:when test="@includeLevel=2">
<xsl:attribute name="colspan">2</xsl:attribute>
        </xsl:when>
        <xsl:otherwise/>
      </xsl:choose>
  <a href="{ModuleUrl}" class="menuMain">
  <xsl:value-of select="ModuleName"/>
  </a>
</td>
		</tr>
<xsl:apply-templates select="MenuModule"/>
</xsl:template>

	<xsl:template name="nbsp-ref">
		<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
	</xsl:template>



<xsl:template match="LoginOnIndexPage">
<div class="frame">
<div class="label">Login</div>
    <xsl:choose>
        <xsl:when test="@isLogged=1">
            <table border="0" class="menuMain">
                <tr>
                    <td>
        Welcome, <xsl:value-of select="UserName"/>
                    </td>
                </tr>
            </table>
        </xsl:when>
        <xsl:otherwise>
            <form method="POST" action="{ActionUrl}">
                <input type="hidden" name="mill.context" value="{PortletName}"/>
                <input type="hidden" name="mill.tourl" value="{ToUrl}"/>
  <table cellpadding="0" cellspacing="0" border="0">
  <tr>
  <td valign="top">
  <input type="text" name="mill.username" tabindex="1" size="6"/>
  <span style="color:black;font-size:8pt;"><xsl:value-of select="LoginMessage"/></span>
  </td>
  </tr>
  <tr>
  <td valign="top">
  <input type="password" name="mill.password" value="" tabindex="2" size="6"/>
  <span style="color:black;font-size:8pt"><xsl:value-of select="PasswordMessage"/></span>
  </td>
  </tr>
  <tr>
  <td align="center">
  <input type="submit" name="button" value="{ButtonMessage}" tabindex="3"/>
  </td>
  </tr>
  </table>
            </form>
        </xsl:otherwise>
    </xsl:choose>
</div>
</xsl:template>

<xsl:template match="LoginXml">
    <xsl:choose>
        <xsl:when test="@isLogged=1">
            <table border="0">
                <tr>
                    <td>
                        <xsl:value-of select="InviteMessage"/>
                    </td>
                </tr>
                <tr>
                    <td>
        Username: <xsl:value-of select="UserName"/>
                    </td>
                </tr>
            </table>
        </xsl:when>
        <xsl:otherwise>
            <form method="POST" action="{ActionUrl}">
                <input type="hidden" name="mill.context" value="{PortletName}"/>
                <input type="hidden" name="mill.tourl" value="{ToUrl}"/>
                <table border="0" cellspacing="0" cellpadding="2" align="center">
                    <tr>
                        <th>
                            <xsl:value-of select="InviteMessage"/>
                        </th>
                    </tr>
                    <tr>
                        <td>
                            <table border="0">
                                <tr>
                                    <td align="right">
                                        <xsl:value-of select="LoginMessage"/>
                                    </td>
                                    <td/>
                                    <td>
                                        <input type="text" name="mill.username" tabindex="1"/>
                                    </td>
                                    <td align="left" valing="top" cellspan="2">
                                        <input type="submit" name="button" value="{ButtonMessage}" tabindex="3"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="right">
                                        <xsl:value-of select="PasswordMessage"/>
                                    </td>
                                    <td/>
                                    <td aling="left">
                                        <input type="password" name="mill.password" value="" tabindex="2"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>


</xsl:stylesheet>

