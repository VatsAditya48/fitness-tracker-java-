package models;

import java.time.LocalDateTime;

/**
 * User model - basic POJO with getters/setters.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private Integer age;
    private Double heightCm;
    private Double weightKg;
    private String membershipType;
    private LocalDateTime createdAt;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }
    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}