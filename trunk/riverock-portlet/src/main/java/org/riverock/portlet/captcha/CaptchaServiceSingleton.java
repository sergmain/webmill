/*
 * org.riverock.portlet - Portlet Library
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