package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalHoldingDao {
    
    public Holding loadHolding( Long id, AuthSession authSession );
    public Long processAddHolding( Holding holdingBean, AuthSession authSession );
    public void processSaveHolding( Holding holdingBean, AuthSession authSession );
    public void processDeleteHolding( Holding holdingBean, AuthSession authSession );

    public List<Holding> getHoldingList( AuthSession authSession );

    public void setRelateHoldingCompany(DatabaseAdapter ora_, Long holdingId, Long companyId );
}
