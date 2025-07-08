package com.klu;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
public class Service {

	
	@Autowired
	UserDetailsRepo user;
    
    public UserDetails saveUser(UserDetails u) {
        return user.save(u);
    }

    public Optional<UserDetails> getUserByEmail(String email) {
        return user.findById(email);
    }

    public List<UserDetails> getAllUsers() {
        return user.findAll();
    }

    public UserDetails updateUser(UserDetails userDetails) {
        Optional<UserDetails> existingOpt = user.findById(userDetails.getEmail());

        if (existingOpt.isPresent()) {
            UserDetails existing = existingOpt.get();

            // Check each field individually and update only if new value is not null
            if (userDetails.getPassword() != null) {
                existing.setPassword(userDetails.getPassword());
            }
            if (userDetails.getName() != null) {
                existing.setName(userDetails.getName());
            }
            if (userDetails.getPhone() != null) {
                existing.setPhone(userDetails.getPhone());
            }
            if (userDetails.getAddress() != null) {
                existing.setAddress(userDetails.getAddress());
            }

            return user.save(existing);
        } else {
            throw new RuntimeException("User not found with email: " + userDetails.getEmail());
        }
    }

    
    public void deleteUserByEmail(String email) {
        user.deleteById(email);
    }
}
