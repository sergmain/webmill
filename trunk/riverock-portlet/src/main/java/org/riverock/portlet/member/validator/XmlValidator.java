/*
 * org.riverock.portlet -- Portlet Library
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
