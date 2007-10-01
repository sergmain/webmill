package org.riverock.portlet.register;

import org.apache.log4j.Logger;

import com.octo.captcha.service.CaptchaServiceException;

import org.riverock.module.action.ModuleActionRequest;
import org.riverock.module.web.request.ModuleRequest;
import org.riverock.portlet.captcha.CaptchaServiceSingleton;
import org.riverock.portlet.register.action.CreateAccountAction;

/**
 * User: SMaslyukov
 * Date: 01.10.2007
 * Time: 17:24:36
 */
public class RegisterUtils {
    private final static Logger log = Logger.getLogger(CreateAccountAction.class);

    public static String checkCaptcha(ModuleActionRequest moduleActionRequest, ModuleRequest moduleRequest) {
        Boolean isResponseCorrect;
        String captchaId = moduleRequest.getParameter(RegisterConstants.CAPTCHA_ID);
        //retrieve the response
        String response = moduleRequest.getParameter("j_captcha_response");

        if (log.isDebugEnabled()) {
            log.debug("captchaId: " + captchaId);
            log.debug("j_captcha_response: " + response);
        }
        // Call the Service method
        try {
            isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, response);
        }
        catch (CaptchaServiceException e) {
            log.error("Error validate captcha", e);
            return RegisterError.captchaWrong(moduleActionRequest);
        }

        if (isResponseCorrect==null || !isResponseCorrect) {
            return RegisterError.captchaWrong(moduleActionRequest);
        }

        return null;
    }
}
