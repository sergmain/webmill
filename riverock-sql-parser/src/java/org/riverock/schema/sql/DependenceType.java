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

