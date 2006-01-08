package org.riverock.portlet.auth.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.core.GetWmListCompanyFullList;
import org.riverock.portlet.core.GetWmListHoldingFullList;
import org.riverock.portlet.core.GetWmListGroupCompanyFullList;
import org.riverock.portlet.schema.core.WmListCompanyItemType;
import org.riverock.portlet.schema.core.WmListCompanyListType;
import org.riverock.portlet.schema.core.WmListHoldingItemType;
import org.riverock.portlet.schema.core.WmListHoldingListType;
import org.riverock.portlet.schema.core.WmListGroupCompanyListType;
import org.riverock.portlet.schema.core.WmListGroupCompanyItemType;

public class ListProvider {
    private final static Logger log = Logger.getLogger( ListProvider.class );

    public List<CompanyItemBean> getCompanyList() {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            WmListCompanyListType groupCompanyList = GetWmListCompanyFullList.getInstance( db, 0 ).item;
            List<CompanyItemBean> list = new ArrayList<CompanyItemBean>();
            Iterator iterator = groupCompanyList.getWmListCompanyAsReference().iterator();
            while( iterator.hasNext() ) {
                WmListCompanyItemType item = ( WmListCompanyItemType ) iterator.next();
                CompanyItemBean bean = new CompanyItemBean();
		if (item.getShortName()!=null && item.getIdFirm()!=null) {
                	bean.setName( item.getShortName() );
                	bean.setId( item.getIdFirm() );
                	list.add( bean );
		}
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }

    public List<GroupCompanyItemBean> getGroupCompanyList() {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            WmListGroupCompanyListType groupCompanyList = GetWmListGroupCompanyFullList.getInstance( db, 0).item;
            List<GroupCompanyItemBean> list = new ArrayList<GroupCompanyItemBean>();
            Iterator iterator = groupCompanyList.getWmListGroupCompanyAsReference().iterator();
            while( iterator.hasNext() ) {
                WmListGroupCompanyItemType item = ( WmListGroupCompanyItemType ) iterator.next();
                GroupCompanyItemBean bean = new GroupCompanyItemBean();
                bean.setName( item.getShortNameService() );
                bean.setId( item.getIdService() );
                list.add( bean );
            }
            return list;
        }
        catch(Exception e) {
            String es = "error";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }

    public List<HoldingItemBean> getHoldingList() {

        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            WmListHoldingListType holdingList = GetWmListHoldingFullList.getInstance( db, 0 ).item;
            List<HoldingItemBean> list = new ArrayList<HoldingItemBean>();
            Iterator iterator = holdingList.getWmListHoldingAsReference().iterator();
            while( iterator.hasNext() ) {
                WmListHoldingItemType item = ( WmListHoldingItemType ) iterator.next();
                HoldingItemBean bean = new HoldingItemBean();
                bean.setName( item.getNameRoad() );
                bean.setId( item.getIdRoad() );
                list.add( bean );
            }
            return list;
        }
        catch( Exception e ) {
            String es = "error";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db );
            db = null;
        }
    }


}