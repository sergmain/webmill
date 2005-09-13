package org.riverock.portlet.member.validator;

import java.io.StringReader;

import javax.portlet.PortletRequest;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.riverock.portlet.schema.member.FieldsType;
import org.riverock.portlet.member.MemberServiceClass;
import org.riverock.portlet.tools.RequestTools;

import org.riverock.common.tools.ExceptionTools;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 15:30:16
 *         $Id$
 */
public class XsltValidator implements MemberValidator {
    public static String validate(PortletRequest renderRequest, String moduleName, FieldsType ff) {
        String data = RequestTools.getString(renderRequest, moduleName + '.' + MemberServiceClass.getRealName(ff));
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
