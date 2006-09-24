/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.member;

/**
 * User: smaslyukov
 * Date: 30.07.2004
 * Time: 19:56:26
 * $Id$
 */
public class MemberException extends Exception{
    public MemberException(){
        super();
    }

    public MemberException(String s){
        super(s);
    }

    public MemberException(String s, Throwable cause){
        super(s, cause);
    }

    public String toString(){
        return super.toString();
    }

    public String getMessage(){
        return super.getMessage();
    }
}
