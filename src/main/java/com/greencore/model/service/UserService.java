package com.greencore.model.service;

import com.greencore.model.entity.User;
import com.greencore.model.repository.UserRepository;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    
    public UserService() {
        this.userRepository = new UserRepository();
    }
    
    public User register(String email, String password, List<String> roles) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
    
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
            
            if (result.verified) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    public User update(User user) {
        return userRepository.save(user);
    }
    
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    public void updatePassword(User user, String newPassword) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
    
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
