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



package org.riverock.cache.impl;



import java.sql.PreparedStatement;

import java.sql.ResultSet;



import org.riverock.a3.AccessDeniedException;

import org.riverock.a3.AuthSession;

import org.riverock.as.ApplicationInterface;

import org.riverock.as.server.ApplicationTools;

import org.riverock.generic.main.CacheItemV2;

import org.riverock.schema.appl_server.ResourceRequestParameterType;

import org.riverock.schema.appl_server.ResourceRequestType;

import org.riverock.schema.core.SiteVirtualHostItemType;

import org.riverock.generic.startup.InitParam;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.tools.XmlTools;

import org.riverock.generic.db.DatabaseAdapter;



import org.apache.log4j.Logger;



public class GetTestVirtualHostItem extends CacheItemV2 implements ApplicationInterface

{

     private static Logger log = Logger.getLogger("org.riverock.test.cache.GetTestVirtualHostItem" );



     private static GetTestVirtualHostItem dummy = new GetTestVirtualHostItem();



     public SiteVirtualHostItemType item = new SiteVirtualHostItemType();



     public boolean isFound = false;



     public GetTestVirtualHostItem(){}



     public int maxCountItems()

     {

         return 100;

     }



     public long maxTimePeriod()

     {

         return 100000;

     }



     public String getNameClass()

     {

         return "org.riverock.test.cache.GetTestVirtualHostItem";

     }



     public static GetTestVirtualHostItem getInstance(DatabaseAdapter db__, Long id__)

         throws Exception

     {

         return getInstance(db__, id__.longValue());

     }



     public static GetTestVirtualHostItem getInstance(DatabaseAdapter db__, long id__)

         throws Exception

     {

         return (GetTestVirtualHostItem) dummy.getInstanceNew(db__, id__);

     }



     public SiteVirtualHostItemType getData(DatabaseAdapter db_, long id)

         throws Exception

     {

         GetTestVirtualHostItem obj = GetTestVirtualHostItem.getInstance(db_, id);

         if (obj!=null)

              return obj.item;



         return new SiteVirtualHostItemType();

     }



     public void copyItem(SiteVirtualHostItemType target)

     {

         copyItem(this.item, target);

     }



     public static void copyItem(SiteVirtualHostItemType source, SiteVirtualHostItemType target)

     {

         if (source==null || target==null)

             return;



         target.setIdSiteVirtualHost(

             source.getIdSiteVirtualHost()

         );

         target.setIdSite(

             source.getIdSite()

         );

         target.setNameVirtualHost(

             source.getNameVirtualHost()

         );

     }



     public GetTestVirtualHostItem(DatabaseAdapter db_, long id)

         throws Exception

     {

         this(db_, new Long(id));

     }



    static String sql_ = null;

    static

    {

        sql_ =

            "select * from SITE_VIRTUAL_HOST where ID_SITE_VIRTUAL_HOST=?";



        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, GetTestVirtualHostItem.class );

        }

        catch(Exception e)

        {

            log.error("Exception in registerSql, sql\n"+sql_, e);

        }

        catch(Error e)

        {

            log.error("Error in registerSql, sql\n"+sql_, e);

        }

    }



     public GetTestVirtualHostItem(DatabaseAdapter db_, Long id)

         throws Exception

     {

         PreparedStatement ps = null;

         ResultSet rs = null;

         try

         {

             ps = db_.prepareStatement(sql_);

             ps.setLong(1, id.longValue());



             rs = ps.executeQuery();

             if (rs.next())

             {

                  isFound = true;



                 item.setIdSiteVirtualHost(

                   RsetTools.getLong( rs, "ID_SITE_VIRTUAL_HOST")

                 );

                 item.setIdSite(

                   RsetTools.getLong( rs, "ID_SITE")

                 );

                 item.setNameVirtualHost(

                   RsetTools.getString( rs, "NAME_VIRTUAL_HOST")

                 );

             }

         }

         catch (Exception e)

         {

             log.error("Exception create object", e);

             throw e;

         }

         catch (Error e)

         {

             log.error("Error create object", e);

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



         Long id = null;

         for (int i=0; i<applReq.getParametersCount(); i++)

         {

             ResourceRequestParameterType param = applReq.getParameters(i);

             if ("mill.id".equals( param.getNameParameter()))

             {

                 String stringParam = ApplicationTools.decodeParameter( param );



                 if (log.isDebugEnabled())

                     log.debug("Parameter is "+stringParam);



                 id = new Long( stringParam );

                 break;

             }

         }

         if (id == null )

             return null;



         String[][] ns = new String[][]

         {

              { "mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd" }

         };



         SiteVirtualHostItemType resultItem = GetTestVirtualHostItem.getInstance( DatabaseAdapter.getInstance( false ), id ).item;

         return XmlTools.getXml(resultItem, "SiteVirtualHostItemType");

     }



   public static void main(String args[])

       throws Exception

   {

       org.riverock.generic.startup.StartupApplication.init();



       long id = 1;

       SiteVirtualHostItemType resultItem = GetTestVirtualHostItem.getInstance( DatabaseAdapter.getInstance( false ), id ).item;



       String[][] ns = new String[][]

       {

           { "mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd" }

       };



       XmlTools.writeToFile(

           resultItem,

           InitParam.getMillDebugDir()+"test-site_virtual_host-item.xml",

           "utf-8",

           null,

           ns

       );

   }

}

