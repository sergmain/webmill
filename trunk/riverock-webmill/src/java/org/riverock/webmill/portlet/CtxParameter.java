/**

 * User: serg_main

 * Date: 30.04.2004

 * Time: 15:45:10

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.portlet;



import java.util.Locale;





public class CtxParameter

{

    // context type can equals some portlet type or 'index'

    // 'index' or 'mill.index' special build-in type of context

    // current name of 'index' context type in Constants.CTX_TYPE_INDEX

    private String contextType = null;

    private String nameTemplate = null;

    private Long ctxId = null;

    private Locale locale = null;



}

