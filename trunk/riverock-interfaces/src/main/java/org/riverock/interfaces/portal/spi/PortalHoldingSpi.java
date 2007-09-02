package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.dao.PortalHoldingDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:40:18
 * $Id$
 */
public interface PortalHoldingSpi extends PortalHoldingDao {
    public Holding loadHolding( Long id );
    public List<Holding> getHoldingList();
    public Long processAddHolding( Holding holdingBean );
    public void processSaveHolding( Holding holdingBean );
    public void processDeleteHolding( Holding holdingBean );
}
