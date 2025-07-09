package com.klu;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
public class Service {

	@Autowired
	ProductRepo product;
	@Autowired
	CommentRepo comment;
	@Autowired
	UserDetailsRepo user;
	
	public Product saveProduct(Product p) {
        return product.save(p);
    }
	
	public Product saveProductWithImage(Product p) {
        // save to DB, or wherever
        System.out.println("Saving product: " + p.getName());
        System.out.println("Image size: " + p.getImageData().length + " bytes");
        return p;
    }

    public Optional<Product> getProductImage(Long id) {
        return product.findById(id);
    }
    
    public void deleteProduct(Long id) {
        if (!product.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        product.deleteById(id);
    }
    
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
    
    public Comment saveComment(Comment c) {
        return comment.save(c);
    }
    
    public void deleteComment(Long id) {
        comment.deleteById(id);
    }
    
    public Comment updateComment(Comment c) {
        Optional<Comment> existingOptional = comment.findById(c.getId());
        if (existingOptional.isPresent()) {
            Comment existing = existingOptional.get();

            existing.setEmail(c.getEmail());
            existing.setComment(c.getComment());
            existing.setRating(c.getRating());
            existing.setProductID(c.getProductID());
            existing.setImageData(c.getImageData());

            return comment.save(existing);
        } else {
            throw new RuntimeException("Comment not found with ID: " + c.getId());
        }
    }
    
    public Comment getCommentById(Long id) {
        return comment.findById(id).orElse(null);
    }
}
