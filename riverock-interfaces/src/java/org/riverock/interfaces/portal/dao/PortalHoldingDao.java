package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:48:52
 *         $Id$
 */
public interface PortalHoldingDao {
    public Holding loadHolding( Long id );
    public List<Holding> getHoldingList();
    public Long processAddHolding( Holding holdingBean );
    public void processSaveHolding( Holding holdingBean );
    public void processDeleteHolding( Holding holdingBean );
}
