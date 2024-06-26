package snackscription.authentication.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import snackscription.authentication.dto.UserDTO;
import snackscription.authentication.enums.UserType;
import snackscription.authentication.model.User;
import snackscription.authentication.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Authentication authentication;

    @Test
    void testRegister() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");
        userDTO.setRole(UserType.USER.getValue());
        userDTO.setPassword("testPassword");

        UserDTO response = new UserDTO();
        User userResult = new User();
        userResult.setEmail("test@example.com");
        userResult.setName("Test User");
        userResult.setRole(UserType.USER.getValue());

        when(userService.register(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.register(userDTO);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testLogin() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();

        when(userService.login(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRefresh() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();

        when(userService.refreshToken(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.refresh(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAllUser() {
        UserDTO userListResponse = new UserDTO();
        List<User> userList = new ArrayList<>();

        when(userService.getAllUser()).thenReturn(userListResponse);

        ResponseEntity<UserDTO> result = userController.getAllUser();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userListResponse, result.getBody());
    }

    @Test
    void testGetUserById() {
        String userId = "someUserId";
        UserDTO response = new UserDTO();

        when(userService.getUserById(userId)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUser() {
        String userId = "someUserId";
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();

        when(userService.updateUser(userId, request)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.updateUser(userId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteUser() {
        String userId = "someUserId";
        UserDTO response = new UserDTO();

        when(userService.deleteUser(userId)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRegisterInternalServerError() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.register(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.register(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testLoginInternalServerError() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.login(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testLoginNotFound() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(404);

        when(userService.login(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.login(request);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testRefreshInternalServerError() {
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.refreshToken(any(UserDTO.class))).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.refresh(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAllUserInternalServerError() {
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.getAllUser()).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.getAllUser();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAllUserNotFound() {
        UserDTO response = new UserDTO();
        response.setStatusCode(404);

        when(userService.getAllUser()).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.getAllUser();

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetUserByIdInternalServerError() {
        String userId = "someUserId";
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.getUserById(userId)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.getUserById(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUserInternalServerError() {
        String userId = "someUserId";
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.updateUser(userId, request)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.updateUser(userId, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUserNotFound() {
        String userId = "someUserId";
        UserDTO request = new UserDTO();
        UserDTO response = new UserDTO();
        response.setStatusCode(404);

        when(userService.updateUser(userId, request)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.updateUser(userId, request);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteUserInternalServerError() {
        String userId = "someUserId";
        UserDTO response = new UserDTO();
        response.setStatusCode(500);

        when(userService.deleteUser(userId)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.deleteUser(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteUserNotFound() {
        String userId = "someUserId";
        UserDTO response = new UserDTO();
        response.setStatusCode(404);

        when(userService.deleteUser(userId)).thenReturn(response);

        ResponseEntity<UserDTO> result = userController.deleteUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetMyProfileSuccess() {
        String userEmail = "test@example.com";
        UserDTO response = new UserDTO();
        when(userService.getMyInfo(userEmail)).thenReturn(response);

        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<UserDTO> result = userController.getMyProfile();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetMyProfileUserNotFound() {
        String userEmail = "test@example.com";
        UserDTO response = new UserDTO();
        response.setStatusCode(404);
        when(userService.getMyInfo(userEmail)).thenReturn(response);

        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<UserDTO> result = userController.getMyProfile();

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetMyProfileInternalServerError() {
        String userEmail = "test@example.com";
        UserDTO response = new UserDTO();
        response.setStatusCode(500);
        when(userService.getMyInfo(userEmail)).thenReturn(response);

        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<UserDTO> result = userController.getMyProfile();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}
