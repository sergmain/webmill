/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.generic.port;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.io.IOException;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.tools.StringManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.interfaces.generic.LocalizedStringInterface;

import org.apache.log4j.Logger;

/**
 *
 *  $Id$
 *
 */
public class LocalizedString implements LocalizedStringInterface {
    private final static Logger log = Logger.getLogger( LocalizedString.class );

    private boolean isProp = false;	// is use Properties
    private String value = null;
    private String storage = null;

    protected void finalize() throws Throwable{
        value = null;
        storage = null;

        super.finalize();
    }

    public LocalizedString(boolean isProperty, String value, String storage){
        this.isProp = isProperty;
        this.value = value;
        this.storage = storage;
    }

    public LocalizedString(ResultSet rs) throws GenericException {
        try {
            Integer i = RsetTools.getInt(rs, "IS_USE_PROPERTIES");
            isProp = (i!=null?i.intValue()==1:false);
            value = RsetTools.getString(rs, "KEY_MESSAGE");
            storage = RsetTools.getString(rs, "STORAGE");
        } catch (SQLException e) {
            throw new GenericException("Error create LocalizedString", e);
        }
    }

    public String getString(Locale loc) throws IOException {
        if (isProp){
            StringManager strMan = StringManager.getManager(storage, loc);

            try {
                return strMan.getStr(value);
            }
            catch( GenericException e ) {
                String es = "Error get localized string";
                log.error( es, e );
                IOException ioException = new IOException( es );
                ioException.initCause( e );
                throw ioException;
            }
        }
        else{
            return value;
        }
    }
}