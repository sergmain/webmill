package org.riverock.portlet.webclip;

import org.riverock.interfaces.portal.search.PortletIndexer;
import org.riverock.portlet.dao.PortletDaoFactory;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 18:53:12
 */
public class WebclipIndexer implements PortletIndexer {
    private Object id;
    private Long siteId;
    private ClassLoader classLoader;

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Object getId() {
        return id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void init(Object id, Long siteId, ClassLoader classLoader) {
        this.id = id;
        this.siteId = siteId;
        this.classLoader = classLoader;
    }

    public long getNotIndexedCount() {
        return PortletDaoFactory.getWebclipDao().getNotIndexedCount(siteId);
    }

    public long getTotal() {
        return PortletDaoFactory.getWebclipDao().getTotalCount(siteId);
    }

    public String getContent(Long objectId) {
        WebclipBean webclipBean = PortletDaoFactory.getWebclipDao().getWebclip(siteId, objectId);
        if (webclipBean!=null) {
            return webclipBean.getWebclipData();
        }
        return null;
    }

    public void markAsIndexed(Long objectId) {
        PortletDaoFactory.getWebclipDao().markAsIndexed(siteId, objectId);
    }

    public void markAllForIndexing() {
        PortletDaoFactory.getWebclipDao().markAllForIndexing(siteId);
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return getNotIndexedCount()!=0;
    }

    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws java.util.NoSuchElementException
     *          iteration has no more elements.
     */
    public Long next() {
        WebclipBean webclip = PortletDaoFactory.getWebclipDao().getFirstForIndexing(siteId);
        if (webclip!=null) {
            return webclip.getWebclipId();
        }
        return null;
    }

    /**
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt>
     *                                       operation is not supported by this Iterator.
     * @throws IllegalStateException         if the <tt>next</tt> method has not
     *                                       yet been called, or the <tt>remove</tt> method has already
     *                                       been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove() {
    }
}
