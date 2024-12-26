package com.org.user1.service;

import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.user1.exception.ConflictException;
import com.org.user1.model.User;
import com.org.user1.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createUserService(User u) throws BadRequestException, DataIntegrityViolationException {

		try {
			User u1 = userRepository.save(u);

			return u1;
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

	public User updateUserService(Long id, User updateUser) throws BadRequestException, NotFoundException {

		try {
			Optional<User> b = userRepository.findById(id);

			User b1 = b.get();

			if (updateUser.getDesignation() != null) {
				b1.setDesignation(updateUser.getDesignation());
			}
			if (updateUser.getCustomerId() != null) {
				b1.setCustomerId(updateUser.getCustomerId());
			}
			if (updateUser.getEmail() != null) {
				b1.setEmail(updateUser.getEmail());
			}
			if (updateUser.getFirstName() != null) {
				b1.setFirstName(updateUser.getFirstName());
			}
			if (updateUser.getLastName() != null) {
				b1.setLastName(updateUser.getLastName());
			}
			if (updateUser.getPassword() != null) {
				b1.setPassword(updateUser.getPassword());
			}
			if (updateUser.getPhone() != null) {
				b1.setPhone(updateUser.getPhone());
			}
			if (updateUser.getRole() != null) {
				b1.setRole(updateUser.getRole());
			}
			b1.setStatus(updateUser.getStatus());

			User u3 = userRepository.save(b1);
			return u3;
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
		return updateUser;
	}

	public User updateStatus(long user_id, String status) throws NotFoundException {

		User u3 = new User();
		try {
			Optional<User> b = userRepository.findById(user_id);

			User b2 = b.get();
			b2.setStatus(status);
			u3 = userRepository.save(b2);

		} catch (Exception e) {
			String rootcause = e.getMessage();
			if (rootcause.contains("No value present")) {
				throw new NotFoundException();
			}
		}
		return u3;
	}

}
