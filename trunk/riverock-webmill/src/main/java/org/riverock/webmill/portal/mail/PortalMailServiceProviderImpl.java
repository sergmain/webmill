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
package org.riverock.webmill.portal.mail;

import org.riverock.interfaces.portal.mail.PortalMailServiceProvider;
import org.riverock.interfaces.portal.mail.PortalMailService;

/**
 * @author Sergei Maslyukov
 *         Date: 30.05.2006
 *         Time: 16:06:32
 */
public class PortalMailServiceProviderImpl implements PortalMailServiceProvider {

    private PortalMailService portalMailService = null;

    public PortalMailServiceProviderImpl(String smtpHost, String sender) {
        portalMailService = new PortalMailServiceImpl(smtpHost, sender);
    }

    public PortalMailService getPortalMailService() {
        return portalMailService;
    }
}
