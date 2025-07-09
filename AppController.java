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
    
    @PostMapping("/product/details/save")
    public ResponseEntity<Product> saveProduct(@RequestBody Product p) {
        Product saved = service.saveProduct(p);
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/productWithImage/upload")
    public ResponseEntity<Product> saveProductWithImage(
            @RequestPart("product") Product product,
            @RequestPart("file") MultipartFile file) {

        try {
            product.setImageData(file.getBytes());
            Product savedProduct = service.saveProduct(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/productImage/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return service.getProductImage(id)
                .map(img -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_JPEG);
                    return new ResponseEntity<>(img.getImageData(), headers, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            service.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully with ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Error: " + e.getMessage());
        }
    }
    
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
    
    @PostMapping("/comment/save")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        Comment savedComment = service.saveComment(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PostMapping("/comment/saveWithImage")
    public ResponseEntity<Comment> saveCommentWithImage(
            @RequestPart("comment") Comment c,
            @RequestPart("File") MultipartFile file) {

        try {
            c.setImageData(file.getBytes());
            Comment savedComment =service.saveComment(c);
            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        service.deleteComment(id);
        return new ResponseEntity<>("Comment deleted successfully!", HttpStatus.OK);
    }

    @PutMapping("/comment/update")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        Comment updated = service.updateComment(comment);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/comment/get/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment comment = service.getCommentById(id);
        return comment != null
                ? new ResponseEntity<>(comment, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
