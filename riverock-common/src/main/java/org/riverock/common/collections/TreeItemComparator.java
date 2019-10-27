package org.riverock.common.collections;

import java.util.Comparator;
import java.io.Serializable;

import org.riverock.interfaces.common.TreeItem;

/**
 * User: SMaslyukov
* Date: 16.08.2007
* Time: 11:48:40
*/
class TreeItemComparator implements Comparator<TreeItem>, Serializable {
    
    public int compare(TreeItem item1, TreeItem item2) {

        if (item1 ==null && item2 ==null)
            return 0;

        if (item1 ==null)
            return 1;

        if (item2 ==null)
            return -1;

        if ( item1.getId() > item2.getId())
            return 1;
        else if ( item1.getId() < item2.getId())
            return -1;

        // Here item1.getId().equals(item2.getId())

        if ( item1.getTopId()==null && item2.getTopId()==null)
            return 0;

        if ( item1.getTopId()!=null && item2.getTopId()==null )
            return -1;

        if ( item1.getTopId()==null && item2.getTopId()!=null)
            return 1;


        if ( item1.getTopId().equals( item2.getTopId()))
            return 0;

        if ( item1.getTopId() > item2.getTopId())
            return 1;
        else
            return -1;
    }
}
