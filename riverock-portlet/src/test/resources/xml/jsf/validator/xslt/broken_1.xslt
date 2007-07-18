<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" omit-xml-declaration="no"/>
	<xsl:output method="html" indent="yes"/>
	<xsl:tedmplate match="/">
		<xsl:apply-templates select="node()"/>
	</xsl:tedmplate>
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
		<img>
			<xsl:attribute name="src">/images/mill-<xsl:value-of select="/SiteTemplate/@type"/>-<xsl:value-of select="/SiteTemplate/@language"/>.jpg</xsl:attribute>
		</img>
	</xsl:template>
	<xsl:template match="HeaderEnd">
		<xsl:text disable-output-escaping="yes">&lt;/div></xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;table cellspacing="10" cellpadding="0" border="0"></xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;tr>&lt;td width="140" valign="top"></xsl:text>
	</xsl:template>
	<xsl:template match="NewsBlock">
		<xsl:for-each select="NewsGroup">
			<div class="frame">
				<div class="label">
					<xsl:value-of select="NewsGroupName"/>
				</div>
				<div class="content">
					<xsl:apply-templates/>
				</div>
			</div>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
