package com.songhanwu.messaging.service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.songhanwu.messaging.dao.UserDAO;
import com.songhanwu.messaging.dao.UserLoginTokenDAO;
import com.songhanwu.messaging.dao.UserValidationCodeDAO;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.dto.UserLoginTokenDTO;
import com.songhanwu.messaging.dto.UserValidationCodeDTO;
import com.songhanwu.messaging.enumeration.Gender;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService { // UserManager, UserLogic

    @Autowired private UserDAO userDAO;
    @Autowired private UserValidationCodeDAO userValidationCodeDAO; // testability
    @Autowired private UserLoginTokenDAO userLoginTokenDAO;

    public void register(String username,
                         String password,
                         String repeatPassword,
                         String nickname,
                         String email,
                         Gender gender,
                         String address) throws Exception {
        // validation
        if (!password.equals(repeatPassword)) {
            throw new Exception("Passwords are not matched");
        }

        if (username == null || username.isBlank() || nickname == null || nickname.isBlank()) {
            throw new Exception("Username or nickname is empty");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setNickname(nickname);
        userDTO.setEmail(email);
        userDTO.setGender(gender);
        userDTO.setAddress(address);
        userDTO.setIsValid(false);
        userDTO.setRegisterTime(new Date());

        this.userDAO.insert(userDTO);

        UserValidationCodeDTO userValidationCodeDTO = new UserValidationCodeDTO();
        userValidationCodeDTO.setValidationCode(String.format("%06d", new Random().nextInt(1000000)));
        userValidationCodeDTO.setUserId(userDTO.getId());
        this.userValidationCodeDAO.insert(userValidationCodeDTO);

    }

    public void activate(String username, String validationCode) throws Exception {
        UserDTO userDTO = this.userDAO.selectByUsername(username);
        if (userDTO == null) {
            throw new Exception("User does not exist");
        }
        if (userDTO.getIsValid()) {
            return;
        }
        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());
        if (userValidationCodeDTO == null) {
            throw new Exception("Validation code does not exist in db");
        }
        if (!validationCode.equals(userValidationCodeDTO.getValidationCode())) {
            throw new Exception("Validation code is not matched with database records");
        } else {
            this.userDAO.updateToValid(userDTO.getId());
            this.userValidationCodeDAO.deleteById(userValidationCodeDTO.getId());
        }
    }

    public String login(String username, String password) throws Exception {
        UserDTO userDTO = this.userDAO.selectByUsername(username);
        if (userDTO == null) {
            throw new Exception("User does not exist");
        }
        if (!userDTO.getIsValid()) {
            throw new Exception("User has not been activated");
        }
        if (!password.equals(userDTO.getPassword())) {
            throw new Exception("Wrong password");
        }

        String loginToken = RandomStringUtils.secure().nextAlphanumeric(128);

        UserLoginTokenDTO userLoginTokenDTO = new UserLoginTokenDTO();
        userLoginTokenDTO.setUserId(userDTO.getId());
        userLoginTokenDTO.setLoginToken(loginToken);
        userLoginTokenDTO.setLoginTime(new Date());
        this.userLoginTokenDAO.insert(userLoginTokenDTO);

        return loginToken;
    }

    public UserDTO authenticate(String loginToken) throws Exception {
        UserLoginTokenDTO userLoginTokenDTO = this.userLoginTokenDAO.selectByLoginToken(loginToken);
        if (userLoginTokenDTO == null) {
            throw new Exception("User login token is invalid");
        }
        if (new Date().getTime() - userLoginTokenDTO.getLoginTime().getTime() > Duration.ofDays(14).toMillis()) {
            throw new Exception("Login token is expired");
        }

        return this.userDAO.selectByUserId(userLoginTokenDTO.getUserId());
    }

    public UserDTO findUserByUserId(int userId) throws Exception {
        UserDTO userDTO = this.userDAO.selectByUserId(userId);
        if (userDTO == null) {
            throw new Exception("User does not exist");
        }
        return userDTO;
    }

    public void logout(String loginToken) throws Exception {
        UserLoginTokenDTO userLoginTokenDTO = this.userLoginTokenDAO.selectByLoginToken(loginToken);
        if (userLoginTokenDTO == null) {
            throw new Exception("User login token is invalid");
        }
        this.userLoginTokenDAO.deleteByUserLoginTokenId(userLoginTokenDTO.getId());

    }

    public List<UserDTO> searchUsers(String keyword) {
        return this.userDAO.selectByKeyword(keyword);
    }
}
