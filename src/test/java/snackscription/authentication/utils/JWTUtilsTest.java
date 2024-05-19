package snackscription.authentication.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTUtilsTest {

    private final String JWT_SECRET = "rMfD5uHwAq6zZb8yP3sQ9vC1xG5jKp2sR5vU8yBwEe2bMfXj"; // Randomly generated JWT secret key

    private final JWTUtils jwtUtils = new JWTUtils(JWT_SECRET);

    @Mock
    private UserDetails userDetails;

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testGenerateRefreshToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        Map<String, Object> claims = new HashMap<>();
        claims.put("key1", "value1");
        claims.put("key2", "value2");
        String refreshToken = jwtUtils.generateRefreshToken(claims, userDetails);
        assertNotNull(refreshToken);
        String extractedUsername = jwtUtils.extractUsername(refreshToken);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.isTokenValid(token, userDetails));

        when(userDetails.getUsername()).thenReturn("anotherUser");
        assertFalse(jwtUtils.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String token = jwtUtils.generateToken(userDetails);
        assertFalse(jwtUtils.isTokenExpired(token));
    }
}
