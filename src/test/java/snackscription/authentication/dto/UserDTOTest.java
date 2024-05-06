package snackscription.authentication.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snackscription.authentication.enums.UserType;
import snackscription.authentication.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
    }

    @Test
    void testStatusCode() {
        userDTO.setStatusCode(200);
        assertEquals(200, userDTO.getStatusCode());
    }

    @Test
    void testError() {
        userDTO.setError("Some error");
        assertEquals("Some error", userDTO.getError());
    }

    @Test
    void testMessage() {
        userDTO.setMessage("Some message");
        assertEquals("Some message", userDTO.getMessage());
    }

    @Test
    void testToken() {
        userDTO.setToken("token123");
        assertEquals("token123", userDTO.getToken());
    }

    @Test
    void testRefreshToken() {
        userDTO.setRefreshToken("refreshToken123");
        assertEquals("refreshToken123", userDTO.getRefreshToken());
    }

    @Test
    void testExpirationTime() {
        userDTO.setExpirationTime("2024-05-06 12:00:00");
        assertEquals("2024-05-06 12:00:00", userDTO.getExpirationTime());
    }

    @Test
    void testName() {
        userDTO.setName("John Doe");
        assertEquals("John Doe", userDTO.getName());
    }

    @Test
    void testEmail() {
        userDTO.setEmail("john@example.com");
        assertEquals("john@example.com", userDTO.getEmail());
    }

    @Test
    void testPassword() {
        userDTO.setPassword("password123");
        assertEquals("password123", userDTO.getPassword());
    }

    @Test
    void testRole() {
        userDTO.setRole(UserType.USER.getValue());
        assertEquals("USER", userDTO.getRole());
    }

    @Test
    void testUser() {
        User user = new User();
        user.setId("7f05c4e7-7890-4cf7-86e3-5d9f477b9e1a");
        userDTO.setUser(user);
        assertEquals(user, userDTO.getUser());
    }

    @Test
    void testUserList() {
        User user = new User();
        user.setId("7f05c4e7-7890-4cf7-86e3-5d9f477b9e1a");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userDTO.setUserList(userList);
        assertEquals(userList, userDTO.getUserList());
    }
}