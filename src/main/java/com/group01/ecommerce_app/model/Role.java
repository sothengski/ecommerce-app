package com.group01.ecommerce_app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "roles")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // , orphanRemoval = true
	@Builder.Default
	private List<User> users = new ArrayList<>();

	public void addUser(User user) {
		users.add(user);
		user.setRole(this);
	}

	// @ElementCollection
	// @CollectionTable(name = "role_images", joinColumns = @JoinColumn(name =
	// "role_id"))
	// @Column(name = "image_url")
	// private List<String> images;

	// Constructors
	// public Role() {
	// }

	public Role(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Role(String name) {
		this.name = name;
	}

	// Getters and Setters
	// public int getId() {
	// return id;
	// }

	// public void setId(int id) {
	// this.id = id;
	// }

	// public String getName() {
	// return name;
	// }

	// public void setName(String name) {
	// this.name = name;
	// }

	// @Override
	// public String toString() {
	// return "Role [id=" + id + ", name=" + name + "]";
	// }
}