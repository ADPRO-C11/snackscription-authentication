package snackscription.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snackscription.authentication.dto.UserDTO;
import snackscription.authentication.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO request){
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request){
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refresh(@RequestBody UserDTO request){
        return ResponseEntity.ok(userService.refreshToken(request));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/useradmin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO request){
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/useradmin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}
