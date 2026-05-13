package com.greencore.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 180)
    private String email;
    
    @Column(length = 255)
    private String profilePicture;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String roles;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Materiel> materiels = new ArrayList<>();
    
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Operation> requestedOperations = new ArrayList<>();
    
    public User() {}
    
    public User(String email, String password, List<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = rolesToJson(roles);
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public List<String> getRoles() {
        return jsonToRoles(roles);
    }
    
    public void setRoles(List<String> roles) {
        this.roles = rolesToJson(roles);
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Materiel> getMateriels() {
        return materiels;
    }
    
    public void setMateriels(List<Materiel> materiels) {
        this.materiels = materiels;
    }
    
    public List<Operation> getRequestedOperations() {
        return requestedOperations;
    }
    
    public void setRequestedOperations(List<Operation> requestedOperations) {
        this.requestedOperations = requestedOperations;
    }
    
    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
    
    // Helper methods for JSON conversion
    private String rolesToJson(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "[]";
        }
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < roles.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(roles.get(i)).append("\"");
        }
        json.append("]");
        return json.toString();
    }
    
    private List<String> jsonToRoles(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) {
            return new ArrayList<>();
        }
        // Simple JSON parsing for array of strings
        List<String> roles = new ArrayList<>();
        String content = json.trim().substring(1, json.length() - 1); // Remove [ ]
        String[] parts = content.split(",");
        for (String part : parts) {
            String role = part.trim().replace("\"", "");
            if (!role.isEmpty()) {
                roles.add(role);
            }
        }
        return roles;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", roles=" + getRoles() +
                '}';
    }
}
