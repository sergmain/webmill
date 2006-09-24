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
