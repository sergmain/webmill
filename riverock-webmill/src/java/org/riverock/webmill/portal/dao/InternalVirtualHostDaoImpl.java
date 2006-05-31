package org.riverock.webmill.portal.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.webmill.core.GetWmPortalVirtualHostWithIdSiteList;
import org.riverock.webmill.core.InsertWmPortalVirtualHostItem;
import org.riverock.webmill.core.GetWmPortalVirtualHostFullList;
import org.riverock.webmill.portal.bean.VirtualHostBean;
import org.riverock.webmill.schema.core.WmPortalVirtualHostItemType;
import org.riverock.webmill.schema.core.WmPortalVirtualHostListType;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:54:30
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalVirtualHostDaoImpl implements InternalVirtualHostDao {
    private final static Logger log = Logger.getLogger(InternalVirtualHostDaoImpl.class);

    public List<VirtualHost> getVirtualHostsFullList() {
        DatabaseAdapter adapter = null;

        List<VirtualHost> virtualHosts = new ArrayList<VirtualHost>();
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalVirtualHostListType hosts = GetWmPortalVirtualHostFullList.getInstance(adapter, 0).item;
            for (Object o : hosts.getWmPortalVirtualHostAsReference()) {
                WmPortalVirtualHostItemType host = (WmPortalVirtualHostItemType) o;
                virtualHosts.add(
                    new VirtualHostBean(host.getIdSiteVirtualHost(), host.getIdSite(), host.getNameVirtualHost())
                );
            }
        } catch (Exception e) {
            String es = "Error get full list of virtual hosts";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
        return virtualHosts;
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        if (siteId == null) {
            throw new IllegalStateException("getVirtualHost(), siteId is null");
        }

        DatabaseAdapter adapter = null;

        List<VirtualHost> virtualHosts = new ArrayList<VirtualHost>();
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalVirtualHostListType hosts = GetWmPortalVirtualHostWithIdSiteList.getInstance(adapter, siteId).item;
            for (Object o : hosts.getWmPortalVirtualHostAsReference()) {
                WmPortalVirtualHostItemType host = (WmPortalVirtualHostItemType) o;
                virtualHosts.add(
                    new VirtualHostBean(host.getIdSiteVirtualHost(), host.getIdSite(), host.getNameVirtualHost())
                );
            }
        } catch (Exception e) {
            String es = "Error get list of virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
        return virtualHosts;
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            Long id = createVirtualHost(adapter, virtualHost);

            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error create virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public Long createVirtualHost(DatabaseAdapter adapter, VirtualHost host) {

        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_VIRTUAL_HOST");
            seq.setTableName("WM_PORTAL_VIRTUAL_HOST");
            seq.setColumnName("ID_SITE_VIRTUAL_HOST");
            Long siteId = adapter.getSequenceNextValue(seq);

            WmPortalVirtualHostItemType item = new WmPortalVirtualHostItemType();
            item.setIdSiteVirtualHost(siteId);
            item.setIdSite(host.getSiteId());
            item.setNameVirtualHost(host.getHost());

            InsertWmPortalVirtualHostItem.process(adapter, item);
            return siteId;
        } catch (Throwable e) {
            String es = "Error create virtual host";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    public void deleteVirtualHost(DatabaseAdapter adapter, Long siteId) {

        try {
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_PORTAL_VIRTUAL_HOST where ID_SITE=?",
                new Object[]{siteId}, new int[]{Types.DECIMAL}
            );

        } catch (SQLException e) {
            String es = "Error delete virtual host for site";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

}
