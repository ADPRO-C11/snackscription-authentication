package snackscription.authentication.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snackscription.authentication.enums.UserType;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();

        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(UserType.USER.getValue());
    }

    @Test
    void testGetName() {
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetEmail() {
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void testGetRole() {
        assertEquals(UserType.USER.getValue(), user.getRole());
    }

    @Test
    void testInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            user.setEmail("invalid");
        });
    }

    @Test
    void testInvalidUserType() {
        assertThrows(IllegalArgumentException.class, () -> {
            user.setRole("INVALID");
        });
    }

    @Test
    void testPasswordLessThan8Characters() {
        assertThrows(IllegalArgumentException.class, () -> {
            user.setPassword("123");
        });
    }
}
