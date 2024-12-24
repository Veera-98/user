package com.org.user1.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users_data")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false)
	@NotNull(message = "First Name is required")
	private String firstName;

	@Column(nullable = false)
	@NotNull(message = "Last name is required.")
	private String lastName;

	@Column(nullable = false)
	@NotNull(message = "Password is required.")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).*$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
	private String password;

	@Column(nullable = false)
	@NotNull(message = "Role is required.")
	private String role;

	@Column(nullable = false)
	@NotNull(message = "Customer ID is required.")
	private String customerId;

	@Column
	private String designation;

	@Column(unique = true, nullable = false)
	@NotNull(message = "Phone number is required.")
	@Pattern(regexp = "^[1-9][0-9]*$", message = "Phone number cannot start with 0 and must be numeric.")
	private String phone;

	@Column(unique = true, nullable = false)
	@NotNull(message = "Email is required.")
	@Email(message = "Please enter a valid email address.")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$", message = "Please enter a professional email address.")
	private String email;

	@Column
	private String middleName;

	private String status;
	private Date createdAt;
	private Date updatedAt;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
