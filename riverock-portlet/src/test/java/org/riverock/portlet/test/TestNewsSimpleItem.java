/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 */

/**
 * User: Admin
 * Date: Feb 22, 2003
 * Time: 11:37:50 PM
 *
 * $Id: TestNewsSimpleItem.java 1111 2006-11-30 00:18:47Z serg_main $
 */
package org.riverock.portlet.test;



public class TestNewsSimpleItem
{
/*
    private static DatabaseAdapter db_ = null;

    private static HttpServletRequestApplWrapper request = null;
    private static HttpServletResponseApplWrapper response = null;
    private static HttpSessionApplWrapper session = null;
    private static InitPage jspPage = null;

    private final static String TEST_SERVER_NAME = "test-host";
    private final static String TEST_LANGUAGE = "ru_RU";
    private final static String NAME_TEST_SITE = "Пробный сайт";
*/
    public static void main(String[] s)
    throws Exception
    {
        throw new Exception("Old version of test. Use TestCaseNews class");
/*
        org.riverock.common.startup.StartupApplication.init();
        String nameConnection = "MSSQL-JTDS";
        DatabaseConnectionType dbConnConfig = InitParam.getDatabaseConnection( nameConnection );
        WebmillConfig conf = InitParam.getWebmillConfig();
        conf.setDefaultConnectionName( nameConnection );

        db_ = DatabaseAdapter.getInstance(true, nameConnection);

        request = null;
        request = new HttpServletRequestApplWrapper();
        request.setServerName( TEST_SERVER_NAME );
        request.setParameter( Constants.NAME_LANG_PARAM, TEST_LANGUAGE  );

        response = null;
        response = new HttpServletResponseApplWrapper();

        session = null;
        session = new HttpSessionApplWrapper();

        request.setSession( session );

        jspPage = new InitPage(db_, request, response,
                null,
                Constants.NAME_LANG_PARAM, null, null);

        PortletDescriptionType desc = PortletManager.getPortletDescription( "mill.news" );

        // localePackage надо брать из файла описателя портлетов
        PortletParameter portletParameter =
            new PortletParameter(request, response, jspPage, desc.getLocaleNamePackage());

//        PortletFile[] portlet = PortletManager.getPortletFileArray();
//        PortletClassGetListType portletClassList = new PortletClassGetListType();

        request.setParameter(desc.getNamePortletID(), ""+90);

        PortletResultObject obj =
            PortletTools.getPortletObject(desc, portletParameter);

        byte[] resultByte = obj.getXml();

        MainTools.writeToFile(SiteUtils.getTempDir()+"test-news-by-id-bytes-from-portlet.xml", resultByte);
*/
    }
}
