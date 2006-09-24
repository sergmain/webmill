/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.namespace;

/**
 * Part of code used from Apache Pluto project, License Apache2
 *
 * @author SergeMaslyukov
 *         Date: 13.05.2006
 *         Time: 20:42:21
 *         $Id$
 */
public interface NamespaceMapper {

    public String encode(Namespace namespace, String name);

    public String encode(Namespace ns1, Namespace ns2, String name);

    public String decode(Namespace ns, String name);
}
