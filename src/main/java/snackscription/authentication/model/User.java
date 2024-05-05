package snackscription.authentication.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "users")
@Data
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String email;
    private String password;
    private String role;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
    private static final List<String> VALID_ROLES = List.of("USER", "ADMIN");

    public void setEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setRole(String role) {
        if (!VALID_ROLES.contains(role)) {
            throw new IllegalArgumentException("Invalid user type");
        }
        this.role = role;
    }

    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        this.password = password;
    }
}
