/*
 * org.riverock.webmill -- Portal framework implementation
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

package org.riverock.webmill.portlet;

import java.util.HashMap;
import java.util.List;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.log4j.Logger;

/**
 *
 * Author: Serg Malyukov
 * Date: Aug 23, 2002
 * Time: 2:48:27 PM
 *
 * $Id$
 *
 */
public class PortletList
{
    private static Logger cat = Logger.getLogger("org.riverock.webmill.portlet.PortletList" );

    private List portletDesc= null;
    private Boolean isInit = new Boolean( false);
    private HashMap portletDescHash = null;

    protected void finalize() throws Throwable
    {
        if (portletDesc != null)
        {
            portletDesc.clear();
            portletDesc = null;
        }

        if (portletDescHash != null)
        {
            portletDescHash.clear();
            portletDescHash = null;
        }

        isInit = null;

        super.finalize();
    }

    public PortletType getPortletDescription( String type )
    {
        if (cat.isDebugEnabled())
            cat.debug("Portlet type - " + type);

        synchronized(isInit)
        {
            if ( !isInit.booleanValue() )
            {
                if (portletDescHash != null)
                {
                    portletDescHash.clear();
                    portletDescHash = null;
                }

                if (cat.isDebugEnabled())
                {
                    cat.debug("portletDesc - " + portletDesc);
                    if (portletDesc != null)
                        cat.debug("portletDesc.size() - " + portletDesc.size());
                }

                if (portletDesc != null)
                {
                    portletDescHash = new HashMap( portletDesc.size() );
                    for (int i=0; i<portletDesc.size(); i++)
                    {
                        PortletType desc = (PortletType) portletDesc.get(i);

                        if (cat.isDebugEnabled())
                            cat.debug("PortletType - " + desc);

                        portletDescHash.put( desc.getPortletName().getContent(), desc);
                    }
                    isInit= null;
                    isInit = new Boolean(true);
                }
            }

            if (type == null || portletDesc== null)
                return null;

            return (PortletType)portletDescHash.get(type );
        }
    }

    public void setPortletDescription(List v_)
    {
        synchronized(isInit)
        {
            portletDesc= v_;
            isInit = null;
            isInit = new Boolean(false);

            if (portletDescHash != null)
            {
                portletDescHash.clear();
                portletDescHash = null;
            }
        }
    }

    public List getPortletDescription()
    {
        return portletDesc;
    }

}
