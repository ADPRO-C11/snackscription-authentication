package snackscription.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snackscription.authentication.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, String> {
    Optional<User> findByEmail(String email);
}
