/**

 * User: serg_main

 * Date: 30.04.2004

 * Time: 20:15:16

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet;



import org.apache.log4j.Logger;

import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.interfaces.schema.portlet.types.PortletDescriptionTypeTypePortletType;





public class PortletDescription

{

    private static Logger log = Logger.getLogger(PortletDescription.class);



    private PortletType portletConfig = null;

    private PortletDescriptionTypeTypePortletType portletType = null;



    private PortletDescription()

    {

    }



    public static PortletDescription getInstance(String type)

        throws Exception

    {

        if (log.isDebugEnabled())

            log.debug("get description for portlet type - "+type);



        PortletDescription desc = new PortletDescription();



        desc.portletConfig = PortletManager.getPortletDescription( type );



        if (log.isDebugEnabled())

            log.debug("portletConfig - "+desc.portletConfig);



        if (desc.getPortletConfig()==null)

            return null;



        String typePortletTemp =

            PortletTools.getStringParam(

                desc.getPortletConfig(), PortletTools.type_portlet

            );



        desc.setPortletType(PortletDescriptionTypeTypePortletType.valueOf( typePortletTemp ));



        return desc;

    }



    public PortletType getPortletConfig()

    {

        return portletConfig;

    }



    public void setPortletConfig(PortletType portletConfig)

    {

        this.portletConfig = portletConfig;

    }



    public PortletDescriptionTypeTypePortletType getPortletType()

    {

        return portletType;

    }



    public void setPortletType(PortletDescriptionTypeTypePortletType portletType)

    {

        this.portletType = portletType;

    }



}

