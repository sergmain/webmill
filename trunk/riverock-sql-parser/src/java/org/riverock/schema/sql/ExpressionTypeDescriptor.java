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



import org.exolab.castor.mapping.AccessMode;

import org.exolab.castor.xml.TypeValidator;

import org.exolab.castor.xml.XMLFieldDescriptor;

import org.exolab.castor.xml.validators.*;



/**

 * Class ExpressionTypeDescriptor.

 * 

 * @version $Revision$ $Date$

 */

public class ExpressionTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {





      //--------------------------/

     //- Class/Member Variables -/

    //--------------------------/



    /**

     * Field nsPrefix

     */

    private java.lang.String nsPrefix;



    /**

     * Field nsURI

     */

    private java.lang.String nsURI;



    /**

     * Field xmlName

     */

    private java.lang.String xmlName;



    /**

     * Field identity

     */

    private org.exolab.castor.xml.XMLFieldDescriptor identity;





      //----------------/

     //- Constructors -/

    //----------------/



    public ExpressionTypeDescriptor() {

        super();

        xmlName = "ExpressionType";

        

        //-- set grouping compositor

        setCompositorAsSequence();

        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;

        org.exolab.castor.xml.XMLFieldHandler              handler        = null;

        org.exolab.castor.xml.FieldValidator               fieldValidator = null;

        //-- initialize attribute descriptors

        

        //-- _objectData

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_objectData", "ObjectData", org.exolab.castor.xml.NodeType.Attribute);

        desc.setImmutable(true);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getObjectData();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setObjectData( (java.lang.String) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        addFieldDescriptor(desc);

        

        //-- validation code for: _objectData

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            StringValidator typeValidator = new StringValidator();

            typeValidator.setWhiteSpace("preserve");

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- initialize element descriptors

        

        //-- _type

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_type", "type", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasType())

                    return null;

                return new Integer(target.getType());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteType();

                        return;

                    }

                    target.setType( ((Integer)value).intValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _type

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            IntegerValidator typeValidator= new IntegerValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _expArg1

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.ExpressionType.class, "_expArg1", "ExpArg1", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getExpArg1();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setExpArg1( (org.riverock.schema.sql.ExpressionType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return new org.riverock.schema.sql.ExpressionType();

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _expArg1

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _expArg2

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.ExpressionType.class, "_expArg2", "ExpArg2", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getExpArg2();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setExpArg2( (org.riverock.schema.sql.ExpressionType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return new org.riverock.schema.sql.ExpressionType();

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _expArg2

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _aggregateSpec

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.types.AggregateSpecType.class, "_aggregateSpec", "AggregateSpec", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getAggregateSpec();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setAggregateSpec( (org.riverock.schema.sql.types.AggregateSpecType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler( new org.exolab.castor.xml.handlers.EnumFieldHandler(org.riverock.schema.sql.types.AggregateSpecType.class, handler));

        desc.setImmutable(true);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _aggregateSpec

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _valueListList

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.ValueItemType.class, "_valueListList", "ValueList", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getValueList();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.addValueList( (org.riverock.schema.sql.ValueItemType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return new org.riverock.schema.sql.ValueItemType();

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(true);

        addFieldDescriptor(desc);

        

        //-- validation code for: _valueListList

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        fieldValidator.setMinOccurs(0);

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _isValueListHasNull

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_isValueListHasNull", "IsValueListHasNull", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasIsValueListHasNull())

                    return null;

                return new Boolean(target.getIsValueListHasNull());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteIsValueListHasNull();

                        return;

                    }

                    target.setIsValueListHasNull( ((Boolean)value).booleanValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _isValueListHasNull

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            BooleanValidator typeValidator = new BooleanValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _dataType

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_dataType", "DataType", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasDataType())

                    return null;

                return new Integer(target.getDataType());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteDataType();

                        return;

                    }

                    target.setDataType( ((Integer)value).intValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _dataType

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            IntegerValidator typeValidator= new IntegerValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _select

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.SelectType.class, "_select", "Select", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getSelect();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setSelect( (org.riverock.schema.sql.SelectType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return new org.riverock.schema.sql.SelectType();

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _select

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _charLikeEscape

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_charLikeEscape", "CharLikeEscape", org.exolab.castor.xml.NodeType.Element);

        desc.setImmutable(true);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getCharLikeEscape();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setCharLikeEscape( (java.lang.String) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _charLikeEscape

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            StringValidator typeValidator = new StringValidator();

            typeValidator.setWhiteSpace("preserve");

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _table

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_table", "Table", org.exolab.castor.xml.NodeType.Element);

        desc.setImmutable(true);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getTable();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setTable( (java.lang.String) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _table

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            StringValidator typeValidator = new StringValidator();

            typeValidator.setWhiteSpace("preserve");

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _column

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_column", "Column", org.exolab.castor.xml.NodeType.Element);

        desc.setImmutable(true);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getColumn();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setColumn( (java.lang.String) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _column

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            StringValidator typeValidator = new StringValidator();

            typeValidator.setWhiteSpace("preserve");

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _filter

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.riverock.schema.sql.TableFilterType.class, "_filter", "Filter", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getFilter();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setFilter( (org.riverock.schema.sql.TableFilterType) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return new org.riverock.schema.sql.TableFilterType();

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _filter

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

        }

        desc.setValidator(fieldValidator);

        //-- _columnNumber

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_columnNumber", "ColumnNumber", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasColumnNumber())

                    return null;

                return new Integer(target.getColumnNumber());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteColumnNumber();

                        return;

                    }

                    target.setColumnNumber( ((Integer)value).intValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _columnNumber

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            IntegerValidator typeValidator= new IntegerValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _columnSize

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_columnSize", "ColumnSize", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasColumnSize())

                    return null;

                return new Integer(target.getColumnSize());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteColumnSize();

                        return;

                    }

                    target.setColumnSize( ((Integer)value).intValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _columnSize

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            IntegerValidator typeValidator= new IntegerValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _columnScale

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_columnScale", "ColumnScale", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasColumnScale())

                    return null;

                return new Integer(target.getColumnScale());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteColumnScale();

                        return;

                    }

                    target.setColumnScale( ((Integer)value).intValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _columnScale

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            IntegerValidator typeValidator= new IntegerValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _alias

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_alias", "Alias", org.exolab.castor.xml.NodeType.Element);

        desc.setImmutable(true);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                return target.getAlias();

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    target.setAlias( (java.lang.String) value);

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _alias

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            StringValidator typeValidator = new StringValidator();

            typeValidator.setWhiteSpace("preserve");

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _isDescending

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_isDescending", "IsDescending", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasIsDescending())

                    return null;

                return new Boolean(target.getIsDescending());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteIsDescending();

                        return;

                    }

                    target.setIsDescending( ((Boolean)value).booleanValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _isDescending

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            BooleanValidator typeValidator = new BooleanValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

        //-- _isDistinctAggregate

        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_isDistinctAggregate", "IsDistinctAggregate", org.exolab.castor.xml.NodeType.Element);

        handler = (new org.exolab.castor.xml.XMLFieldHandler() {

            public java.lang.Object getValue( java.lang.Object object ) 

                throws IllegalStateException

            {

                ExpressionType target = (ExpressionType) object;

                if(!target.hasIsDistinctAggregate())

                    return null;

                return new Boolean(target.getIsDistinctAggregate());

            }

            public void setValue( java.lang.Object object, java.lang.Object value) 

                throws IllegalStateException, IllegalArgumentException

            {

                try {

                    ExpressionType target = (ExpressionType) object;

                    // if null, use delete method for optional primitives 

                    if (value == null) {

                        target.deleteIsDistinctAggregate();

                        return;

                    }

                    target.setIsDistinctAggregate( ((Boolean)value).booleanValue());

                }

                catch (java.lang.Exception ex) {

                    throw new IllegalStateException(ex.toString());

                }

            }

            public java.lang.Object newInstance( java.lang.Object parent ) {

                return null;

            }

        } );

        desc.setHandler(handler);

        desc.setMultivalued(false);

        addFieldDescriptor(desc);

        

        //-- validation code for: _isDistinctAggregate

        fieldValidator = new org.exolab.castor.xml.FieldValidator();

        { //-- local scope

            BooleanValidator typeValidator = new BooleanValidator();

            fieldValidator.setValidator(typeValidator);

        }

        desc.setValidator(fieldValidator);

    } //-- org.riverock.schema.sql.ExpressionTypeDescriptor()





      //-----------/

     //- Methods -/

    //-----------/



    /**

     * Method getAccessMode

     */

    public org.exolab.castor.mapping.AccessMode getAccessMode()

    {

        return null;

    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 



    /**

     * Method getExtends

     */

    public org.exolab.castor.mapping.ClassDescriptor getExtends()

    {

        return null;

    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 



    /**

     * Method getIdentity

     */

    public org.exolab.castor.mapping.FieldDescriptor getIdentity()

    {

        return identity;

    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 



    /**

     * Method getJavaClass

     */

    public java.lang.Class getJavaClass()

    {

        return org.riverock.schema.sql.ExpressionType.class;

    } //-- java.lang.Class getJavaClass() 



    /**

     * Method getNameSpacePrefix

     */

    public java.lang.String getNameSpacePrefix()

    {

        return nsPrefix;

    } //-- java.lang.String getNameSpacePrefix() 



    /**

     * Method getNameSpaceURI

     */

    public java.lang.String getNameSpaceURI()

    {

        return nsURI;

    } //-- java.lang.String getNameSpaceURI() 



    /**

     * Method getValidator

     */

    public org.exolab.castor.xml.TypeValidator getValidator()

    {

        return this;

    } //-- org.exolab.castor.xml.TypeValidator getValidator() 



    /**

     * Method getXMLName

     */

    public java.lang.String getXMLName()

    {

        return xmlName;

    } //-- java.lang.String getXMLName() 



}

