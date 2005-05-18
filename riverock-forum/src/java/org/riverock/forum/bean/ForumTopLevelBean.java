package org.riverock.forum.bean;

import java.util.List;
import java.util.ArrayList;

/**
 * @author SMaslyukov
 *         Date: 06.05.2005
 *         Time: 18:32:41
 *         $Id$
 */
public class ForumTopLevelBean {
    private List forums = new ArrayList();

    public List getForums() {
        return forums;
    }

    public void setForums(List forums) {
        this.forums = forums;
    }
}
