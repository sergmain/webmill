/*

 * org.riverock.sso -- Single Sign On implementation

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



package org.riverock.sso.test;



import java.io.OutputStreamWriter;

import java.io.FileOutputStream;



import org.riverock.sso.config.SsoConfig;



public class Test1

{

    private static Test1 test = null;



    public static Test1 getInstance()

            throws Exception

    {

        if (test == null)

            test = new Test1();



        return test;

    }



    private Test1()

            throws Exception

    {

        String file_name = SsoConfig.getSsoDebugDir()+"Test.constructor.log";



        OutputStreamWriter prn_ = new OutputStreamWriter(

                new FileOutputStream(file_name, true)

                , "Cp1251");



        String ss = "Instance is: " + this + "\n";

        prn_.write(ss, 0, ss.length());

        prn_.flush();

        prn_.close();

        prn_ = null;



    }





    protected void finalize() throws java.lang.Throwable

    {

        String file_name = SsoConfig.getSsoDebugDir()+"Test.finalize.log";



        OutputStreamWriter prn_ = new OutputStreamWriter(

                new FileOutputStream(file_name, true)

                , "Cp1251");



        String ss = "Instance is: " + this + "\n";

        prn_.write(ss, 0, ss.length());

        prn_.flush();

        prn_.close();

        prn_ = null;



        super.finalize();

    }



}

