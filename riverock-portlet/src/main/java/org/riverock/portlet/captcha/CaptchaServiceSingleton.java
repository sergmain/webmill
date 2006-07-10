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