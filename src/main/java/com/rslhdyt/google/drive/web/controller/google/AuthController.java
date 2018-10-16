package com.rslhdyt.google.drive.web.controller.google;

import com.rslhdyt.google.drive.model.UserModel;
import com.rslhdyt.google.drive.repository.UserRepository;
import com.rslhdyt.google.drive.util.GoogleDriveUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/oauth/google")
public class AuthController
{

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void oauthLogin(HttpServletResponse response) throws Exception
    {
        String redirectURL = GoogleDriveUtil.getRedirectOauth();

        response.sendRedirect(redirectURL);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public String saveAuthorizationCode(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");

        if (code != null) {
            String userId = "1";
            String accessToken = saveToken(code, userId);

            UserModel user = userRepository.findById(Long.valueOf(userId)).get();
            user.setAccessToken(accessToken);
            userRepository.save(user);

            System.out.print(accessToken);

            return "success.html";
        }

        return "failed.html";
    }

    private String saveToken(String code, String userId) throws IOException
    {
        return GoogleDriveUtil.getAccessToken(code, userId);
    }
}
