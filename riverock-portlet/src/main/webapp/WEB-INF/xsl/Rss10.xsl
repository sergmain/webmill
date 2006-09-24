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
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
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