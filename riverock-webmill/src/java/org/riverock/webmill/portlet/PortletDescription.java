/**

 * User: serg_main

 * Date: 30.04.2004

 * Time: 20:15:16

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet;



import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.riverock.interfaces.schema.portlet.types.PortletDescriptionTypeTypePortletType;





public class PortletDescription

{

    private PortletType portletConfig = null;

    private PortletDescriptionTypeTypePortletType portletType = null;



    private PortletDescription()

    {

    }



    public static PortletDescription getInstance(String type)

        throws Exception

    {

        PortletDescription desc = new PortletDescription();



        desc.setPortletConfig(PortletManager.getPortletDescription( type ));



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

