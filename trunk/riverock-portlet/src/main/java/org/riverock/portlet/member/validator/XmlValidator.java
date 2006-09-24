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
package org.riverock.portlet.member.validator;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.portlet.PortletRequest;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;


import org.riverock.portlet.member.MemberServiceClass;
import org.riverock.portlet.schema.member.FieldsType;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 15:24:23
 *         $Id$
 */
public class XmlValidator implements MemberValidator {
    public static String validate(PortletRequest renderRequest, String moduleName, FieldsType ff) {
        String data = PortletService.getString(renderRequest, moduleName + '.' + MemberServiceClass.getRealName(ff), null);

        try
        {
            InputSource inSrc = new InputSource( new StringReader( data ) );

            DefaultHandler handler = new DefaultHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( inSrc, handler);
        }
        catch(Exception e)
        {
            return "Error validate field "+ff.getName()+" as 'XML' type field<br>"+e.toString();
        }
        return null;
    }
}
