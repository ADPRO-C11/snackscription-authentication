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
        var response = userService.register(request);
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO request){
        var response = userService.login(request);
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        } else if(response.getStatusCode() == 404){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDTO> refresh(@RequestBody UserDTO request){
        var response = userService.refreshToken(request);
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDTO> getAllUser(){
        var response = userService.getAllUser();
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        else if(response.getStatusCode() == 404){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId){
       var response = userService.getUserById(userId);

       if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/useradmin/update/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String userId, @RequestBody UserDTO request){
        var response = userService.updateUser(userId, request);
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        else if(response.getStatusCode() == 404){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/useradmin/delete/{userId}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String userId){
        var response = userService.deleteUser(userId);
        if(response.getStatusCode() == 500){
            return ResponseEntity.internalServerError().body(response);
        }
        else if(response.getStatusCode() == 404){
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
