package org.riverock.portlet.cms.dao;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 16:27:57
 */
public class CmsDaoFactory {
    private final static CmsNewsDao cmsNewsDao = new CmsNewsDaoImpl();

    public static CmsNewsDao getCmsNewsDao() {
        return cmsNewsDao;
    }
}
