/*

 * org.riverock.cache -- Generic cache implementation

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



package org.riverock.test.cache;



import java.io.StringReader;

import java.sql.PreparedStatement;

import java.sql.ResultSet;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Unmarshaller;

import org.xml.sax.InputSource;



import org.riverock.a3.AccessDeniedException;

import org.riverock.a3.AuthSession;

import org.riverock.as.ApplicationInterface;

import org.riverock.as.server.ApplicationTools;

import org.riverock.generic.db.DBconnect;

import org.riverock.schema.appl_server.ResourceRequestParameterType;

import org.riverock.schema.appl_server.ResourceRequestType;

import org.riverock.schema.core.SiteVirtualHostItemType;

import org.riverock.generic.startup.InitParam;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.tools.XmlTools;



public class UpdateTestSiteVirtualHostItem implements ApplicationInterface

{

     private static Logger cat = Logger.getLogger("org.riverock.main.core.UpdateTestSiteVirtualHostItem" );



     public UpdateTestSiteVirtualHostItem(){}



     public static Long processData(DBconnect db_, SiteVirtualHostItemType item)

         throws Exception

     {



         String sql_ =

             "update SITE_VIRTUAL_HOST "+

             "set"+

             "    ID_SITE_VIRTUAL_HOST=?, "+

             "    ID_SITE=?, "+

             "    NAME_VIRTUAL_HOST=?"+

             "where ID_SITE_VIRTUAL_HOST=?";



         PreparedStatement ps = null;

         ResultSet rs = null;

         try

         {

             ps = db_.prepareStatement(sql_);



             ps.setLong(1, item.getIdSiteVirtualHost() );

             ps.setLong(2, item.getIdSite() );

             ps.setString(3, item.getNameVirtualHost() );



             // prepare PK

             ps.setLong(4, item.getIdSiteVirtualHost() );



             int countInsertRecord = ps.executeUpdate();



             if (cat.isDebugEnabled())

                 cat.debug("Count of inserted records - "+countInsertRecord);



             return new Long( countInsertRecord );



         }

         catch (Exception e)

         {

             cat.error("Error update db", e);

             cat.error("Item getIdSiteVirtualHost(), value - "+item.getIdSiteVirtualHost(), e);

             cat.error("Item getIdSite(), value - "+item.getIdSite(), e);

             cat.error("Item getNameVirtualHost(), value - "+item.getNameVirtualHost(), e);

             throw e;

         }

         finally

         {

             RsetTools.closeRsPs(rs, ps);

             rs = null;

             ps = null;

         }



     }



     public byte[] processRequest( ResourceRequestType applReq, AuthSession authSession )

         throws Exception

     {

          if (applReq==null || applReq.getParametersCount()==0)

              return null;



          SiteVirtualHostItemType item = null;

          for (int i=0; i<applReq.getParametersCount(); i++)

          {

              ResourceRequestParameterType param = applReq.getParameters(i);

              if ("mill.item".equals( param.getNameParameter()))

              {

                  String stringParam = ApplicationTools.decodeParameter( param );



                  if (cat.isDebugEnabled())

                      cat.debug("Parameter is "+stringParam);



                  InputSource inSrc = new InputSource( new StringReader(stringParam) );

                  item = (SiteVirtualHostItemType) Unmarshaller.unmarshal(SiteVirtualHostItemType.class, inSrc);

                  break;

              }

          }

          if (item == null )

              return null;



          Long resultItem = UpdateTestSiteVirtualHostItem.processData( DBconnect.getInstance( false ), item );

          return XmlTools.getXml(resultItem, "CountProcessedRecords");

      }



   public static void main(String args[])

       throws Exception

   {

       org.riverock.generic.startup.StartupApplication.init();



       SiteVirtualHostItemType data = new SiteVirtualHostItemType();



//      data.set();



       Long resultItem = UpdateTestSiteVirtualHostItem.processData( DBconnect.getInstance( false ), data );



       XmlTools.writeToFile(

           resultItem,

           InitParam.getMillDebugDir()+"test-site_virtual_host-item.xml",

           "utf-8",

           "CountProcessedRecords"

       );

   }

}

