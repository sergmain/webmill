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
 * Class DependenceType.
 * 
 * @version $Revision$ $Date$
 */
public class DependenceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _source
     */
    private org.riverock.schema.sql.SqlNameListType _source;

    /**
     * Field _target
     */
    private org.riverock.schema.sql.SqlNameListType _target;


      //----------------/
     //- Constructors -/
    //----------------/

    public DependenceType() {
        super();
    } //-- org.riverock.schema.sql.DependenceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getSourceReturns the value of field 'source'.
     * 
     * @return the value of field 'source'.
     */
    public org.riverock.schema.sql.SqlNameListType getSource()
    {
        return this._source;
    } //-- org.riverock.schema.sql.SqlNameListType getSource() 

    /**
     * Method getTargetReturns the value of field 'target'.
     * 
     * @return the value of field 'target'.
     */
    public org.riverock.schema.sql.SqlNameListType getTarget()
    {
        return this._target;
    } //-- org.riverock.schema.sql.SqlNameListType getTarget() 

    /**
     * Method setSourceSets the value of field 'source'.
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(org.riverock.schema.sql.SqlNameListType source)
    {
        this._source = source;
    } //-- void setSource(org.riverock.schema.sql.SqlNameListType) 

    /**
     * Method setTargetSets the value of field 'target'.
     * 
     * @param target the value of field 'target'.
     */
    public void setTarget(org.riverock.schema.sql.SqlNameListType target)
    {
        this._target = target;
    } //-- void setTarget(org.riverock.schema.sql.SqlNameListType) 

}
