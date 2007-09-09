package org.riverock.webmill.portal.dao;

import java.util.Observable;

/**
 * User: SergeMaslyukov
* Date: 10.09.2007
* Time: 0:51:55
* $Id$
*/
class ChangableObservable extends Observable {
    protected synchronized void setChanged() {
        super.setChanged();
    }

    public void notifyObservers() {
        super.setChanged();
        super.notifyObservers();
    }
}
