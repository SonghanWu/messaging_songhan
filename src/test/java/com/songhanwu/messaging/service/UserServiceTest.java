package com.songhanwu.messaging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.songhanwu.messaging.dao.UserDAO;
import com.songhanwu.messaging.dao.UserValidationCodeDAO;
import com.songhanwu.messaging.dto.UserDTO;
import com.songhanwu.messaging.dto.UserValidationCodeDTO;
import com.songhanwu.messaging.enumeration.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;

    @Mock private UserDAO userDAO;
    @Mock private UserValidationCodeDAO userValidationCodeDAO;

    @Captor private ArgumentCaptor<UserDTO> userDTOArgumentCaptor;
    @Captor private ArgumentCaptor<UserValidationCodeDTO> userValidationCodeDTOArgumentCaptor;

    @Test
    void testRegister_passwordsNotMatched_throwsException() throws Exception {
        Exception exception = assertThrows(
                Exception.class,
                () -> this.userService.register("username", "aaabb", "cccddd", "nickname",
                                                "email", Gender.MALE, "ca"));
        assertEquals("Passwords are not matched", exception.getMessage());
    }

    @Test
    void testRegister_happyCase() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(10);
        userDTO.setUsername("username");
        userDTO.setPassword("cccddd");
        userDTO.setNickname("nickname");
        userDTO.setEmail("email");
        userDTO.setGender(Gender.MALE);
        userDTO.setAddress("ca");

        when(this.userDAO.selectByUsername("username")).thenReturn(userDTO);

        doNothing().when(this.userDAO).insert(any());
        doNothing().when(this.userValidationCodeDAO).insert(any());

        this.userService.register("username", "cccddd", "cccddd", "nickname",
                                  "email", Gender.MALE, "ca");


        verify(this.userDAO).insert(this.userDTOArgumentCaptor.capture());

        UserDTO insertedUserDTO = this.userDTOArgumentCaptor.getValue();
        assertEquals("username", insertedUserDTO.getUsername());
        assertEquals("cccddd", insertedUserDTO.getPassword());

        verify(this.userValidationCodeDAO).insert(this.userValidationCodeDTOArgumentCaptor.capture());

        UserValidationCodeDTO insertedUserValidationCodeDTO = this.userValidationCodeDTOArgumentCaptor.getValue();
        assertEquals(10, insertedUserValidationCodeDTO.getUserId());
        assertEquals(6, insertedUserValidationCodeDTO.getValidationCode().length());
    }
}
