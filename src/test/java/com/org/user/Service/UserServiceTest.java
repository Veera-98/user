package com.org.user.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.org.user.exception.ConflictException;
import com.org.user.model.User;
import com.org.user.repository.UserRepository;
import com.org.user.service.UserService;



public class UserServiceTest {

	@InjectMocks
	private UserService uService;

	@Mock
	private UserRepository uRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void testCreateUserSuccess() throws DataIntegrityViolationException, BadRequestException {
		User newUser = new User();
		newUser.setFirstName("Dinesh");
		uService.createUserService(newUser);

	}

	@Test
	public void testCreateUserThrowsConflictOnEmailDuplication() throws DataIntegrityViolationException, BadRequestException {
		User newUser = new User();
		newUser.setFirstName("Veera Dinesh");
		when(uRepository.save(newUser)).thenThrow(new DataIntegrityViolationException("Key (email)"));

		ConflictException exception = assertThrows(ConflictException.class, () -> {
			uService.createUserService(newUser);
		});
	}

	@Test
	public void testCreateUserThrowsConflictOnPhoneDuplication() throws DataIntegrityViolationException, BadRequestException {
		User newUser = new User();
		newUser.setFirstName("Dinesh");
		when(uRepository.save(newUser)).thenThrow(new DataIntegrityViolationException("Key (phone)"));

		ConflictException exception = assertThrows(ConflictException.class, () -> {
			uService.createUserService(newUser);
		});
	}

	@Test
	public void testCreateUserThrowsBadRequestOnUnknownKey() throws DataIntegrityViolationException, BadRequestException {
		User  newUser= new User();
		newUser.setFirstName("VeeraDinesh");
		when(uRepository.save(newUser)).thenThrow(new DataIntegrityViolationException("Key (phon)"));

		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			uService.createUserService(newUser);
		});
	}

	@Test
	public void testGetUsersWithPagination() throws BadRequestException {
		int page = 1;
		int limit = 10;
		Pageable pageable = PageRequest.of(page - 1, limit);
		List<User> userList = new ArrayList<User>();
		User user = new User();
		userList.add(user);
		Page<User> mockPage = new PageImpl<>(userList, PageRequest.of(0, 2), 10);
		when(uRepository.findAll(pageable)).thenReturn(mockPage);
		uService.getUsers(page, limit);
	}

	@Test
	public void testUpdateUserSuccessfully() throws BadRequestException, NotFoundException {
		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setEmail("DINESH@GMAIL.COM");
		existingUser.setPhone("9874563214");

		User updatedUser  = new User();
		updatedUser .setEmail("DINESH@GMAIL.COM");
		updatedUser .setPhone("9874563214");
		updatedUser .setDesignation("Tester1");
		updatedUser .setCustomerId("CUST789");
		updatedUser .setFirstName("Dinesh");
		updatedUser .setLastName("Kumar");

		updatedUser .setPassword("dineshpass");
		updatedUser .setRole("Tester");
		updatedUser .setStatus("enabled");

		long userId = 3;
		Optional<User> o = Optional.ofNullable(existingUser);
		when(uRepository.findById(userId)).thenReturn(o);
		uService.updateUserService(userId, updatedUser );

	}

	@Test
	public void testUpdateUserThrowsConflictOnEmailDuplication() throws DataIntegrityViolationException, BadRequestException {
		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setEmail("DINESH@GMAIL.COM");
		existingUser.setPhone("9874563214");

		User updatedUser  = new User();
		updatedUser .setEmail("DINESH@GMAIL.COM");
		updatedUser .setPhone("9874563214");
		updatedUser .setDesignation("Tester");
		updatedUser .setCustomerId("CUST789");
		updatedUser .setFirstName("Dinesh");
		updatedUser .setLastName("Kumar");
		updatedUser .setPassword("dineshpass");
		updatedUser .setRole("Tester");
		updatedUser .setStatus("enabled");

		long userId = 3;
		when(uRepository.save(updatedUser)).thenThrow(new DataIntegrityViolationException("Key (email)"));

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			uService.updateUserService(userId, updatedUser);
		});
	}
	
	@Test
	public void testUpdateUserThrowsNotFoundWhenUserDoesNotExist() throws DataIntegrityViolationException, BadRequestException {
		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setEmail("VEERADINESH@GMAIL.COM");
		existingUser.setPhone("9874563216");

		User updatedUser  = new User();
		updatedUser .setEmail("VEERADINESH@GMAIL.COM");
		updatedUser .setPhone("9874563216");
		updatedUser .setDesignation("Tester");
		updatedUser .setCustomerId("CUST123");
		updatedUser .setFirstName("Dinesh");
		updatedUser .setLastName("Kumar");
		updatedUser .setPassword("dineshpass");
		updatedUser .setRole("Tester");
		updatedUser .setStatus("enabled");

	    long userId = 3;
	    when(uRepository.findById(userId)).thenReturn(Optional.empty());

	    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
	        uService.updateUserService(userId, updatedUser);
	    });
	}

	@Test
	public void testUpdateStatusSuccessfully() throws NotFoundException {
	    User user = new User();
	    user.setUserId(1L);
	    user.setStatus("enabled");

	    long userId = 1L;
	    String status = "disabled";

	    when(uRepository.findById(userId)).thenReturn(Optional.of(user));
	    when(uRepository.save(user)).thenReturn(user);

	    uService.updateStatus(userId, status);
	}

	@Test
	public void testUpdateStatusThrowsNotFoundWhenUserDoesNotExist() {
	    long userId = 1L;
	    String status = "disabled";

	    when(uRepository.findById(userId)).thenReturn(Optional.empty());

	    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
	        uService.updateStatus(userId, status);
	    });
	}

	@Test
	public void testUpdateStatusWithNullStatus() throws NotFoundException {
	    User user = new User();
	    user.setUserId(1L);
	    user.setStatus("enabled");

	    long userId = 1L;
	    String status = null;

	    when(uRepository.findById(userId)).thenReturn(Optional.of(user));
	    when(uRepository.save(user)).thenReturn(user);

	    uService.updateStatus(userId, status);
	}

	
}
