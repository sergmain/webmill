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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.service.CaptchaServiceException;

//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Sergei Maslyukov
 *         Date: 26.06.2006
 *         Time: 17:10:11
 */
public class ImageCaptchaServlet extends HttpServlet {
    private static final String CAPTCHA_ID = "captchaId";

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        byte[] captchaChallengeAsJpeg;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            String captchaId = httpServletRequest.getParameter(CAPTCHA_ID);

            // call the ImageCaptchaService getChallenge method
            BufferedImage challenge = CaptchaServiceSingleton.getInstance().getImageChallengeForID(
                captchaId, httpServletRequest.getLocale()
            );

            // a jpeg encoder
//            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
//            jpegEncoder.encode(challenge);
        }
        catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        catch (CaptchaServiceException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
