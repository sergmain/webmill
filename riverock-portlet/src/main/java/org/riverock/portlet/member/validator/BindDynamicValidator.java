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

import javax.portlet.PortletRequest;

import org.riverock.portlet.schema.member.FieldsType;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 15:31:55
 *         $Id$
 */
public class BindDynamicValidator implements MemberValidator {
    public static String validate(PortletRequest renderRequest, String moduleName, FieldsType ff) {
        return null;
/*
                        Long idTemplate = PortletService.getLong(renderRequest, mod.getName() + '.' + "ID_SITE_TEMPLATE");
                        Long idCtxCatalog = PortletService.getLong(renderRequest, mod.getName() + '.' + "ID_SITE_CTX_CATALOG");
                        if (log.isDebugEnabled())
                        {
                            log.debug("idTemplate "+idTemplate);
                            log.debug("idCtxCatalog "+idCtxCatalog);
                        }

                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String ctxType = null;
                        try
                        {
                            ps = dbDyn.prepareStatement(
                                "select a.TYPE " +
                                "from   WM_PORTAL_PORTLET_NAME a, WM_PORTAL_CATALOG b " +
                                "where  a.ID_SITE_CTX_TYPE=b.ID_SITE_CTX_TYPE and b.ID_SITE_CTX_CATALOG=?"
                            );

                            RsetTools.setLong(ps,  1, idCtxCatalog );

                            rs = ps.executeQuery();
                            if (rs.next())
                            {
                                ctxType = RsetTools.getString(rs, "TYPE");
                            }
                            else
                                return "Menu with ID "+idCtxCatalog+" not found";

                        }
                        catch (Exception e)
                        {
                            log.debug("Error get type of context",e);
                            return "Error validate field "+ff.getName()+"<br>"+
                                    ExceptionTools.getStackTrace(e, 20, "<br>");
                        }
                        finally
                        {
                            DatabaseManager.close(rs, ps);
                            rs = null;
                            ps = null;
                        }

                        if ( !ctxType.equalsIgnoreCase("mill.index") )
                            return null;

                        SiteTemplate template = null;
                        WmPortalTemplateItemType templateItem = null;
                        try
                        {
                            templateItem = GetWmPortalTemplateItem.getInstance(dbDyn, idTemplate).item;
                            template = (SiteTemplate)Unmarshaller.unmarshal(SiteTemplate.class,
                                    new InputSource(new StringReader( templateItem.getTemplateData() ) )
                            );
                            template.setNameTemplate( templateItem.getNameSiteTemplate() );
                        }
                        catch (Exception e)
                        {
                            log.debug("Error get template",e);
                            return
                                "Error validate field "+ff.getName()+"<br>"+
                                "template id "+idTemplate+"<br>"+
                                ExceptionTools.getStackTrace(e, 20, "<br>");
                        }
                        if (template==null)
                            return "Template with ID "+idTemplate+" not found";

                        if ( isDynamic( template ) )
                            return
                                "Template '"+template.getNameTemplate()+"' is dynamic template and "+
                                " can not bind to context with 'mill.index' type";
*/

    }

}
