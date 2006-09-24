/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.price;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public class PositionItem
{
    public String name = null;
    public String url = null;
    public long id_group_current = 0;
    public long id_group_top = 0;

    protected void finalize() throws Throwable
    {
        name = null;
        url = null;

        super.finalize();
    }

    public PositionItem(String n_, String u_, long id_curr, long id_top)
    {
        name = n_;
        url = u_;

        id_group_current = id_curr;
        id_group_top = id_top;

    }

    public PositionItem(){}
}
