package com.songhanwu.messaging.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.songhanwu.messaging.dao.TestUserDAO;
import com.songhanwu.messaging.dao.TestUserValidationCodeDAO;
import com.songhanwu.messaging.dao.UserDAO;
import com.songhanwu.messaging.dao.UserValidationCodeDAO;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.dto.UserValidationCodeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// test framework: JUnit
@SpringBootTest(properties = {
    "spring.profiles.active=test"
})
@AutoConfigureMockMvc // HTTP client
class UserIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TestUserDAO testUserDAO;
    @Autowired private TestUserValidationCodeDAO testUserValidationCodeDAO;
    @Autowired private UserValidationCodeDAO userValidationCodeDAO;
    @Autowired private UserDAO userDAO;

    @BeforeEach
    public void deleteAllOldData() {
        this.testUserDAO.deleteAll();
        this.testUserValidationCodeDAO.deleteAll();
    }

    @Test
        // test${target}_${scenario}_${expectation}
    void testRegister_passwordsNotMatched_returnsBadRequest() throws Exception {
        String body = """
                        {
                            "username" : "xxx1111111231",
                            "password": "xxxxx11",
                            "repeatPassword": "xxxxx1",
                            "nickname":"dsfsfdf",
                            "email": "Ssdfdsfsfsss",
                            "gender":"MALE",
                            "address": "ssssssss"
                        }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords are not matched"));
    }

    @Test
    void testRegister_validInput_returnsOkAndUserIsRegistered() throws Exception {
        String body = """
                        {
                            "username" : "xxx11111112323232313211",
                            "password": "xxxxx11",
                            "repeatPassword": "xxxxx11",
                            "nickname":"dsfsfdf",
                            "email": "Ssdfdsf12312313sfsss1",
                            "gender":"MALE",
                            "address": "ssssssss"
                        }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(body))
                .andExpect(status().isOk());
    }


    @Test
    void testActivate_validInput_returnsOkAndUserIsSetToValid() throws Exception {
        String body = """
                        {
                            "username" : "xxx11111112323232313211",
                            "password": "xxxxx11",
                            "repeatPassword": "xxxxx11",
                            "nickname":"dsfsfdf",
                            "email": "Ssdfdsf12312313sfsss1",
                            "gender":"MALE",
                            "address": "ssssssss"
                        }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(body));

        UserDTO userDTO = this.userDAO.selectByUsername("xxx11111112323232313211");
        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());

        body = String.format("""
                                      {
                                          "username" : "xxx11111112323232313211",
                                          "validationCode": "%s"
                                      }
                                     """,
                             userValidationCodeDTO.getValidationCode());
        this.mockMvc.perform(post("/users/activate")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(body))
                .andExpect(status().isOk());

        // "%s %s" % (a,b)
    }

    @Test
        // test${target}_${scenario}_${expectation}
    void testRegister_usernameIsNotGiven_returnsBadRequest() throws Exception {
        String body = """
                        {
                            "password": "xxxxx11",
                            "repeatPassword": "xxxxx11",
                            "nickname":"dsfsfdf",
                            "email": "Ssdfdsfsfsss",
                            "gender":"MALE",
                            "address": "ssssssss"
                        }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username or nickname is empty"));
    }
}
