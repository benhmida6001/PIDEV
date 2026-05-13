package com.greencore.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materiel")
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(length = 255)
    private String image;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private Integer availableQuantity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User owner;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean transport = false;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean visible = true;
    
    @OneToMany(mappedBy = "materiel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Operation> operations = new ArrayList<>();
    
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public Materiel() {}
    
    public Materiel(String name, String description, Integer quantity, Status status, 
                   Integer availableQuantity, Category category, User owner) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.availableQuantity = availableQuantity;
        this.category = category;
        this.owner = owner;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    
    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Boolean getTransport() {
        return transport;
    }
    
    public void setTransport(Boolean transport) {
        this.transport = transport;
    }
    
    public Boolean getVisible() {
        return visible;
    }
    
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public List<Operation> getOperations() {
        return operations;
    }
    
    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isAvailable() {
        return availableQuantity > 0;
    }
    
    @Override
    public String toString() {
        return "Materiel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", availableQuantity=" + availableQuantity +
                ", status=" + status +
                '}';
    }
}
