/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.portlet.forum;

import java.util.Calendar;
import java.util.List;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.common.TreeItemInterface;
import org.riverock.portlet.core.GetMainForumThreadsItem;
import org.riverock.portlet.schema.core.MainForumThreadsItemType;

import org.apache.log4j.Logger;

public final class ForumMessage implements TreeItemInterface {
    private final static Logger log = Logger.getLogger( ForumMessage.class );

    private String text = "";
//    private String ip = "";

    public Calendar getDatePost() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( item.getDatePost().getTime());
        return calendar;
    }

    public String getHeader() {
        if (item.getHeader()==null || item.getHeader().trim().length()==0)
            return "___";

        return item.getHeader();
    }

    public String getEmail() {
        return item.getEmail()==null?"":item.getEmail();
    }

    public String getFio() {
        return item.getFio()==null?"":item.getFio();
    }

    public String getText() {
        return text==null?"":text;
    }

    private MainForumThreadsItemType item = null;

    public List getSubMessageTree() {
        return subMessageTree;
    }

    public void setSubMessageTree(List subMessageTree) {
        this.subMessageTree = subMessageTree;
    }

    private List subMessageTree = null;

    public ForumMessage(DatabaseAdapter db__, MainForumThreadsItemType item)
        throws ForumException {
        this.item = item;
        try {
            this.text = DatabaseManager.getBigTextField(db__, item.getId(), "MESSAGE_TEXT", "MAIN_FORUM_TEXT", "ID", "ID_MAIN_FORUM_TEXT");
        }
        catch( Exception e ) {
            String es = "Error create FirumMessage";
            log.error( es, e );
            throw new ForumException( es, e );
        }
    }

    public Long getTopId() {
        return item.getIdMain();
    }

    public Long getId() {
        return item.getId();
    }

    public List getSubTree() {
        return subMessageTree;
    }

    public void setSubTree(List list) {
        subMessageTree = list;
    }

    public static ForumMessage getInstance(DatabaseAdapter db__, Long id_)
            throws ForumException
    {
        try
        {
            MainForumThreadsItemType item = GetMainForumThreadsItem.getInstance(db__, id_).item;
            if (item != null)
                return new ForumMessage(db__, item);
            else
                return null;
        }
        catch (Exception e) {
            log.error("Exception in SimpleForumMessage(...", e);
            throw new ForumException(e.getMessage());
        }
        finally{
        }
    }

}
