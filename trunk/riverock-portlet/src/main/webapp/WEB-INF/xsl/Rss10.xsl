<?xml version="1.0"?>

<!--
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
  -->
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:rss="http://purl.org/rss/1.0/"
    xmlns:content="http://purl.org/rss/1.0/modules/content/"
    xmlns:annotate="http://purl.org/rss/1.0/modules/annotate/"
    xmlns:admin="http://webns.net/mvcb/"
    xmlns:image="http://purl.org/rss/1.0/modules/image/"
    xmlns:cc="http://web.resource.org/cc/"
    xmlns:reqv="http://purl.org/rss/1.0/modules/richequiv/"
    exclude-result-prefixes="rdf dc dcterms rss content annotate admin image cc reqv"                   
    version="1.0">

    <xsl:output method="html"/>

    <!-- Setup -->
    <xsl:template match="/">
        <TABLE WIDTH="100%" BORDER="0" CELLPADDING="2" CELLSPACING="0">
            <xsl:apply-templates/>
        </TABLE>
    </xsl:template>

    <!-- Items -->
    <xsl:template match="item">
        <TR><TD>
            - <A TARGET="_popup" STYLE="text-decoration: none;">
	            <xsl:attribute name="HREF">
	                <xsl:value-of select="link"/>
	            </xsl:attribute>
	            <xsl:value-of select="title"/>
            </A>
        </TD></TR>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="text()"/>      
</xsl:stylesheet>