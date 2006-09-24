/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.interfaces.generic;

/**
 * @author Sergei Maslyukov
 *         Date: 25.05.2006
 *         Time: 15:55:35
 */
/**
 * project: pluto, license: Apache2
 * The internal render request interface extends the internal portlet request
 * interface and provides some render-specific methods.
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @since 2006-02-17
 */
public interface InternalRequest {
    public void setIncluded(boolean included);
    public boolean isIncluded();
    public void setIncludedQueryString(String queryString);
}
