<?xml version="1.0" ?>
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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
<xsl:template match="/rss/channel">
<p>
<a><xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
<xsl:value-of select="title"/></a>
</p>
<p>
<xsl:value-of select="lastBuildDate"/>
</p>
<dl>
<xsl:for-each select="item">
    <dt>
        <a><xsl:attribute name="href"><xsl:value-of select="link"/></xsl:attribute>
        <xsl:value-of select="title"/></a>
    </dt>
    <dd>
        <xsl:value-of select="description"/> (<xsl:value-of select="pubDate"/>)
    </dd>
</xsl:for-each>
</dl>
<p>
<xsl:value-of select="copyright"/>
</p>
</xsl:template>
</xsl:stylesheet>