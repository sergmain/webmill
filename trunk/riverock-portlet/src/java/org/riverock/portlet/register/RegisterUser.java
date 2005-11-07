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
package org.riverock.portlet.register;

import java.io.File;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.generic.tools.XmlTools;

import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 4:32:29 PM
 *
 * $Id$
 */
public final class RegisterUser implements PortletResultObject, PortletResultContent {

    private final static Log log = LogFactory.getLog(RegisterUser.class);

    public RegisterUser()
    {
    }

    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private ResourceBundle bundle = null;

    public void setParameters( final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
    }

    public PortletResultContent getInstance( final Long id ) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( final String portletCode_ ) throws PortletException {
        try {
            return this;
        }
        catch(Exception e) {
            final String es = "Error create Register object";
            log.error(es, e);
            throw new PortletException( es, e );
        }
    }

    public PortletResultContent getInstance()
        throws PortletException {

        try {
            return this;
        }
        catch(Exception e) {
            final String es = "Error create Register object";
            log.error(es, e);
            throw new PortletException( es, e );
        }
    }


    private static Object syncDebug = new Object();
    public byte[] getXml(String rootElement) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("start get XmlByte array, rootElement: " + rootElement );
        }


        byte b[] = null;
        try {
            b = XmlTools.getXml( null, rootElement );
        }
        catch(Exception e) {
            log.error("Error persists register object to XML", e);
            try{
                log.error("info Xerces version - " + org.apache.xerces.impl.Version.getVersion() );
            }
            catch(Exception e2){
                log.error("Error get version of xerces "+e.getMessage());
            }
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("end get XmlByte array. length of array - " + b.length);

            final String testFile = SiteUtils.getTempDir()+File.separatorChar+"regiser-item.xml";
            log.debug("Start output test data to file " + testFile );
            synchronized(syncDebug){
                MainTools.writeToFile(testFile, b);
            }
            log.debug("end output data");
        }

        return b;
    }

    public byte[] getXml() throws Exception {
        return getXml( "Register" );
    }

    public byte[] getPlainHTML() throws Exception {
        StringBuffer out = new StringBuffer();
        try {

//            PortalInfo portalInfo = (PortalInfo)renderRequest.getAttribute(ContainerConstants.PORTAL_INFO_ATTRIBUTE);
// Todo write method which return 'index' template and replace "" to name of 'index' template
            String indexPage = PortletService.url(ContainerConstants.CTX_TYPE_INDEX, renderRequest, renderResponse, "" );

            printSendPasswordForm( out );

            boolean isRegisterAllowed = false;
            //Todo uncomment and implement
/*
            boolean isRegisterAllowed = portalInfo.getSites().getIsRegisterAllowed();
*/
            if (log.isDebugEnabled()) {
                log.debug("getIsRegisterAllowed " + isRegisterAllowed);
            }

            if ( isRegisterAllowed ) {
                printNewRegisterForm( out );
            }
            else {
                printMessageRegistrationDisabled( out, indexPage );
            }
            
            return out.toString().getBytes( ContentTypeTools.CONTENT_TYPE_UTF8 );
        }
        catch (Exception e) {
            final String es = "Erorr processing RegisterUser page";
            log.error(es, e);
            throw new PortletException( es, e );
        }
    }

    private void printSendPasswordForm( StringBuffer out ) {
            out.append( 
                bundle.getString( "reg.forgot_password" ) + "\n" +
                "<table>\n" +

                "<form method=\"POST\" action=\"" + PortletService.ctx( renderRequest ) + "\" >\n" +
                ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, RegisterPortlet.REGISTER_PROCESS_PORTLET ) +
                ServletTools.getHiddenItem( RegisterPortlet.NAME_REGISTER_ACTION_PARAM, "send_pass") +
                "<tr>\n" +
                "<td witdh=\"25%\">" + bundle.getString( "reg.member_email" ) + ":</td>\n" +
                "<td><input type=\"text\" name=\"email\" size=\"12\"></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td colspan=\"2\"><input type=\"submit\" value=\"" + bundle.getString( "reg.send_password" ) + "\"></td>\n" +
                "</tr>\n" +
                "</form>\n" +
                "</table>\n"
            );
    } 

    private void printMessageRegistrationDisabled( StringBuffer out, String indexPage ) throws Exception {

        out        
           .append("<p class=\"register-text\">Registration on this site disabled by administrator.</p>")
           .append("<p class=\"register-continue\">You can continue work at <a href=\"")
           .append(indexPage)
           .append("\">homepage</a></p>");

    } 

    private void printNewRegisterForm( StringBuffer out ) throws Exception {

            out.append("<br>\n\n");
            out.append( bundle.getString( "reg.need_register" ) );
            out.append(
                "<table>\n" +
                "<form method=\"POST\" action=\"" + PortletService.ctx( renderRequest ) + "\" >\n" +
                ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, RegisterPortlet.REGISTER_PROCESS_PORTLET ) +
                ServletTools.getHiddenItem( RegisterPortlet.NAME_REGISTER_ACTION_PARAM, "reg_new") 
            );
            out.append("<tr>\n");
            out.append("<td witdh=\"25%\">");
            out.append( bundle.getString( "reg.login" ) );
            out.append(":");
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"text\" name=\"username\" size=\"20\" maxlength=\"20\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.password" ) );
            out.append(":");
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"password\" name=\"password1\" size=\"20\" maxlength=\"20\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.password_repeat" ) );
            out.append(":");
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"password\" name=\"password2\" size=\"20\" maxlength=\"20\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.first_name" ) );
            out.append(":");
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"text\" name=\"first_name\" size=\"50\" maxlength=\"50\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.last_name" ) );
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"test\" name=\"last_name\" size=\"50\" maxlength=\"50\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.telephone" ) );
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"test\" name=\"phone\" size=\"25\" maxlength=\"25\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.address" ) );
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"test\" name=\"addr\" size=\"50\" maxlength=\"50\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td>");
            out.append( bundle.getString( "reg.email" ) );
            out.append("</td>\n");
            out.append("<td>");
            out.append("<input type=\"text\" name=\"email\" size=\"30\" maxlength=\"30\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("<tr>\n");
            out.append("<td colspan=\"2\">");
            out.append("<input type=\"submit\" value=\"");
            out.append( bundle.getString( "reg.register" ) );
            out.append("\">");
            out.append("</td>\n");
            out.append("</tr>\n");
            out.append("</form>\n");
            out.append("</table>");

    }
}
