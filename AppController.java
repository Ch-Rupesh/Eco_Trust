package com.klu;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class AppController {

    @Autowired
    private Service service;
    
    @PostMapping("/user/save")
    public ResponseEntity<String> saveUser(@RequestBody UserDetails userDetails) {
        try {
            UserDetails saved = service.saveUser(userDetails);
            return ResponseEntity.ok("User saved with email: " + saved.getEmail());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving user: " + e.getMessage());
        }
    }

    @GetMapping("/user/get/{email}")
    public ResponseEntity<UserDetails> getUser(@PathVariable String email) {
        return service.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get/allUsers")
    public ResponseEntity<List<UserDetails>> getAllUsers() {
        List<UserDetails> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/user/update")
    public ResponseEntity<UserDetails> updateUser(@RequestBody UserDetails userDetails) {
        UserDetails updated = service.updateUser(userDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/user/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        try {
            service.deleteUserByEmail(email);
            return ResponseEntity.ok("User deleted: " + email);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }
}
