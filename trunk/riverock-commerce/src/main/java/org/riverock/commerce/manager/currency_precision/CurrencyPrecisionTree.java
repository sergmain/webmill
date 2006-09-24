/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.manager.currency_precision;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.*;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.manager.currency.CurrencyBean;
import org.riverock.webmill.container.ContainerConstants;

import java.io.Serializable;
import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 1:15:09
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionTree implements Serializable {
    private final static Logger log = Logger.getLogger(CurrencyPrecisionTree.class);
    private static final long serialVersionUID = 2057005500L;

    private CurrencyPrecisionService currencyPrecisionService = null;
    private TreeState treeState=null;

    private CurrencyPrecisionSessionBean currencyPrecisionSessionBean = null;

    public CurrencyPrecisionTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public CurrencyPrecisionService getCurrencyPrecisionService() {
        return currencyPrecisionService;
    }

    public void setCurrencyPrecisionService(CurrencyPrecisionService currencyPrecisionService) {
        this.currencyPrecisionService = currencyPrecisionService;
    }

    public CurrencyPrecisionSessionBean getCurrencyPrecisionSessionBean() {
        return currencyPrecisionSessionBean;
    }

    public void setCurrencyPrecisionSessionBean(CurrencyPrecisionSessionBean currencyPrecisionSessionBean) {
        this.currencyPrecisionSessionBean = currencyPrecisionSessionBean;
    }

    public TreeModel getCurrencyPrecisionTree() {
        log.info("Invoke getCurrencyPrecisionTree()");

        TreeNode rootNode = getPrepareCurrencyPrecisionTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    private TreeNode getPrepareCurrencyPrecisionTree() {

        log.info("Invoke getPrepareCurrencyPrecisionTree()");

        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);

        List<ShopBean> shops = CommerceDaoFactory.getShopDao().getShopList(siteId);

        for (ShopBean shop : shops) {
            TreeNodeBase shopNode = new TreeNodeBase("shop", shop.getShopName(), shop.getShopId().toString(), false);
            treeRoot.getChildren().add(shopNode);

            for (CurrencyPrecisionBean currencyPrecisionBean : CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecisionList(shop.getShopId())) {
                CurrencyBean currencyBean =CommerceDaoFactory.getCurrencyDao().getCurrency(currencyPrecisionBean.getCurrencyId());
                TreeNodeBase currencyPrecisionNode = new TreeNodeBase(
                    "currency-precision",
                    currencyBean.getCurrencyName()+", " +currencyBean.getCurrencyCode() + " ["+ currencyPrecisionBean.getPrecision()+" digits]",
                    currencyPrecisionBean.getCurrencyPrecisionId().toString(),
                    false);
                shopNode.getChildren().add(currencyPrecisionNode);
            }
        }

        return treeRoot;
    }
}

