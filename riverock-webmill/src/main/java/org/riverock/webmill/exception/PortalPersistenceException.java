/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.exception;

/**
 * User: serg_main
 * Date: 11.05.2004
 * Time: 17:46:28
 * @author Serge Maslyukov
 * $Id$
 */

public class PortalPersistenceException extends Exception{

    public PortalPersistenceException(){
        super();
    }

    public PortalPersistenceException(String s){
        super(s);
    }

    public PortalPersistenceException(String s, Throwable cause){
        super(s, cause);
    }

    public String toString(){
        return super.toString();
    }

    public String getMessage(){
        return super.getMessage();
    }
}

