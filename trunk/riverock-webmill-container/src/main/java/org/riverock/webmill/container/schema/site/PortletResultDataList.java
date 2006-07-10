package org.riverock.webmill.container.schema.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Class SitePortletDataListType.
 *
 * @version $Revision$ $Date$
 */
public class PortletResultDataList {

    /**
     * Field portletResultDatas
     */
    private List<PortletResultData> portletResultDatas;

    public PortletResultDataList() {
        super();
        portletResultDatas = new ArrayList<PortletResultData>();
    }

    /**
     * Method addPortlet
     *
     * @param vPortletResult
     */
    public void addPortletResultData(PortletResultData vPortletResult) {
        portletResultDatas.add(vPortletResult);
    }

    /**
     * Method setPortlet
     * <p/>
     * Sets the value of 'portlet' by copying the given ArrayList.
     *
     * @param portletCollection the ArrayList to copy.
     */
    public void setPortletResultDatas(List<PortletResultData> portletCollection) {
        portletResultDatas.clear();
        portletResultDatas.addAll(portletCollection);
    }
}
