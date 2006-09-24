/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.test;

/**
 * User: Admin
 * Date: Jan 29, 2003
 * Time: 12:21:44 AM
 *
 * $Id$
 */
public class TestEncodeUrl
{
    public static void main(String args[])
        throws Exception
    {
        String s =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<SiteListSite IdSite=\"16\" IdFirm=\"1\" IsCssDynamic=\"true\" IsRegisterAllowed=\"false\" IsActivateEmailOrder=\"true\"><DefLanguage>ru</DefLanguage><DefCountry>RU</DefCountry><DefVariant></DefVariant><NameSite>Mill Engine</NameSite><AdminEmail></AdminEmail><CssFile>/front_styles.css</CssFile><OrderEmail>'tiger@localhost'%</OrderEmail></SiteListSite>";

//        System.out.println( URIUtil.encodeQuery( s ) );

        String s1 = "%3C%3Fxml%20version%3D%221.0%22%20encoding%3D%22UTF-8%22%3F%3E%3CSiteListSite%20IdSite%3D%2216%22%20IdFirm%3D%221%22%20IsCssDynamic%3D%22true%22%20IsRegisterAllowed%3D%22false%22%20IsActivateEmailOrder%3D%22true%22%3E%3CDefLanguage%3Eru%3C%2FDefLanguage%3E%3CDefCountry%3ERU%3C%2FDefCountry%3E%3CDefVariant%3E%3C%2FDefVariant%3E%3CNameSite%3EMill%20Engine%3C%2FNameSite%3E%3CAdminEmail%3E%3C%2FAdminEmail%3E%3CCssFile%3E%2Ffront_styles.css%3C%2FCssFile%3E%3COrderEmail%3Etiger%40localhost%3C%2FOrderEmail%3E%3C%2FSiteListSite%3E";
//        System.out.println( URIUtil.decode( s1 ) );
    }
}
