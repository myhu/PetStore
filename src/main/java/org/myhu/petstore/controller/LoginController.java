package org.myhu.petstore.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.*;

@RestController
public class LoginController
{
    private static final Logger                    LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final            Map<String, List<String>> userDb = new HashMap<>();

    public LoginController() {
        userDb.put("tom", Arrays.asList("user"));
        userDb.put("sally", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login)
            throws ServletException
    {
        System.out.println("login.userName: " + login.userName);
        if (login.userName == null || !userDb.containsKey(login.userName)) {
            throw new ServletException("Invalid login");
        }
        return new LoginResponse(Jwts.builder().setSubject(login.userName)
                                         .claim("roles", userDb.get(login.userName)).setIssuedAt(new Date())
                                         .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    }

    private static class UserLogin {
        public String userName;
        public String password;
    }

    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
