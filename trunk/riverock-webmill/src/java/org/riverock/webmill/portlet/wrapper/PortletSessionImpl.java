/**

 * User: serg_main

 * Date: 12.05.2004

 * Time: 18:50:27

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet.wrapper;



import java.util.Enumeration;



import javax.portlet.PortletContext;

import javax.portlet.PortletSession;

import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;



public class PortletSessionImpl implements PortletSession

{

    private static Logger log = Logger.getLogger( PortletSessionImpl.class );



    private HttpSession session = null;

    public PortletSessionImpl(HttpSession session)

    {

        this.session = session;

        creationTime = System.currentTimeMillis();

    }



    private long creationTime;

    public long getCreationTime()

    {

        return creationTime;

    }



    public String getId()

    {

        accessTime = System.currentTimeMillis();

        return session.getId();

    }



    private long accessTime;

    public long getLastAccessedTime()

    {

        return accessTime;

    }



    public void setMaxInactiveInterval(int i)

    {

        accessTime = System.currentTimeMillis();

        session.setMaxInactiveInterval(i);

    }



    public PortletContext getPortletContext()

    {

        accessTime = System.currentTimeMillis();

        return null;

    }



    public int getMaxInactiveInterval()

    {

        accessTime = System.currentTimeMillis();

        return session.getMaxInactiveInterval();

    }



    public Object getAttribute(String s)

    {

        accessTime = System.currentTimeMillis();

        return session.getAttribute( s );

    }



    public Object getAttribute(String s, int i)

    {

        accessTime = System.currentTimeMillis();

        return session.getAttribute(s);

    }



    public Enumeration getAttributeNames()

    {

        accessTime = System.currentTimeMillis();

        return session.getAttributeNames();

    }



    public Enumeration getAttributeNames(int i)

    {

        accessTime = System.currentTimeMillis();

        return session.getAttributeNames();

    }



    public void setAttribute(String s, Object o)

    {

        accessTime = System.currentTimeMillis();

        session.setAttribute( s,  o);

    }



    public void setAttribute(String s, Object o, int i)

    {

        accessTime = System.currentTimeMillis();

        session.setAttribute( s,  o);

    }



    public void removeAttribute(String s)

    {

        accessTime = System.currentTimeMillis();

        session.removeAttribute( s);

    }



    public void removeAttribute(String s, int i)

    {

        accessTime = System.currentTimeMillis();

        try

        {

            session.removeAttribute( s );

        }

        catch (IllegalStateException e)

        {

            log.error("session.removeAttribute() ", e);

            throw e;

//            session.invalidate();

//            session = null;

        }

    }



    public void invalidate()

    {

        accessTime = System.currentTimeMillis();

        session.invalidate();

    }



    public boolean isNew()

    {

        accessTime = System.currentTimeMillis();

        return session.isNew();

    }

}

