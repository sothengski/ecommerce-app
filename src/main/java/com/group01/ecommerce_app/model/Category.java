package com.group01.ecommerce_app.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
@Builder
@Entity
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "active")
   	private boolean isActive;
    
    @Column(name = "UserID")
   	private long userId;
    
//    public Category() {
//    	this.name = "";
//    	this.userId = 0;
//    	this.isActive = false;
//    }
    
    public Category(String name, long userId, boolean isActive) {
    	this.name = name;
    	this.userId = userId;
    	this.isActive = isActive;
    }
}