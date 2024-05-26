package snackscription.authentication.service;

import snackscription.authentication.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO registrationRequest);
    UserDTO login(UserDTO loginRequest);
    UserDTO refreshToken(UserDTO refreshTokenRequest);
    UserDTO getAllUser();
    UserDTO getUserById(String id);
    UserDTO deleteUser(String id);
    UserDTO updateUser(String id, UserDTO updatedUser);
    UserDTO getMyInfo(String email);
}
