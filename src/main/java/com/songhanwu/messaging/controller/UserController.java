package com.songhanwu.messaging.controller;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.songhanwu.messaging.annotation.NeedAuth;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.request.UserActivationRequest;
import com.songhanwu.messaging.request.UserLoginRequest;
import com.songhanwu.messaging.request.UserRegisterRequest;
import com.songhanwu.messaging.response.UserResponse;
import com.songhanwu.messaging.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    // DI = dependency injection; design pattern
    @Autowired private UserService userService;

    // invocation, latency, error
    @PostMapping("/register")
    public void register(@RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        this.userService.register(userRegisterRequest.getUsername(),
                                  userRegisterRequest.getPassword(),
                                  userRegisterRequest.getRepeatPassword(),
                                  userRegisterRequest.getNickname(),
                                  userRegisterRequest.getEmail(),
                                  userRegisterRequest.getGender(),
                                  userRegisterRequest.getAddress());


    }

    @PostMapping("/activate")
    public void activate(@RequestBody UserActivationRequest userActivationRequest) throws Exception {
        this.userService.activate(userActivationRequest.getUsername(),
                                  userActivationRequest.getValidationCode());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest userLoginRequest) throws Exception {
        String loginToken = this.userService.login(userLoginRequest.getUsername(),
                                                   userLoginRequest.getPassword());

        HttpCookie cookie = ResponseCookie.from("login_token", loginToken)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/logout")
    @NeedAuth
    public ResponseEntity<Void> logout(@CookieValue("login_token") String loginToken) throws Exception {
        this.userService.logout(loginToken);

        HttpCookie cookie = ResponseCookie.from("login_token", "")
                .path("/")
                .maxAge(Duration.ofSeconds(0))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/search")
    @NeedAuth
    public List<UserResponse> searchUsers(@CookieValue("login_token") String loginToken,
                                          @RequestParam String keyword) throws Exception {
        this.userService.authenticate(loginToken);
        List<UserDTO> userDTOs = this.userService.searchUsers(keyword);
        List<UserResponse> userResponses = userDTOs.stream().map(UserResponse::from).collect(Collectors.toList());
        return userResponses;
    }


    // forgetPassword
    // public void forgetPassword(@RequestBody) throws Exception {
    //   - check if username exists; - send an email to their email


    // resetPassword
    // public void resetPassword(validationCode, newPassword, repeatPassword)


}
