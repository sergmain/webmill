/*

 * org.riverock.sso -- Single Sign On implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

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



/**

 *

 * $Id$

 *

 */

package org.riverock.sso.main;



public class Constants

{

    public final static String CTX_TYPE_LOGIN_CHECK   = "mill.login_check";

    public final static String NAME_LANG_PARAM = "mill.lang";



    public static final String NAME_TOURL_PARAM     = "mill.tourl";



    public final static String AUTH_SESSION = "MILL.AUTH_SESSION";



    public final static String JNDI_SSO_CONFIG_FILE = "jsmithy/sso/ConfigFile";



/*

    public final static String NAME_LOCALE_COOKIE   = "mill.lang.cookie";



    public final static String NAME_PORTLET_PARAM   = "mill.portlet";



    public final static String NAME_XMLROOT_PARAM = "mill.xmlroot";



    public final static String NAME_YEAR_PARAM = "mill.year";

    public final static String NAME_MONTH_PARAM = "mill.month";



    public final static String NAME_ID_SHOP_PARAM = "mill.id_shop";

    public final static String NAME_ID_FORUM_PARAM = "mill.id_forum";

    public final static String NAME_ID_MESSAGE_FORUM_PARAM = "mill.id_forum_message";

    public final static String NAME_ID_MAIN_FORUM_PARAM = "mill.forum.id_main";



    public final static String NAME_INVOICE_NEW_COUNT_PARAM = "mill.invoice.count";

    public final static String NAME_SHOP_SORT_BY = "mill.shop.sort";

    public final static String NAME_SHOP_SORT_DIRECT = "mill.shop.sort.direct";

    public final static String NAME_ID_GROUP_SHOP = "mill.id_group";

    public final static String NAME_ID_LANGUAGE   = "mill.id_language";

    public final static String NAME_ID_CURRENCY_SHOP   = "mill.id_currency";

    public final static String NAME_COUNT_ADD_ITEM_SHOP    = "mill.count_add";

    public final static String NAME_ADD_ID_ITEM = "mill.add_id";

    public final static String NAME_ID_JOB_PARAM  = "mill.id_job";

    public final static String NAME_ID_CONTEXT_PARAM    = "mill.id_context";

    public final static String NAME_TYPE_CONTEXT_PARAM    = "mill.context";

    public final static String NAME_TEMPLATE_CONTEXT_PARAM    = "mill.template";



    public final static String NAME_AS_XML_FORMAT = "mill.asxml";

    public final static String NAME_ID_NEWS_PARAM = "mill.id_news_item";



    public final static String NAME_ID_ARTICLE_PARAM = "mill.id_article";

    public final static String NAME_ADMIN_PARAM = "mill.admin";

    public final static String NAME_ID_FAQ_PARAM = "mill.id_faq";



    public static final String NAME_REGISTER_ACTION_PARAM = "mill.register.action";





    public final static String NAME_FAQ_CODE_PARAM    = "mill.faq_code";

    public final static String NAME_FAQ_BLOCK_CODE_PARAM     = "mill.faq_block_code";

    public final static String NAME_NEWS_CODE_PARAM    = "mill.news_code";

    public final static String NAME_NEWS_BLOCK_CODE_PARAM    = "mill.news_block_code";

    public final static String NAME_ARTICLE_CODE_PARAM    = "mill.article_code";



    public static final String MEMBER_MODULE_PARAM    = "mill.mm"; //module

    public static final String MEMBER_ACTION_PARAM    = "mill.ma"; // action

    public static final String MEMBER_SUBACTION_PARAM    = "mill.ms"; // subaction

    public static final String MEMBER_FROM_PARAM     = "mill.mf"; // from



    public static final String NAME_USERNAME_PARAM     = "mill.username";

    public static final String NAME_PASSWORD_PARAM     = "mill.password";





    public static final String MEMBER_NAME_MOD_PARAM    = "mill.member.mod"; //module

    public static final String MEMBER_NAME_APPL_PARAM    = "mill.member.appl"; //appl



    public static final String MILL_MEMBER_DIR    = "xml";

    public static final String MILL_PORTLET_DIR    = "portlet";

    public static final String MILL_APPL_DIR    = "appl";

    public static final String MILL_DEFINITION_DIR    = "data-definition";



    public static final String NAME_APPL_PARAM = "p";



    public final static String JNDI_MILL_LOG_PATH = "mill/LogPath";

    public final static String JNDI_MILL_LOG_CONFIG_FILE = "mill/LogConfigFile";

    public final static String JNDI_MILL_CONFIG_FILE = "mill/ConfigFile";



    public final static String JNDI_CUSTOM_PORTLET_DIR = "mill/CustomPortletDir";

    public final static String JNDI_CUSTOM_MEMBER_DIR = "mill/CustomMemberDir";

    public final static String JNDI_CUSTOM_APPL_DIR = "mill/CustomApplicationDir";



    public final static String CTX_TYPE_FAQ = "mill.faq";

    public final static String CTX_TYPE_MENU = "mill.menu";

    public final static String CTX_TYPE_MENU_MEMBER = "mill.menu_member";

    public final static String CTX_TYPE_MEMBER   = "mill.member";

    public final static String CTX_TYPE_MEMBER_COMMIT = "mill.member_commit";

    public final static String CTX_TYPE_MEMBER_VIEW = "mill.member_view";



    public final static String CTX_TYPE_REGISTER   = "mill.register";

    public final static String CTX_TYPE_SHOP = "mill.shop";

    public final static String CTX_TYPE_NEWS = "mill.news";

    public final static String CTX_TYPE_JOB = "mill.job";

    public final static String CTX_TYPE_LOGOUT = "mill.logout";



    public final static String CTX_TYPE_ARTICLE_PLAIN = "mill.article_plain";

    public final static String CTX_TYPE_ARTICLE_XML = "mill.article_xml";



    public final static String CTX_TYPE_NEWS_BLOCK = "mill.news_block";

    public final static String CTX_TYPE_POSITION_BLOCK = "mill.position_block";

    public final static String CTX_TYPE_LANGUAGE = "mill.language";

    public final static String CTX_TYPE_INDEX = "mill.index";

    public final static String CTX_TYPE_LOGIN  = "mill.login";

    public final static String CTX_TYPE_UPLOAD_PRICE_CONTR    = "mill.upload_price_controller";

    public final static String CTX_TYPE_UPLOAD_PRICE   = "mill.upload_price";



    public final static String CTX_TYPE_FORUM = "mill.forum";

    public final static String CTX_TYPE_FORUM_COMMIT   = "mill.forum_commit";



    public final static String CTX_TYPE_INVOICE = "mill.invoice";



//    public final static String TYPE_TEMPLATE_ITEM_FILE = "file";

//    public final static String TYPE_TEMPLATE_ITEM_PORTLET  ="portlet";

//    public final static String TYPE_TEMPLATE_ITEM_DYNAMIC = "dynamic";

//    public final static String TYPE_TEMPLATE_ITEM_CUSTOM = "custom";





//    public final static String URI_CSS_PAGE = "/share/css.jsp";



//    public final static String URI_SHOP_PAGE = "/share/jsp/shop.jsp";

//    public final static String URI_INVOICE_PAGE = "/share/jsp/invoice.jsp";

//    public final static String URI_REGISTER_PAGE = "/share/jsp/register.jsp";

    public final static String URI_CTX_MANAGER = "/ctx";



//    public final static String URI_MEMBER_PAGE = "/member/index.jsp";

//    public final static String URI_LOGIN_PAGE = "/member/index.jsp";

//    public final static String URI_LOGIN_CTX = "/member/index.jsp";



//    public final static String URI_LOGOUT_PAGE = "/member/auth/logout.jsp";

//    public final static String URI_LOGIN_CHECK_PAGE = "/member/auth/login.jsp";



//    public final static String URI_FORUM_PAGE = "/share/jsp/forum.jsp";

//    public final static String URI_NEWS_BLOCK_PAGE    = "/ctx";

//    public final static String URI_NEWS_PAGE = "/share/jsp/news.jsp";

//    public final static String URI_POSITION_BLOCK_PAGE   = "/share/jsp/position_block.jsp";

//    public final static String URI_POSITION_PAGE = "/share/jsp/position.jsp";

//    public final static String URI_ARTICLE_PAGE = "/share/jsp/article.jsp";

//    public final static String URI_FAQ_PAGE = "/share/jsp/faq.jsp";







// context's jsp

//    public final static String URI_LANGUAGE_CTX = "/share/ctx/language.jsp";

//    public final static String URI_NEWS_BLOCK_CTX = "/share/ctx/news_block.jsp";

//    public final static String URI_POSITION_BLOCK_CTX = "/share/ctx/position_block.jsp";





    public final static String CURRENT_SHOP = "MILL.CURRENT_SHOP";

    public final static String BASKET_SHOP_SESSION = "MILL.BASKET_SHOP_SESSION";

// константа для хранения объекта в сессиии. Объект - заказ товаров - OrderType

    public final static String ORDER_SESSION = "MILL.ORDER_SESSION";



    public final static String PAGE_TITLE_SESSION = "MILL.PAGE_TITLE_SESSION";



//    public final static String TYPE_CTX_SESSION = "MILL.TYPE_CTX_SESSION";

//    public final static String PORTLET_CODE_SESSION     = "MILL.PORTLET_CODE_SESSION";

//    public final static String TEMPLATE_NAME_SESSION = "MILL.TEMPLATE_NAME_SESSION";



//    public final static String CURRENT_CATALOG_ITEM_SESSION = "MILL.CURRENT_CATALOG_ITEM_SESSION";

    public final static String FORUM_SUBJECT_SESSION = "MILL.FORUM_SUBJECT_SESSION";

    public final static String ID_SHOP_SESSION = "MILL.ID_SHOP_SESSION";





    public final static String MD5RSA = "md5WithRSAEncryption";

    public final static String FORGE_MD5RSA = "MD5withRSA";



    public final static String CARD_VISA = "VISA";

    public final static String CARD_MASTER = "MASTER";

    public final static String CARD_DINER_CLUB = "DINERCLUB";

    public final static String CARD_AMEX = "AMERICAN_EXPRESS";

    public final static String CARD_SBER = "SBER_BANK";



    public final static String CURRENCY_RUB = "RUB";

    public final static String CURRENCY_USD = "USD";

    public final static String CURRENCY_EURO = "EURO";



    public final static String RSA = "RSA";

*/

}