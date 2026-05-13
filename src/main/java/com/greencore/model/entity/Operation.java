package com.greencore.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus status = OperationStatus.PENDING;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Materiel materiel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User requester;
    
    public Operation() {}
    
    public Operation(OperationType type, Integer quantity, String description, 
                    LocalDate startDate, LocalDate endDate, Materiel materiel, User requester) {
        this.type = type;
        this.quantity = quantity;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.materiel = materiel;
        this.requester = requester;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public OperationType getType() {
        return type;
    }
    
    public void setType(OperationType type) {
        this.type = type;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public OperationStatus getStatus() {
        return status;
    }
    
    public void setStatus(OperationStatus status) {
        this.status = status;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Materiel getMateriel() {
        return materiel;
    }
    
    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
    }
    
    public User getRequester() {
        return requester;
    }
    
    public void setRequester(User requester) {
        this.requester = requester;
    }
    
    public boolean isPending() {
        return status == OperationStatus.PENDING;
    }
    
    public boolean isApproved() {
        return status == OperationStatus.APPROVED;
    }
    
    public boolean isRejected() {
        return status == OperationStatus.REJECTED;
    }
    
    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", type=" + type +
                ", quantity=" + quantity +
                ", status=" + status +
                ", materiel=" + (materiel != null ? materiel.getName() : "null") +
                ", requester=" + (requester != null ? requester.getEmail() : "null") +
                '}';
    }
}
