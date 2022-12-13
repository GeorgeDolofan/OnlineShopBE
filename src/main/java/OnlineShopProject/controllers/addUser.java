package OnlineShopProject.controllers;

import OnlineShopProject.models.User;
import OnlineShopProject.models.UserServices;
import OnlineShopProject.repos.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
public class addUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServices service;


    @PostMapping(path="/register")
    public String addUser(@RequestBody User user, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        System.out.println(new Gson().toJson(user));

        service.register(user, getSiteURL(request));

        return "register_success";

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
