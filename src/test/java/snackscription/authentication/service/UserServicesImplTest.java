package snackscription.authentication.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import snackscription.authentication.dto.UserDTO;
import snackscription.authentication.model.User;
import snackscription.authentication.repository.UserRepository;
import snackscription.authentication.utils.JWTUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServicesImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        lenient().when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId("1");
            return userToSave;
        });

        lenient().when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        lenient().when(jwtUtils.generateToken(any(User.class))).thenReturn("jwtToken");
        lenient().when(jwtUtils.generateRefreshToken(any(), any(User.class))).thenReturn("refreshToken");
    }

    @Test
    void testRegisterSuccess() {
        UserDTO registrationRequest = new UserDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setName("Test User");
        registrationRequest.setPassword("password");
        registrationRequest.setRole("USER");

        UserDTO response = userService.register(registrationRequest);

        assertEquals(201, response.getStatusCode());
        assertNotNull(response.getUser());
        assertEquals("1", response.getUser().getId());
        assertEquals("Register success!", response.getMessage());
    }

    @Test
    void testRegisterFailure() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Registration failed"));
        UserDTO registrationRequest = new UserDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setName("Test User");
        registrationRequest.setPassword("password");
        registrationRequest.setRole("USER");

        UserDTO response = userService.register(registrationRequest);

        assertEquals(500, response.getStatusCode());
        assertNull(response.getUser());
        assertEquals("Registration failed", response.getMessage());
    }

    @Test
    void testLoginSuccess() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        UserDTO loginRequest = new UserDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDTO response = userService.login(loginRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("24hrs", response.getExpirationTime());
        assertEquals("Login success!", response.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        String userEmail = "test@example.com";
        String userPassword = "password";
        UserDTO loginRequest = new UserDTO();
        loginRequest.setEmail(userEmail);
        loginRequest.setPassword(userPassword);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        UserDTO response = userService.login(loginRequest);

        assertEquals(404, response.getStatusCode());
        assertNull(response.getToken());
        assertNull(response.getRefreshToken());
        assertNull(response.getExpirationTime());
        assertEquals("User not found for email: " + userEmail, response.getMessage());
    }

    @Test
    void testLoginAuthenticationFailure() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        UserDTO loginRequest = new UserDTO();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Authentication failed"));
        lenient().when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDTO response = userService.login(loginRequest);

        assertEquals(500, response.getStatusCode());
        assertNull(response.getToken());
        assertNull(response.getRefreshToken());
        assertNull(response.getExpirationTime());
        assertEquals("Authentication failed", response.getMessage());
    }

    @Test
    void testRefreshToken() {
        UserDTO refreshTokenRequest = new UserDTO();
        refreshTokenRequest.setToken("existingJwtToken");
        refreshTokenRequest.setRefreshToken("existingRefreshToken");

        User user = new User();
        user.setEmail("test@example.com");

        when(jwtUtils.extractUsername(refreshTokenRequest.getToken())).thenReturn("test@example.com");
        when(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)).thenReturn(true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("newJwtToken");

        UserDTO response = userService.refreshToken(refreshTokenRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("newJwtToken", response.getToken());
        assertEquals("existingRefreshToken", response.getRefreshToken());
        assertEquals("24Hr", response.getExpirationTime());
        assertEquals("Refresh token success!", response.getMessage());

        verify(jwtUtils, times(1)).extractUsername("existingJwtToken");
        verify(jwtUtils, times(1)).isTokenValid("existingJwtToken", user);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtUtils, times(1)).generateToken(user);
    }

    @Test
    void testGetAllUser() {
        User user1 = new User();
        user1.setId("1");
        user1.setEmail("test1@example.com");
        user1.setName("Test User 1");
        user1.setRole("USER");
        user1.setPassword("hashedPassword");

        User user2 = new User();
        user2.setId("2");
        user2.setEmail("test2@example.com");
        user2.setName("Test User 2");
        user2.setRole("ADMIN");
        user2.setPassword("hashedPassword");

        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        UserDTO response = userService.getAllUser();

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getUserList());
        assertEquals(2, response.getUserList().size());
        assertEquals("Get all user success!", response.getMessage());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole("USER");
        user.setPassword("hashedPassword");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserDTO response = userService.getUserById("1");

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getUser());
        assertEquals("1", response.getUser().getId());
        assertEquals("test@example.com", response.getUser().getEmail());
        assertEquals("Test User", response.getUser().getName());
        assertEquals("USER", response.getUser().getRole());
        assertEquals("User with id 1 is found!", response.getMessage());

        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole("USER");
        user.setPassword("hashedPassword");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById("1");

        UserDTO response = userService.deleteUser("1");

        assertEquals(200, response.getStatusCode());
        assertEquals("Delete user success!", response.getMessage());

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        UserDTO response = userService.deleteUser("1");

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setEmail("test@example.com");
        existingUser.setName("Test User");
        existingUser.setRole("USER");
        existingUser.setPassword("hashedPassword");

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO updatedUserData = new UserDTO();
        updatedUserData.setEmail("updated@example.com");
        updatedUserData.setName("Updated User");
        updatedUserData.setRole("ADMIN");
        updatedUserData.setPassword("newHashedPassword");

        UserDTO response = userService.updateUser("1", updatedUserData);

        assertEquals(200, response.getStatusCode());
        assertEquals("User update success!", response.getMessage());
        assertEquals("1", response.getUser().getId());
        assertEquals("updated@example.com", response.getUser().getEmail());
        assertEquals("Updated User", response.getUser().getName());
        assertEquals("ADMIN", response.getUser().getRole());

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        UserDTO updatedUserData = new UserDTO();
        updatedUserData.setEmail("updated@example.com");
        updatedUserData.setName("Updated User");
        updatedUserData.setRole("ADMIN");
        updatedUserData.setPassword("newHashedPassword");

        UserDTO response = userService.updateUser("1", updatedUserData);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found!", response.getMessage());

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserDTO registrationRequest = new UserDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setName("Test User");
        registrationRequest.setPassword("password");
        registrationRequest.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        UserDTO response = userService.register(registrationRequest);

        assertEquals(500, response.getStatusCode());
        assertNull(response.getUser());
        assertEquals("User with the email test@example.com already exist!", response.getMessage());
    }

    @Test
    void testRefreshTokenFailure() {
        UserDTO refreshTokenRequest = new UserDTO();
        refreshTokenRequest.setToken("invalidToken");

        when(jwtUtils.extractUsername(anyString())).thenThrow(new RuntimeException("Invalid token"));

        UserDTO response = userService.refreshToken(refreshTokenRequest);

        assertEquals(500, response.getStatusCode());
        assertNull(response.getToken());
        assertNull(response.getRefreshToken());
        assertNull(response.getExpirationTime());
        assertEquals("Invalid token", response.getMessage());
    }

    @Test
    void testGetAllUserNotFound() {
        when(userRepository.findAll()).thenReturn(List.of());

        UserDTO response = userService.getAllUser();

        assertEquals(404, response.getStatusCode());
        assertNull(response.getUserList());
        assertEquals("No users found!", response.getMessage());
    }

    @Test
    void testGetAllUserException() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        UserDTO response = userService.getAllUser();

        assertEquals(500, response.getStatusCode());
        assertNull(response.getUserList());
        assertEquals("Database error", response.getMessage());
    }

    @Test
    void testGetUserByIdException() {
        when(userRepository.findById("1")).thenThrow(new RuntimeException("Database error"));

        UserDTO response = userService.getUserById("1");

        assertEquals(500, response.getStatusCode());
        assertNull(response.getUser());
        assertEquals("Database error", response.getMessage());
    }

    @Test
    void testDeleteUserException() {
        when(userRepository.findById("1")).thenThrow(new RuntimeException("Database error"));

        UserDTO response = userService.deleteUser("1");

        assertEquals(500, response.getStatusCode());
        assertEquals("Database error", response.getMessage());
    }

    @Test
    void testUpdateUserException() {
        UserDTO updatedUserData = new UserDTO();
        updatedUserData.setEmail("updated@example.com");
        updatedUserData.setName("Updated User");
        updatedUserData.setRole("ADMIN");
        updatedUserData.setPassword("newHashedPassword");

        when(userRepository.findById("1")).thenThrow(new RuntimeException("Database error"));

        UserDTO response = userService.updateUser("1", updatedUserData);

        assertEquals(500, response.getStatusCode());
        assertEquals("Database error", response.getMessage());
    }

    @Test
    void testGetMyInfoSuccess() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setId("1");
        user.setEmail(userEmail);
        user.setName("Test User");
        user.setRole("USER");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserDTO response = userService.getMyInfo(userEmail);

        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getUser());
        assertEquals("1", response.getUser().getId());
        assertEquals(userEmail, response.getUser().getEmail());
    }

    @Test
    void testGetMyInfoUserNotFound() {
        String userEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        UserDTO response = userService.getMyInfo(userEmail);

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found for update", response.getMessage());
        assertNull(response.getUser());
    }

    @Test
    void testGetMyInfoException() {
        String userEmail = "test@example.com";

        when(userRepository.findByEmail(userEmail)).thenThrow(new RuntimeException("Database error"));

        UserDTO response = userService.getMyInfo(userEmail);

        assertEquals(500, response.getStatusCode());
        assertEquals("Error occurred while getting user info: Database error", response.getMessage());
        assertNull(response.getUser());
    }
}
