package com.org.user1.controller;

import com.org.user1.exception.ConflictException;
import com.org.user1.model.User;
import com.org.user1.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("v1/users_data")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User createdUser = userService.createUserService(user);
			Map<String, Object> response = new HashMap<>();
			response.put("message", "201 Created: User created successfully");
			response.put("user", createdUser);
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (BadRequestException | ConflictException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("v1/users_data/search")
	public ResponseEntity<?> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		try {
			Page<User> users = userService.getUsers(page, limit);
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<>("400 Bad Request : Invalid query parameters", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("v1/users_data/{user_id}")
	public ResponseEntity<?> updateUser(@PathVariable Long user_id, @RequestBody User updateUser) {
		try {
			User updatedUser = userService.updateUserService(user_id, updateUser);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} catch (BadRequestException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NotFoundException e) {
			return new ResponseEntity<>("404 Not Found: User not found ", HttpStatus.NOT_FOUND);
		}
	}

	@PatchMapping("v1/users_data/{user_id}")
	public ResponseEntity<?> updateStatus(@PathVariable Long user_id, @RequestParam String status) {
		try {
			if (!status.equals("enabled") && !status.equals("disabled")) {
				return new ResponseEntity<>("Invalid status. Use 'enabled' or 'disabled'.", HttpStatus.BAD_REQUEST);
			}
			User updatedUser = userService.updateStatus(user_id, status);
			return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
	}
}
