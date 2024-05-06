package snackscription.authentication.utils;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    private final SecretKey KEY = null;
    private static final Long EXPIRATION_TIME = null;

    public JWTUtils() {

    }

    public String generateToken(UserDetails userDetails){
        return null;
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails){
        return null;
    }

    public String extractUsername(String token){
        return null;
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return false;
    }

    public boolean isTokenExpired(String token){
        return false;
    }
}
