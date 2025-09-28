package com.songhanwu.messaging.aspect;

import com.songhanwu.messaging.context.UserContext;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.service.UserService;
import jakarta.servlet.http.Cookie;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthenticationAspect {

    @Autowired private UserService userService;

    @Before("execution(* com.songhanwu.messaging.controller.*.*(..)) && @annotation(com.songhanwu.messaging" +
            ".annotation.NeedAuth)")
    public void authenticate(JoinPoint joinPoint) throws Exception {
        System.out.println("12123123s");
        var request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getCookies() == null) {
            throw new Exception("Login token is not found in request cookies.");

        }
        for (Cookie cookie : request.getCookies()) {
            if ("login_token".equals(cookie.getName())) {
                UserDTO userDTO = this.userService.authenticate(cookie.getValue());
                UserContext.setCurrentUser(userDTO);
                return;
            }
        }
        throw new Exception("Login token is not found in request cookies.");
    }
}
