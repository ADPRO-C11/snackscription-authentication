package snackscription.authentication.service;

import org.springframework.stereotype.Service;
import snackscription.authentication.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO register(UserDTO registrationRequest){
       return null;
    }

    @Override
    public UserDTO login(UserDTO loginRequest){
        return null;
    }

    @Override
    public UserDTO refreshToken(UserDTO refreshTokenRequest){
        return null;
    }

    @Override
    public UserDTO getAllUser(){
       return null;
    }

    @Override
    public UserDTO getUserById(String id){
        return null;
    }

    @Override
    public UserDTO deleteUser(String id){
        return null;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO updatedUser){
        return null;
    }
}
