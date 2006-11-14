/*
 * org.riverock.commerce - Commerce application
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

