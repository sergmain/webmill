<?xml version="1.0" encoding="windows-1251"?>

<!--

/*

 * org.riverock.webmill - Portal framework implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock - The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public

 * License as published by the Free Software Foundation; either

 * version 2 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * General Public License for more details.

 *

 * You should have received a copy of the GNU General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */

-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html" omit-xml-declaration="no"/>

	<xsl:output method="html" indent="no"/>

	<xsl:template match="/">

		<xsl:apply-templates select="node()"/>

	</xsl:template>

	<xsl:template match="Separator">

		<xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;

		</xsl:text>

	</xsl:template>

	<xsl:template match="TextBlock1">

		<xsl:text disable-output-escaping="yes">&lt;/td>&lt;td valign="top"></xsl:text>

		<div class="frame">

			<div class="label">Внимание!!!</div>

			<div>

				<table border="0">

					<tr>

						<td>

							<b>Ремонт жестки дисков только у нас</b>

							<p>Мы оказываем услуги по ремонту жестких дисков всех известных марок</p>

						</td>

					</tr>

				</table>

			</div>

		</div>

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

		<xsl:text disable-output-escaping="yes">&lt;html>&lt;!-- zeon XSLT --></xsl:text>

		<head>

			<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

			<meta http-equiv="pragma" content="no-cache"/>

			<META http-equiv="cache-control" content="no-cache"/>

			<link rel="stylesheet" href="/share/css.jsp"/>

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

</xsl:template>



<xsl:template match="MainStart">

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



<xsl:template match="Article">

<div align="center">

<font size="6" face="Arial">

<xsl:value-of select="ArticleName"/>

</font></div>

<xsl:apply-templates select="ArticleText"/>

</xsl:template>



<xsl:template match="ArticleText">

<xsl:apply-templates/>

</xsl:template>



<xsl:template match="ArticlePara">

<p><xsl:value-of select="."/></p>

</xsl:template>





<xsl:template match="ArticleImage">

				<img>

					<xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>

					<xsl:attribute name="border"><xsl:value-of select="@border"/></xsl:attribute>

					<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>

					<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>

<xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute>

<xsl:attribute name="title"><xsl:value-of select="@title"/></xsl:attribute>

</img>

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

				<a href="{UrlToFullNewsItem}&amp;mill.template=me.askmore-dynamic" class="newsNext">

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

					<xsl:value-of select="NewsText"/>

				</div>

			</div>

		</div>

	</xsl:template>

	<xsl:template match="JobBlock">

		<div class="frame">

			<div class="label">Открытые вакансии</div>

			<table width="100%" border="0" cellspacing="0" cellpadding="0">

				<tr>

					<th class="jobHead">дата</th>

					<td width="10">

						<xsl:call-template name="nbsp-ref"/>

					</td>

					<th width="80%" class="jobHead">вакансия</th>

				</tr>

				<xsl:apply-templates select="JobItem"/>

			</table>

		</div>

	</xsl:template>

	<xsl:template match="JobItem">

		<tr>

			<td align="right" valign="top">

				<xsl:apply-templates select="@datePost"/>

			</td>

			<td>

				<xsl:call-template name="nbsp-ref"/>

			</td>

			<td>

				<a href="{@url}&amp;mill.template=me.askmore-dynamic" class="newsNext">

					<xsl:apply-templates select="@jobName"/>

				</a>

			</td>

		</tr>

	</xsl:template>

	<xsl:template match="JobItemSimple">

		<div class="frame">

			<div class="label">

				<xsl:value-of select="@datePost"/>

				<xsl:call-template name="nbsp-ref"/>

				<xsl:value-of select="@jobName"/>

			</div>

			<div class="text">

				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>

						<td width="25%" align="right">

							<xsl:value-of select="@dateEndString"/>

						</td>

						<td width="50">

							<xsl:call-template name="nbsp-ref"/>

						</td>

						<td width="75%">

							<xsl:value-of select="@dateEnd"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@ageString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@ageFromString"/>

							<xsl:value-of select="@ageFrom"/>, <xsl:value-of select="@ageTillString"/>

							<xsl:value-of select="@ageTill"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@genderString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@gender"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@educationString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@education"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@salaryString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@salary"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@cityString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@city"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@testPeriodString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@testPeriod"/>

						</td>

					</tr>

					<tr>

						<td align="right">

							<xsl:value-of select="@contactPersonString"/>

						</td>

						<td/>

						<td>

							<xsl:value-of select="@contactPerson"/>

						</td>

					</tr>

				</table>

				<table width="100%">

					<tr>

						<td class="jobHead">

							<xsl:value-of select="@textJobString"/>

						</td>

					</tr>

					<tr>

						<td>

							<xsl:value-of select="TextJob"/>

						</td>

					</tr>

				</table>

			</div>

		</div>

	</xsl:template>

	<xsl:template match="ShopPage">

		<xsl:value-of select="PriceHeader"/>

		<xsl:apply-templates select="CurrencyList"/>

		<table border="0" width="100%">

			<tr>

				<td>

					<xsl:apply-templates select="PricePosition"/>

				</td>

				<td align="right">

					<xsl:apply-templates select="CurrentBasket"/>

				</td>

			</tr>

		</table>

		<xsl:apply-templates select="GroupList"/>

		<xsl:apply-templates select="ItemList"/>

		<xsl:value-of select="PriceFooter"/>

	</xsl:template>

	<xsl:template match="CurrentBasket">

		<a href="{CurrentBasketUrl}">

			<xsl:value-of select="CurrentBasketName"/>

		</a>

	</xsl:template>



	<xsl:template match="PricePosition">

		<a href="{TopLevelUrl}">

		В начало

		<!--

			<xsl:value-of select="TopLevelName"/>

			-->

		</a>

		<xsl:apply-templates select="PositionItem"/>

	</xsl:template>

	<xsl:template match="PositionItem">

>> <a href="{PositionUrl}">

			<xsl:value-of select="PositionName"/>

		</a>

	</xsl:template>



	<xsl:template match="CurrencyList">

		<table width="100%" border="0">

			<tr>

				<form action="{CurrencySwitchUrl}" method="GET">

					<xsl:apply-templates select="HiddenParam"/>

					<td align="right">

						<select name="{CurrencySelectParam}" size="1">

							<option value="0">

								<xsl:value-of select="NoCurrencyName"/>

							</option>

							<xsl:apply-templates select="CurrencyItem"/>

						</select>

						<input type="submit" value="{CurrencyNameSwitch}"/>

					</td>

				</form>

			</tr>

		</table>

	</xsl:template>

	<xsl:template match="HiddenParam">

		<input type="hidden" value="{HiddenParamValue}" name="{HiddenParamName}"/>

	</xsl:template>

	<xsl:template match="GroupList">

		<table border="0" width="100%" cellpadding="2px" cellspacing="2px">

			<xsl:for-each select="GroupItem">

				<tr>

					<td class="priceData" width="100%">

						<a href="{GroupUrl}">

							<xsl:value-of select="GroupName"/>

						</a>

					</td>

				</tr>

			</xsl:for-each>

		</table>

	</xsl:template>

	<xsl:template match="ItemList">

		<table border="0" width="100%" cellpadding="2px" cellspacing="2px">

			<xsl:apply-templates select="PriceFieldName"/>

			<xsl:for-each select="PriceItem">

				<tr>

					<td class="priceData{ItemInBasket}">

						<table>

							<tr>

								<td valign="top">

									<img src="{ItemImageFileName}" alt="image"/>

								</td>

								<td valign="top">

									<xsl:value-of select="ItemName"/>

								</td>

							</tr>

						</table>

					</td>

					<td class="priceData{ItemInBasket}" align="right">

						<xsl:value-of select="ItemPrice"/>

					</td>

					<td class="priceData{ItemInBasket}">

						<xsl:value-of select="ItemNameCurrency"/>

					</td>

					<xsl:if test="//ShopPage/@isProcessInvoice='true'">

						<form action="{ItemToInvoice}">

							<xsl:apply-templates select="HiddenParam"/>

							<td class="priceData{ItemInBasket}" nowrap="0">

								<input type="text" size="3" value="1" name="{ItemToInvoiceCountParam}"/>

								<input type="submit" value="+"/>

							</td>

						</form>

					</xsl:if>

				</tr>

			</xsl:for-each>

		</table>

	</xsl:template>

	<xsl:template match="PriceFieldName">

		<tr align="center">

			<th class="priceData" width="85%">

				<xsl:value-of select="NameItem"/>

			</th>

			<th class="priceData" width="10%">

				<xsl:value-of select="NamePrice"/>

			</th>

			<th class="priceData" width="10%" align="center">

				<xsl:value-of select="NameCurrency"/>

			</th>

			<xsl:if test="//ShopPage/@isProcessInvoice='true'">

				<th class="priceData" width="5%" align="center">

					<xsl:value-of select="NameToInvoice"/>

				</th>

			</xsl:if>

		</tr>

	</xsl:template>

	

	<xsl:template match="CurrencyItem">

<xsl:text disable-output-escaping="yes">&lt;option</xsl:text>

		<xsl:value-of select="@selectCurrentCurrency"/> value="<xsl:value-of select="CurrencyID"/>"

<xsl:text disable-output-escaping="yes">&gt;</xsl:text>

			<xsl:value-of select="CurrencyName"/> 1:<xsl:value-of select="CurrencyCurs"/>

<xsl:text disable-output-escaping="yes">&lt;/option></xsl:text>

		

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



<xsl:template match="TopMenu">

<table cellspacing="10" cellpadding="0" border="0">

<tr><td width="100%" valign="top">



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

</td>

</tr>

</table>

</xsl:template>



<!--

<xsl:template match="TopMenu/MenuModule">

<tr>

	<td height="15">

	<a href="{ModuleUrl}" class="menuMain">

	<xsl:value-of select="ModuleName"/>

	</a>

	</td>

</tr>

</xsl:template>

-->



<xsl:template match="MenuModule">

	<tr>

			

      <xsl:choose>

        <xsl:when test='@includeLevel=1'/>

        <xsl:when test='@includeLevel=2'>

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

        <xsl:when test='@includeLevel=1'>

<xsl:attribute name="colspan">3</xsl:attribute>

        </xsl:when>

        <xsl:when test='@includeLevel=2'>

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

</xsl:stylesheet>

