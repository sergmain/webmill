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



package org.riverock.portlet.price;



import java.io.CharArrayWriter;

import java.io.FileReader;

import java.util.Vector;



import org.apache.log4j.Logger;

import org.xml.sax.Attributes;

import org.xml.sax.InputSource;

import org.xml.sax.SAXException;

import org.xml.sax.XMLReader;

import org.xml.sax.helpers.DefaultHandler;



/**

 *

 *  $Id$

 *

 */



public class ProcessStorageOrders extends DefaultHandler

{



    private static Logger cat = Logger.getLogger( ProcessStorageOrders.class  );



    // Local list of order items...

    private Vector storageOrderItems = new Vector();

    private Vector storageOrders = new Vector();

    // Local current order item reference...

    private StorageOrder currentStorageOrder;

    private StorageOrderItem currentStorageOrderItem;

    // Buffer for collecting data from

    // the "characters" SAX event.

    private CharArrayWriter contents = new CharArrayWriter();





    private static final String

            DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";



    private static boolean setValidation = false; //defaults

    private static boolean setNameSpaces = true;

    private static boolean setSchemaSupport = true;

    private static boolean setSchemaFullSupport = false;



    // Override methods of the DefaultHandler class

    // to gain notification of SAX Events.

    //

    // See org.xml.sax.ContentHandler for all available events.

    //





    public void startElement(String namespaceURI,

                             String localName,

                             String qName,

                             Attributes attr) throws SAXException

    {



        contents.reset();



        // New twist...

        if (localName.equals("Order"))

        {

            currentStorageOrder = new StorageOrder();

            storageOrders.addElement(currentStorageOrder);

        }



        if (localName.equals("Item"))

        {

            currentStorageOrderItem = new StorageOrderItem();



            currentStorageOrder.items.addElement(currentStorageOrderItem);

        }



        for (int i = 0; i < attr.getLength(); i++)

        {

            cat.debug(" ATTRIBUTE: " +

                    attr.getLocalName(i) +

                    " VALUE: " +

                    attr.getValue(i));

        }



    }



    public void endElement(String namespaceURI,

                           String localName,

                           String qName) throws SAXException

    {



        if (localName.equals("DateOrder"))

        {

            currentStorageOrder.dateOrder =

                    contents.toString().trim();

        }



//        if (localName.equals("NumberOrder"))

//        {

//            try

//            {

//                String a = StringTools.convertString(

//                        contents.toString(),

//                        InitParam.getStreamCharset(), "Cp1251"

//                );

//

//            }

//            catch (java.io.UnsupportedEncodingException e)

//            {

//            }

//            currentStorageOrder.numberOrder =

//                    contents.toString().trim();

//        }



        if (localName.equals("OrderID"))

        {

            currentStorageOrder.orderID =

                    Long.valueOf(contents.toString().trim()).longValue();

        }



        if (localName.equals("ItemID"))

        {

            currentStorageOrderItem.itemID =

                    Long.valueOf(contents.toString().trim());

        }



        if (localName.equals("Quantity"))

        {

            currentStorageOrderItem.quantity =

                    Long.valueOf(contents.toString().trim()).longValue();

        }



    }



    public void characters(char[] ch, int start, int length)

            throws SAXException

    {



        contents.write(ch, start, length);



    }



    public Vector getStorageOrders()

    {

        return storageOrders;

    }





    public static Vector parseStorageOrdersFile(String xmlFile)

            throws PriceException

    {



        Vector retVector = null;

        try

        {

            DefaultHandler handler = new ProcessStorageOrders();



            XMLReader xr = (XMLReader) Class.forName(DEFAULT_PARSER_NAME).newInstance();



            xr.setFeature("http://xml.org/sax/features/validation",

                    setValidation);

            xr.setFeature("http://xml.org/sax/features/namespaces",

                    setNameSpaces);

/*

            xr.setFeature(

"http://apache.org/xml/features/validation/schema",

                                                setSchemaSupport );

            xr.setFeature(

"http://apache.org/xml/features/validation/schema-full-checking",

                                                setSchemaFullSupport );

*/



            // Create SAX 2 parser...

//         XMLReader xr = XMLReaderFactory.createXMLReader();



            // Set the ContentHandler...

            ProcessStorageOrders ex4 = new ProcessStorageOrders();

            xr.setContentHandler(ex4);



            // Parse the file...

            xr.parse(new InputSource(

                    new FileReader(xmlFile)));



            retVector = ex4.getStorageOrders();

        }

        catch (Exception e)

        {

            throw new PriceException(e.toString());

        }



        return retVector;





    }



}



