/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * User: Admin
 * Date: Feb 22, 2003
 * Time: 11:37:50 PM
 *
 * $Id$
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
        org.riverock.generic.startup.StartupApplication.init();
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
