package org.riverock.portlet.member;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.ModuleTypeTypeType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 17.04.2006
 *         Time: 15:14:16
 */
public class MemberPortletRenderMethod {
    private final static Logger log = Logger.getLogger(MemberPortletRenderMethod.class);

    public static void render(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("request: " + renderRequest);
            log.debug("response: " + renderResponse);
        }
        MemberProcessingRenderRequest mp = null;
        try {
            ResourceBundle bundle = (ResourceBundle) renderRequest.getAttribute(ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE);

            PrintWriter out = renderResponse.getWriter();

            if (!renderRequest.isUserInRole("webmill.member")) {
                out.println("Access denied, you have not right to execute 'webmill.member' portlet");
                return;
            }

            ModuleManager moduleManager = ModuleManager.getInstance(PropertiesProvider.getConfigPath());
            mp = new MemberProcessingRenderRequest(renderRequest, renderResponse, bundle, moduleManager);

            if (log.isDebugEnabled()) {
                log.debug("MemberProcessingRenderRequest - " + mp);

                for (Enumeration e = renderRequest.getParameterNames(); e.hasMoreElements();) {
                    String s = (String) e.nextElement();
                    log.debug("Request param: " + s + ", value: " + RequestTools.getString(renderRequest, s));
                }
                boolean isFound = false;
                for (Enumeration e = renderRequest.getAttributeNames(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();
                    log.debug("Request attr: " + key + ", value: " + renderRequest.getAttribute(key));
                    isFound = true;
                }
                if (!isFound) {
                    log.debug("Count of attr in request is: " + isFound);
                }
            }

///////////////

            String moduleName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_MODULE_PARAM);
            String actionName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_ACTION_PARAM);
            String subActionName = RequestTools.getString(renderRequest, MemberConstants.MEMBER_SUBACTION_PARAM, "").trim();

            if (log.isDebugEnabled()) {
                log.debug("Point #2.1 module '" + moduleName + "'");
                log.debug("Point #2.2 action '" + actionName + "'");
                log.debug("Point #2.3 subAction '" + subActionName + "'");
            }


            if (mp.mod == null) {
                out.println("Point #4.2. Module '" + moduleName + "' not found");
                return;
            }


            if (log.isDebugEnabled())
                log.debug("mod.getDefaultPortletType() - " + mp.mod.getType());

            // Check was module is lookup and can not calling directly from menu.
            if (mp.mod.getType() != null &&
            mp.mod.getType().getType() == ModuleTypeTypeType.LOOKUP_TYPE &&
            (mp.fromParam == null || mp.fromParam.length() == 0)
            ) {
                out.println("Point #4.4. Module " + moduleName + " is lookup module<br>");
                return;
            }

            int actionType = ContentTypeActionType.valueOf(actionName).getType();
            if (log.isDebugEnabled()) {
                log.debug("action name " + actionName);
                log.debug("ContentTypeActionType " + ContentTypeActionType.valueOf(actionName).toString());
                log.debug("action type " + actionType);
            }
            mp.content = MemberServiceClass.getContent(mp.mod, actionType);
            if (mp.content == null) {
                if (log.isDebugEnabled())
                    log.debug("Module: '" + moduleName + "', action '" + actionName + "', not found");

                out.println("Module: '" + moduleName + "', action '" + actionName + "', not found");
                return;
            }

            mp.initRight();

            if (!mp.isMainAccess) {
                if (log.isDebugEnabled())
                    log.debug("Access to module '" + moduleName + "' denied");

                out.println("Access to module '" + moduleName + "' denied");
                return;
            }

            if (!"commit".equalsIgnoreCase(subActionName)) {
                out.println("<h4>" + MemberServiceClass.getString(mp.content.getTitle(), renderRequest.getLocale()) + "</h4>");

                switch (actionType) {
                    case ContentTypeActionType.INDEX_TYPE:
                        if (mp.isIndexAccess) {
                            String recStr = mp.checkRecursiveCall();
                            if (recStr == null) {
                                String sql_ = mp.buildSelectSQL();

                                if (log.isDebugEnabled())
                                    log.debug("index SQL\n" + sql_);

                                if (mp.isInsertAccess) {
                                    out.println("<p>" + mp.buildAddURL() + "</p>");
                                }
                                String s = mp.buildSelectHTMLTable(sql_);
                                out.println(s);
                            } else
                                out.println(recStr);
                        } else {
                            putAccessDenied(out, moduleName);
                        }
                        break;

                    case ContentTypeActionType.INSERT_TYPE:
                        if (mp.isInsertAccess) {
                            out.println(mp.buildAddCommitForm() + "<br>");
                            out.println(mp.buildIndexURL() + "<br><br>");
                            putInsertButton(out, mp, renderRequest);
                            out.println(mp.buildAddHTMLTable());
                            putInsertButton(out, mp, renderRequest);
                            putEndForm(out);
                        } else {
                            putAccessDenied(out, moduleName);
                        }
                        break;

                    case ContentTypeActionType.CHANGE_TYPE:
                        if (mp.isChangeAccess) {
                            out.println(mp.buildUpdateCommitForm() + "<br>");

                            String sql_ = mp.buildSelectForUpdateSQL();

                            if (log.isDebugEnabled())
                                log.debug("SQL " + sql_);

                            if (log.isDebugEnabled())
                                log.debug("buildIndexURL");

                            out.println(mp.buildIndexURL() + "<br><br>");

                            putChangeButton(out, mp, renderRequest);

                            if (log.isDebugEnabled())
                                log.debug("buildUpdateHTMLTable");

                            out.println(mp.buildUpdateHTMLTable(sql_));
                            putChangeButton(out, mp, renderRequest);
                            putEndForm(out);
                        } else {
                            putAccessDenied(out, moduleName);
                        }
                        break;

                    case ContentTypeActionType.DELETE_TYPE:
                        if (mp.isDeleteAccess) {
                            out.println(mp.buildDeleteCommitForm() + "<br>");

                            String sql_ = mp.buildSelectForDeleteSQL();

                            if (log.isDebugEnabled())
                                log.debug("SQL " + sql_);

                            out.println(mp.buildIndexURL() + "<br><br>");
                            putDeleteButton(out, mp, renderRequest);
                            out.println(mp.buildDeleteHTMLTable(sql_));
                            putDeleteButton(out, mp, renderRequest);
                            putEndForm(out);
                        } else {
                            putAccessDenied(out, moduleName);
                        }
                        break;

                    default:
                        log.error("Unknonw action - '" + actionType + "'");
                }
                out.println("<br><p>" + mp.buildMainIndexURL() + "</p>");
            }
        }
        finally {
            if (mp != null) {
                mp.destroy();
            }
        }
    }

    private static void putAccessDenied(PrintWriter out, String moduleName) {
        out.println("Access to module '" + moduleName + "' is denied");
    }

    private static void putInsertButton(PrintWriter out, MemberProcessingRenderRequest mp, RenderRequest renderRequest) {
        putButton(out, mp, renderRequest, "Add");
    }

    private static void putChangeButton(PrintWriter out, MemberProcessingRenderRequest mp, RenderRequest renderRequest) {
        putButton(out, mp, renderRequest, "Change");
    }

    private static void putDeleteButton(PrintWriter out, MemberProcessingRenderRequest mp, RenderRequest renderRequest) {
        putButton(out, mp, renderRequest, "Delete");
    }

    private static void putButton(PrintWriter out, MemberProcessingRenderRequest mp, RenderRequest renderRequest, String name) {
        out.println("<input type=\"submit\" class=\"par\" value=\"" +
        MemberServiceClass.getString(
        mp.content.getActionButtonName(),
        renderRequest.getLocale(),
        name
        ) +
        "\">\n"
        );
    }

    private static void putEndForm(PrintWriter out) {
        out.println("</form>");
    }
}
