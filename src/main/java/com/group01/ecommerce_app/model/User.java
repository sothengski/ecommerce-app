package com.group01.ecommerce_app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "user_name", nullable = true, unique = true, length = 100)
    private String userName;

    @Column(name = "first_name", nullable = true, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = true, length = 100)
    private String lastName;

    @Column(name = "phone", nullable = true, length = 9)
    private String phone;

    @Column(name = "shippingAddress", nullable = true, length = 100)
    private String shippingAddress;

    // @JsonIgnore // This will exclude `password` from all JSON responses
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = true)
    private Role role;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    // Constructors
    // public User() {
    // }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        this.products.add(product);
        product.setUser(this);
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public User(Boolean active) {
        this.active = active;
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = "";
        this.lastName = "";
        this.phone = "";
        this.shippingAddress = "";
        this.active = false;
    }

    public User(String firstName, String lastName, String phone, String address) {
        this.firstName = Objects.requireNonNullElse(firstName, "");
        this.lastName = Objects.requireNonNullElse(lastName, "");
        this.phone = Objects.requireNonNullElse(phone, "");
        this.shippingAddress = Objects.requireNonNullElse(address, "");
    }

    // Getters and Setters
    // public Long getId() {
    // return id;
    // }

    // public void setId(Long id) {
    // this.id = id;
    // }

    // public String getEmail() {
    // return email;
    // }

    // public void setEmail(String email) {
    // this.email = email;
    // }

    // public String getPassword() {
    // return password;
    // }

    // public void setPassword(String password) {
    // this.password = password;
    // }

    // public Role getRole() {
    // return role;
    // }

    // public void setRole(Role role) {
    // this.role = role;
    // }

    // public Date getCreatedAt() {
    // return createdAt;
    // }

    // public void setCreatedAt(Date createdAt) {
    // this.createdAt = createdAt;
    // }

    // public Date getUpdatedAt() {
    // return updatedAt;
    // }

    // public void setUpdatedAt(Date updatedAt) {
    // this.updatedAt = updatedAt;
    // }

    // public String getFirstName() {
    // return firstName;
    // }

    // public void setFirstName(String firstName) {
    // this.firstName = firstName;
    // }

    // public String getLastName() {
    // return lastName;
    // }

    // public void setLastName(String lastName) {
    // this.lastName = lastName;
    // }

    // public String getPhone() {
    // return phone;
    // }

    // public void setPhone(String phone) {
    // this.phone = phone;
    // }

    // public String getShippingAddress() {
    // return shippingAddress;
    // }

    // public void setShippingAddress(String shippingaddress) {
    // this.shippingAddress = shippingaddress;
    // }

    // public Boolean getActive() {
    // return active;
    // }

    // public void setActive(Boolean active) {
    // this.active = active;
    // }

}

// @Column(nullable = false)
// private Date date_birth;

// private String city;

// private String state;

// private int zipCode;
// private int start_hour; // ?? time ? e.g: 19:50
// private int end_hour; // ?? time ? e.g: 19:50

// @Enumerated(EnumType.STRING)
// private Specialty specialty;

// @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
// @JoinTable(name = "USER_ROLE", joinColumns = {
// @JoinColumn(name = "USER_ID")
// }, inverseJoinColumns = {
// @JoinColumn(name = "ROLE_ID")
// }

// )
// private Set<Role> role;

// @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
// private List<Appointment> appointments;

// /*
// * @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
// * private List<Review> reviews;
// *
// *
// * @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
// * private List<Report> reports;
// */

// public Set<Role> getRole() {
// return role;
// }

// public void setRole(Set<Role> role) {
// this.role = role;
// }

// "shippingAddress":"123 Main St",

// "shippingCity":"San Francisco",

// "shippingState":"CA",

// "shippingPostalCode":"94105",

// "shippingCountry":"USA",