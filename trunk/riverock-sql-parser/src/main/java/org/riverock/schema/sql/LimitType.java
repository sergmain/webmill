/*
 * org.riverock.sql - Classes for tracking database changes
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

/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.schema.sql;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class LimitType.
 * 
 * @version $Revision$ $Date$
 */
public class LimitType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _start
     */
    private int _start;

    /**
     * keeps track of state for field: _start
     */
    private boolean _has_start;

    /**
     * Field _count
     */
    private int _count;

    /**
     * keeps track of state for field: _count
     */
    private boolean _has_count;


      //----------------/
     //- Constructors -/
    //----------------/

    public LimitType() {
        super();
    } //-- org.riverock.schema.sql.LimitType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getCountReturns the value of field 'count'.
     * 
     * @return the value of field 'count'.
     */
    public int getCount()
    {
        return this._count;
    } //-- int getCount() 

    /**
     * Method getStartReturns the value of field 'start'.
     * 
     * @return the value of field 'start'.
     */
    public int getStart()
    {
        return this._start;
    } //-- int getStart() 

    /**
     * Method hasCount
     */
    public boolean hasCount()
    {
        return this._has_count;
    } //-- boolean hasCount() 

    /**
     * Method hasStart
     */
    public boolean hasStart()
    {
        return this._has_start;
    } //-- boolean hasStart() 

    /**
     * Method setCountSets the value of field 'count'.
     * 
     * @param count the value of field 'count'.
     */
    public void setCount(int count)
    {
        this._count = count;
        this._has_count = true;
    } //-- void setCount(int) 

    /**
     * Method setStartSets the value of field 'start'.
     * 
     * @param start the value of field 'start'.
     */
    public void setStart(int start)
    {
        this._start = start;
        this._has_start = true;
    } //-- void setStart(int) 

}
