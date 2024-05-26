package snackscription.authentication.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import snackscription.authentication.service.UserDetailsServiceImpl;
import snackscription.authentication.utils.JWTUtils;
import java.io.IOException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_EmptyAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("");
        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        String jwtToken = "validToken";
        String userEmail = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtils.extractUsername(jwtToken)).thenReturn(userEmail);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils).extractUsername(jwtToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        String jwtToken = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtils.extractUsername(jwtToken)).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils).extractUsername(jwtToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_AuthenticatedUser() throws ServletException, IOException {
        String jwtToken = "validToken";
        String userEmail = "test@example.com";

        UserDetails userDetails = mock(UserDetails.class);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null)
        );

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        when(jwtUtils.extractUsername(jwtToken)).thenReturn(userEmail);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils).extractUsername(jwtToken);
        verify(filterChain).doFilter(request, response);
    }
}
