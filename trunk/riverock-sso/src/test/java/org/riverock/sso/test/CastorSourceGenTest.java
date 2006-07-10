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

/**
 * @author SMaslyukov
 *         Date: 12.05.2005
 *         Time: 17:14:18
 *         $Id$
 */
public class CastorSourceGenTest {
    public static void main(String s[]) throws Exception {

        String p[] = new String[] {
            "-i",
            "src-core/xsd/riverock-sso-core.xsd",
            "-package",
            "org.riverock.sso.schema.core",
            "-dest",
            "src-core/java",
            "-types",
            "j2",
            "-f"
        };
        org.exolab.castor.builder.SourceGenerator.main(p);
/*
                    <property name="schema-dir" value="${src-core.java.dir}/org/riverock/sso/schema/core/"/>
                    <property name="schema-file" value="${src-core.xsd.dir}/riverock-sso-core.xsd"/>
                    <property name="schema-package-java" value="org.riverock.sso.schema.core"/>

                        <java fork="true" classname="org.exolab.castor.builder.SourceGenerator">
                            <arg value="-i"/>
                            <arg value="${schema-file}"/>
                            <arg value="-package"/>
                            <arg value="${schema-package-java}"/>
                            <arg value="-dest"/>
                            <arg value="${src-core.java.dir}"/>
                            <arg value="-types"/>
                            <arg value="j2"/>
                            <arg value="-f"/>
                            <classpath>
                                <pathelement location="${local-lib.dir}/castor"/>
                                <path refid="classpath-core"/>
                            </classpath>
                        </java>
*/

    }
}
