/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.captcha;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

/**
 * @author Sergei Maslyukov
 *         Date: 26.06.2006
 *         Time: 17:13:29
 */
public class CaptchaServiceSingleton {

    public static final int MAX_CAPTCHA_SESSION = 3600*1000;

    private static Map<String, Long> captchaIdMap = new ConcurrentHashMap<String, Long>();
    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

    public static ImageCaptchaService getInstance(){
        return instance;
    }

    public static String createCaptchaId() {
        while (true) {
            String captchaId = ""+new Double(Math.random()*10000000).intValue();
            Long created = captchaIdMap.get(captchaId);
            if (created==null || (System.currentTimeMillis()-created) > MAX_CAPTCHA_SESSION) {
                captchaIdMap.put(captchaId, System.currentTimeMillis());
                return captchaId;
            }
        }
    }

}