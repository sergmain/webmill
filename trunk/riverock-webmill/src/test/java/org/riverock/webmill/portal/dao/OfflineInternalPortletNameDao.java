package org.riverock.webmill.portal.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;

import org.xml.sax.SAXException;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.webmill.portal.bean.PortletNameBean;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 13:35:49
 */
public class OfflineInternalPortletNameDao implements InternalPortletNameDao {

    public List<PortletNameBean> portletNames = new ArrayList<PortletNameBean>();

    public static class PortletNames {
        public List<PortletNameBean> items = new ArrayList<PortletNameBean>();

        public void addItem(PortletNameBean item) {
            items.add(item);
        }

        public List<PortletNameBean> getItems() {
            return items;
        }

        public void setItems(List<PortletNameBean> items) {
            this.items = items;
        }
    }

    public OfflineInternalPortletNameDao() throws IOException, SAXException {
        loadPortletNames();
    }

    public PortletName getPortletName(Long portletId) {
        for (PortletNameBean portletName : portletNames) {
            if (portletName.getPortletId().equals(portletId)) {
                return portletName;
            }
        }
        return null;  
    }

    public PortletName getPortletName(String portletName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createPortletName(PortletName portletName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updatePortletName(PortletName portletNameBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deletePortletName(PortletName portletNameBean) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<PortletName> getPortletNameList() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void registerPortletName(String portletName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private void loadPortletNames() throws IOException, SAXException {
/*
	<row>
		<ID_SITE_CTX_TYPE>5</ID_SITE_CTX_TYPE>
		<TYPE>mill.menu</TYPE>
	</row>
*/
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("table", PortletNames.class);
        digester.addObjectCreate("table/row", PortletNameBean.class);
        digester.addSetNext("table/row", "addItem");
        digester.addBeanPropertySetter("table/row/ID_SITE_CTX_TYPE", "portletId");
        digester.addBeanPropertySetter("table/row/TYPE", "portletName");

        InputStream inputStream = OfflineInternalCatalogDao.class.getResourceAsStream("/xml/site/portlet-names.xml");

        PortletNames items = (PortletNames)digester.parse(inputStream);

        this.portletNames = items.getItems();
    }
}
