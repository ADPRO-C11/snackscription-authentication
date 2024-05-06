package snackscription.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snackscription.authentication.dto.UserDTO;
import snackscription.authentication.service.UserServiceImpl;

@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO request){
        return ResponseEntity.ok(userServiceImpl.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request){
        return ResponseEntity.ok(userServiceImpl.login(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refresh(@RequestBody UserDTO request){
        return ResponseEntity.ok(userServiceImpl.refreshToken(request));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUser(){
        return ResponseEntity.ok(userServiceImpl.getAllUser());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userServiceImpl.getUserById(userId));
    }

    @PutMapping("/useradmin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO request){
        return ResponseEntity.ok(userServiceImpl.updateUser(userId, request));
    }

    @DeleteMapping("/useradmin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userServiceImpl.deleteUser(userId));
    }
}
