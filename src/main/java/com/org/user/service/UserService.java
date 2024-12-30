package com.org.user.service;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.user.exception.ConflictException;
import com.org.user.model.User;
import com.org.user.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUserService(User user) throws BadRequestException, DataIntegrityViolationException {

		try {
			User savedUser = userRepository.save(user);

			return savedUser;
		} catch (DataIntegrityViolationException e) {
			String rootcause = e.getMessage();
			if (rootcause.contains("Key (email)")) {
				throw new ConflictException("409 Conflict : Email address already in use");

			} else if (rootcause.contains("Key (phone)")) {
				throw new ConflictException("409 Conflict : Phone Number already in use");

			}
			throw new BadRequestException(e.getMessage());
		}
	}

	public Page<User> getUsers(int page, int limit) throws BadRequestException {
		try {
			Pageable pageable = PageRequest.of(page - 1, limit);
			return userRepository.findAll(pageable);

		} catch (Exception e) {
			throw new BadRequestException("Invalid query Parameters");
		}

	}

	public User updateUserService(Long id, User updatedUser) throws BadRequestException, NotFoundException {

		try {
			Optional<User> optionalExistingUser = userRepository.findById(id);

			User existingUser = optionalExistingUser.get();

			if (updatedUser.getDesignation() != null) {
				existingUser.setDesignation(updatedUser.getDesignation());
			}
			if (updatedUser.getCustomerId() != null) {
				existingUser.setCustomerId(updatedUser.getCustomerId());
			}
			if (updatedUser.getEmail() != null) {
				existingUser.setEmail(updatedUser.getEmail());
			}
			if (updatedUser.getFirstName() != null) {
				existingUser.setFirstName(updatedUser.getFirstName());
			}
			if (updatedUser.getLastName() != null) {
				existingUser.setLastName(updatedUser.getLastName());
			}
			if (updatedUser.getPassword() != null) {
				existingUser.setPassword(updatedUser.getPassword());
			}
			if (updatedUser.getPhone() != null) {
				existingUser.setPhone(updatedUser.getPhone());
			}
			if (updatedUser.getRole() != null) {
				existingUser.setRole(updatedUser.getRole());
			}
			existingUser.setStatus(updatedUser.getStatus());

			User savedUser = userRepository.save(existingUser);
			return savedUser;
		} catch (DataIntegrityViolationException e) {
			String rootcause = e.getMessage();
			if (rootcause.contains("Key (email)")) {
				throw new ConflictException("Email address already in use");

			} else if (rootcause.contains("Key (phone)")) {
				throw new ConflictException("Phone Number already in use");
			}

			throw new BadRequestException(e.getMessage());
		} catch (Exception e) {
			String rootcause = e.getMessage();
			if (rootcause.contains("No value present")) {
				throw new NotFoundException();
			}
		}
		return updatedUser;
	}

	public User updateStatus(long user_id, String status) throws NotFoundException {

		User updatedUser = new User();
		try {
			Optional<User> existingUser = userRepository.findById(user_id);

			User user = existingUser.get();
			user.setStatus(status);
			updatedUser = userRepository.save(user);

		} catch (Exception e) {
			String rootcause = e.getMessage();
			if (rootcause.contains("No value present")) {
				throw new NotFoundException();
			}
		}
		return updatedUser;
	}

}
