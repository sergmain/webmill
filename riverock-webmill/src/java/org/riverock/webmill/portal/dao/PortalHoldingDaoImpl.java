package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.dao.PortalHoldingDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalHoldingDaoImpl implements PortalHoldingDao {
    private AuthSession authSession = null;

    PortalHoldingDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Holding loadHolding(Long id) {
        return InternalDaoFactory.getInternalHoldingDao().loadHolding( id, authSession );
    }

    public List<Holding> getHoldingList() {
        return InternalDaoFactory.getInternalHoldingDao().getHoldingList( authSession );
    }

    public Long processAddHolding(Holding holdingBean ) {
        return InternalDaoFactory.getInternalHoldingDao().processAddHolding( holdingBean, authSession );
    }

    public void processSaveHolding(Holding holdingBean) {
        InternalDaoFactory.getInternalHoldingDao().processSaveHolding( holdingBean, authSession );
    }

    public void processDeleteHolding(Holding holdingBean) {
        InternalDaoFactory.getInternalHoldingDao().processDeleteHolding( holdingBean, authSession );
    }
}
