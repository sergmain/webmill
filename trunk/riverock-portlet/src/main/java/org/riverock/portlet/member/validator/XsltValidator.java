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
package org.riverock.portlet.member.validator;

import java.io.StringReader;

import javax.portlet.PortletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.riverock.portlet.schema.member.FieldsType;
import org.riverock.portlet.member.MemberServiceClass;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.webmill.container.tools.PortletService;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 15:30:16
 *         $Id$
 */
public class XsltValidator implements MemberValidator {
    public static String validate(PortletRequest renderRequest, String moduleName, FieldsType ff) {
        String data = PortletService.getString(renderRequest, moduleName + '.' + MemberServiceClass.getRealName(ff), null);
        try
        {
            Source xslSource = new StreamSource(
                    new StringReader( data )
            );

            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.newTemplates(xslSource);
        }
        catch (Exception e)
        {
            return "Error validate field "+ff.getName()+" as 'XSLT' type field<br>"+
                    ExceptionTools.getStackTrace(e, 20, "<br>");
        }
        return null;
    }
}
