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
package org.riverock.portlet.manager.site;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.apache.myfaces.custom.tree2.TreeStateBase;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;

import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 16.05.2006
 *         Time: 20:06:35
 *         $Id$
 */
@SuppressWarnings("unchecked")
public class SiteTree implements Serializable {
    private final static Logger log = Logger.getLogger(SiteTree.class);
    private static final long serialVersionUID = 2057005500L;

    private SiteService siteService = null;
    private TreeState treeState=null;

    private SiteSessionBean siteSessionBean = null;

    public SiteTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public SiteSessionBean getSiteSessionBean() {
        return siteSessionBean;
    }

    public void setSiteSessionBean(SiteSessionBean siteSessionBean) {
        this.siteSessionBean = siteSessionBean;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public TreeModel getSiteTree() {
        log.info("Invoke getSiteTree()");

        TreeNode rootNode = getPrepareSiteTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    public TreeNode getPrepareSiteTree() {

        log.info("Invoke getPrepareSiteTree()");

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);
        if (siteSessionBean.getCurrentSiteId()!=null) {
            TreeNode treeData = new TreeNodeBase("site-list", "Webmill portal. List of sites.", false);
            treeRoot.getChildren().add(treeData);

            Site site = siteService.getSite(siteSessionBean.getCurrentSiteId());

            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);
            treeRoot.getChildren().add(siteNode);

            if (FacesTools.isUserInRole("webmill.css") || FacesTools.isUserInRole("webmill.site-manager") ||
                FacesTools.isUserInRole("webmill.portal-manager")) {
                TreeNodeBase cssListNode = new TreeNodeBase("css-list", "Css list", site.getSiteId().toString(), false);
                treeRoot.getChildren().add(cssListNode);

                for (Css css : siteService.getCssList(site.getSiteId())) {
                    TreeNodeBase cssNode =
                        new TreeNodeBase("css", StringUtils.isNotBlank(css.getCssComment()) ? css.getCssComment() : "CSS",
                            css.getCssId().toString(), true);

                    cssListNode.getChildren().add(cssNode);
                }

            }

            if (FacesTools.isUserInRole("webmill.template") ||
                FacesTools.isUserInRole("webmill.xslt") ||
                FacesTools.isUserInRole("webmill.site-manager") ||
                FacesTools.isUserInRole("webmill.portal-manager")) {

                TreeNodeBase siteLanguageListNode = new TreeNodeBase("site-language-list", "Site languages", site.getSiteId().toString(), false);
                treeRoot.getChildren().add(siteLanguageListNode);

                for (SiteLanguage siteLanguage : siteService.getSiteLanguageList(site.getSiteId())) {
                    TreeNodeBase siteLanguageNode = new TreeNodeBase(
                        "site-language",
                        siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                        siteLanguage.getSiteLanguageId().toString(),
                        false);
                    siteLanguageListNode.getChildren().add(siteLanguageNode);

                    if (FacesTools.isUserInRole("webmill.template") || FacesTools.isUserInRole("webmill.site-manager") ||
                        FacesTools.isUserInRole("webmill.portal-manager")) {

                        TreeNodeBase templateListNode = new TreeNodeBase("template-list", "Templates", siteLanguage.getSiteLanguageId().toString(), false);
                        for (Template template : siteService.getTemplateList(siteLanguage.getSiteLanguageId())) {
                            templateListNode.getChildren().add(
                                new TreeNodeBase("template", template.getTemplateName(), template.getTemplateId().toString(), true)
                            );
                        }
                        siteLanguageNode.getChildren().add(templateListNode);

                    }

                    if (FacesTools.isUserInRole("webmill.xslt") || FacesTools.isUserInRole("webmill.site-manager") ||
                        FacesTools.isUserInRole("webmill.portal-manager")) {

                        TreeNodeBase xsltListNode = new TreeNodeBase("xslt-list", "Xslt list", siteLanguage.getSiteLanguageId().toString(), false);
                        for (Xslt xslt : siteService.getXsltList(siteLanguage.getSiteLanguageId())) {
                            xsltListNode.getChildren().add(
                                new TreeNodeBase("xslt", xslt.getName(), xslt.getId().toString(), true)
                            );
                        }
                        siteLanguageNode.getChildren().add(xsltListNode);
                    }
                }
            }
        }
        return treeRoot;
    }
}
