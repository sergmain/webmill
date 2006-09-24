/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software             
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
