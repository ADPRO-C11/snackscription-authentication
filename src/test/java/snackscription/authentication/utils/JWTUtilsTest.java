package snackscription.authentication.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTUtilsTest {

    private JWTUtils jwtUtils;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        String JWT_SECRET = "rMfD5uHwAq6zZb8yP3sQ9vC1xG5jKp2sR5vU8yBwEe2bMfXj";
        jwtUtils = new JWTUtils(JWT_SECRET);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(userDetails).getAuthorities();
        doReturn("testUser").when(userDetails).getUsername();
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testGenerateRefreshToken() {
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
