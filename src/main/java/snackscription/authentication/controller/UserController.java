package snackscription.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snackscription.authentication.dto.UserDTO;

@RestController
public class UserController {

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO request){
        return null;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request){
        return null;
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refresh(@RequestBody UserDTO request){
        return null;
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUser(){
        return null;
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
        return null;
    }

    @PutMapping("/useradmin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO request){
        return null;
    }

    @DeleteMapping("/useradmin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String userId){
        return null;
    }
}
