/*
 * org.riverock.sql -- Classes for tracking database changes
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
package org.riverock.sql.cache;

/**
 * User: SergeMaslyukov
 * Date: 27.01.2005
 * Time: 12:40:45
 * $Id$
 */
public class SqlStatementRegisterException extends RuntimeException {

    public SqlStatementRegisterException(){
        super();
    }

    public SqlStatementRegisterException(String s){
        super(s);
    }

    public SqlStatementRegisterException(String s, Throwable cause){
        super(s, cause);
    }
}