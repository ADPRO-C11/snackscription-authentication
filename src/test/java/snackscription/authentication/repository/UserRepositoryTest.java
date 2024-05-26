package snackscription.authentication.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import snackscription.authentication.model.User;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void testFindByEmail_UserFound() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByEmail(userEmail);

        assertEquals(user, foundUser.orElse(null));
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertEquals(Optional.empty(), foundUser);
    }
}
