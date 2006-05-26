package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.webmill.core.GetWmListUserMetadataItem;
import org.riverock.webmill.core.InsertWmListUserMetadataItem;
import org.riverock.webmill.core.UpdateWmListUserMetadataItem;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.portal.bean.UserMetadataItemBean;
import org.riverock.webmill.schema.core.WmListUserMetadataItemType;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 20:01:52
 */
@SuppressWarnings({"UnusedAssignment"})
public class InternalUserMetadataDaoImpl implements InternalUserMetadataDao {
    private final static Logger log = Logger.getLogger(InternalUserMetadataDaoImpl.class);

    private final static String sql =
        "select a.* " +
            "from   WM_LIST_USER_METADATA a, WM_AUTH_USER c " +
            "where  a.ID_SITE=? and a.ID_USER=c.ID_USER and c.USER_LOGIN=? and META=?";

    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            return getMetadata(adapter, userLogin, siteId, metadataName);
        } catch (Exception e) {
            String es = "Error get metadata from DB";
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public UserMetadataItem getMetadata(DatabaseAdapter adapter, String userLogin, Long siteId, String metadataName) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement(sql);
            ps.setLong(1, siteId);
            ps.setString(2, userLogin);
            ps.setString(3, metadataName);

            rs = ps.executeQuery();
            if (rs.next()) {
                WmListUserMetadataItemType meta = GetWmListUserMetadataItem.fillBean(rs);
                UserMetadataItemBean bean = new UserMetadataItemBean();
                bean.setMetadataItemId(meta.getIdMainUserMetadata());
                bean.setSiteId(meta.getIdSite());
                bean.setUserId(meta.getIdUser());
                bean.setMetadataName(meta.getMeta());
                bean.setMetadataName(meta.getMeta());
                bean.setIntValue(meta.getIntValue());
                bean.setStringValue(meta.getStringValue());
                bean.setDateValue(meta.getDateValue());

                return bean;
            }
            return null;
        } catch (Exception e) {
            String es = "Error get metadata from DB";
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue) {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            UserInfo userInfo =  InternalDaoFactory.getInternalAuthDao().getUserInfo(adapter, userLogin);
            if (userInfo==null) {
                throw new IllegalStateException("User with login: " +userLogin+", not exists");
            }
            UserMetadataItem meta = getMetadata(adapter, userLogin, siteId, metadataName);
            if (meta == null) {
                createNewMetadataItem(adapter, siteId, metadataName, intValue, null, null, userInfo);
            } else {
                WmListUserMetadataItemType item = new WmListUserMetadataItemType();
                item.setIdMainUserMetadata(meta.getMetadataItemId());
                item.setIdSite(meta.getSiteId());
                item.setIdUser(meta.getUserId());
                item.setMeta(meta.getMetadataName());
                item.setIntValue(intValue);
                item.setStringValue(null);
                item.setDateValue(null);
                UpdateWmListUserMetadataItem.process(adapter, item);
            }
            adapter.commit();
        } catch (Exception e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error get metadata from DB";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue) {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            UserInfo userInfo =  InternalDaoFactory.getInternalAuthDao().getUserInfo(adapter, userLogin);
            if (userInfo==null) {
                throw new IllegalStateException("User with login: " +userLogin+", not exists");
            }
            UserMetadataItem meta = getMetadata(adapter, userLogin, siteId, metadataName);
            if (meta == null) {
                createNewMetadataItem(adapter, siteId, metadataName, null, stringValue, null, userInfo);
            } else {
                WmListUserMetadataItemType item = new WmListUserMetadataItemType();
                item.setIdMainUserMetadata(meta.getMetadataItemId());
                item.setIdSite(meta.getSiteId());
                item.setIdUser(meta.getUserId());
                item.setMeta(meta.getMetadataName());
                item.setIntValue(null);
                item.setStringValue(stringValue);
                item.setDateValue(null);
                UpdateWmListUserMetadataItem.process(adapter, item);
            }
            adapter.commit();
        } catch (Exception e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error get metadata from DB";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue) {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            UserInfo userInfo =  InternalDaoFactory.getInternalAuthDao().getUserInfo(adapter, userLogin);
            if (userInfo==null) {
                throw new IllegalStateException("User with login: " +userLogin+", not exists");
            }

            UserMetadataItem meta = getMetadata(adapter, userLogin, siteId, metadataName);
            if (meta == null) {
                createNewMetadataItem(adapter, siteId, metadataName, null, null, dateValue, userInfo);
            } else {
                WmListUserMetadataItemType item = new WmListUserMetadataItemType();
                item.setIdMainUserMetadata(meta.getMetadataItemId());
                item.setIdSite(meta.getSiteId());
                item.setIdUser(meta.getUserId());
                item.setMeta(meta.getMetadataName());
                item.setIntValue(null);
                item.setStringValue(null);
                item.setDateValue(dateValue);
                UpdateWmListUserMetadataItem.process(adapter, item);
            }
            adapter.commit();
        } catch (Exception e) {
            try {
                if (adapter != null)
                    adapter.rollback();
            }
            catch (Throwable th) {
                // catch rollback error
            }
            String es = "Error get metadata from DB";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    private void createNewMetadataItem(DatabaseAdapter adapter, Long siteId, String metadataName, Long intValue, String stringValue, Date dateValue, UserInfo userInfo) throws PortalPersistenceException, SQLException {

        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName("seq_WM_LIST_USER_METADATA");
        seq.setTableName("WM_LIST_USER_METADATA");
        seq.setColumnName("ID_MAIN_USER_METADATA");
        Long id = adapter.getSequenceNextValue(seq);

        WmListUserMetadataItemType item = new WmListUserMetadataItemType();
        item.setIdMainUserMetadata(id);
        item.setIdSite(siteId);
        item.setIdUser( userInfo.getUserId() );
        item.setMeta(metadataName);
        item.setIntValue(intValue);
        item.setStringValue(stringValue);
        item.setDateValue(dateValue);
        InsertWmListUserMetadataItem.process(adapter, item);
    }
}
