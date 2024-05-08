package snackscription.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import snackscription.authentication.dto.UserDTO;
import snackscription.authentication.model.User;
import snackscription.authentication.repository.UserRepository;
import snackscription.authentication.utils.JWTUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final JWTUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JWTUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO register(UserDTO registrationRequest){
        UserDTO response = new UserDTO();

        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setName(registrationRequest.getName());
            user.setRole(registrationRequest.getRole());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            User userResult = userRepository.save(user);
            response.setUser(userResult);
            response.setMessage("Register success!");
            response.setStatusCode(201);
        } catch(Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO login(UserDTO loginRequest){
        UserDTO response = new UserDTO();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String jwt = jwtUtils.generateToken(user);
                String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRole(user.getRole());
                response.setRefreshToken(refreshToken);
                response.setExpirationTime("24hrs");
                response.setMessage("Login success!");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found for email: " + loginRequest.getEmail());
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO refreshToken(UserDTO refreshTokenRequest){
        UserDTO response = new UserDTO();
        try{
            String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User user = userRepository.findByEmail(email).orElseThrow();
            if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)){
                var jwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getRefreshToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Refresh token success!");
            }
            response.setStatusCode(200);

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO getAllUser(){
        UserDTO response = new UserDTO();
        try {
            List<User> result = userRepository.findAll();
            if(!result.isEmpty()){
                response.setUserList(result);
                response.setStatusCode(200);
                response.setMessage("Get all user success!");
            } else {
                response.setStatusCode(404);
                response.setMessage("No users found!");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO getUserById(String id){
        UserDTO response = new UserDTO();
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            response.setUser(user);
            response.setStatusCode(200);
            response.setMessage("User with id " + id + " is found!");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO deleteUser(String id){
        UserDTO response = new UserDTO();
        try{
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                userRepository.deleteById(id);
                response.setStatusCode(200);
                response.setMessage("Delete user success!");
            } else {
                response.setStatusCode(404);
                response.setMessage("User not found!");
            }
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO updatedUser){
        UserDTO response = new UserDTO();
        try {
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                User existingUser = user.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setRole(updatedUser.getRole());

                if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = userRepository.save(existingUser);
                response.setUser(savedUser);
                response.setStatusCode(200);
                response.setMessage("User update success!");
            } else{
                response.setStatusCode(404);
                response.setMessage("User not found!");
            }
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
