package com.org.user1.Service;

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

import com.org.user1.exception.ConflictException;
import com.org.user1.model.User;
import com.org.user1.repository.UserRepository;
import com.org.user1.service.UserService;

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
	public void createUserServiceTest1() throws DataIntegrityViolationException, BadRequestException {
		User u = new User();
		u.setFirstName("Dinesh");
		uService.createUserService(u);

	}

	@Test
	public void createUserServiceTest2() throws DataIntegrityViolationException, BadRequestException {
		User u = new User();
		u.setFirstName("Dinesh");
		when(uRepository.save(u)).thenThrow(new DataIntegrityViolationException("Key (email)"));

		ConflictException exception = assertThrows(ConflictException.class, () -> {
			uService.createUserService(u);
		});
	}

	@Test
	public void createUserServiceTest3() throws DataIntegrityViolationException, BadRequestException {
		User u = new User();
		u.setFirstName("Dinesh");
		when(uRepository.save(u)).thenThrow(new DataIntegrityViolationException("Key (phone)"));

		ConflictException exception = assertThrows(ConflictException.class, () -> {
			uService.createUserService(u);
		});
	}

	@Test
	public void createUserServiceTest4() throws DataIntegrityViolationException, BadRequestException {
		User u = new User();
		u.setFirstName("Dinesh");
		when(uRepository.save(u)).thenThrow(new DataIntegrityViolationException("Key (phon)"));

		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			uService.createUserService(u);
		});
	}

	@Test
	public void getUsersTest1() throws BadRequestException {
		int page = 1;
		int limit = 10;
		Pageable pageable = PageRequest.of(page - 1, limit);
		List<User> u = new ArrayList<User>();
		User user = new User();
		u.add(user);
		Page<User> mockPage = new PageImpl<>(u, PageRequest.of(0, 2), 10);
		when(uRepository.findAll(pageable)).thenReturn(mockPage);
		uService.getUsers(page, limit);
	}

//	@Test
//	public void getUsersTest2() throws BadRequestException {
//		int page = 0;
//		int limit = 10;
//		uService.getUsers(page, limit);
//		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
//			uService.getUsers(page, limit);
//		});
//	}

	@Test
	public void updateUserServiceTest1() throws BadRequestException, NotFoundException {
		User user1 = new User();
		user1.setUserId(1L);
		user1.setEmail("DINESH@GMAIL.COM");
		user1.setPhone("9874563214");

		User user2 = new User();
		user2.setEmail("DINESH@GMAIL.COM");
		user2.setPhone("9874563214");
		user2.setDesignation("Tester");
		user2.setCustomerId("CUST789");
		user2.setFirstName("Dinesh");
		user2.setLastName("Kumar");

		user2.setPassword("dineshpass");
		user2.setRole("Tester");
		user2.setStatus("enabled");

		long s = 3;
		Optional<User> o = Optional.ofNullable(user1);
		when(uRepository.findById(s)).thenReturn(o);
		uService.updateUserService(s, user2);

	}

	@Test
	public void updateUserServiceTest2() throws DataIntegrityViolationException, BadRequestException {
		User user1 = new User();
		user1.setUserId(1L);
		user1.setEmail("DINESH@GMAIL.COM");
		user1.setPhone("9874563214");

		User user2 = new User();
		user2.setEmail("DINESH@GMAIL.COM");
		user2.setPhone("9874563214");
		user2.setDesignation("Tester");
		user2.setCustomerId("CUST789");
		user2.setFirstName("Dinesh");
		user2.setLastName("Kumar");

		user2.setPassword("dineshpass");
		user2.setRole("Tester");
		user2.setStatus("enabled");

		long s = 3;
		when(uRepository.save(user2)).thenThrow(new DataIntegrityViolationException("Key (email)"));

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			uService.updateUserService(s, user2);
		});
	}
	
	@Test
	public void updateUserServiceTest3() throws DataIntegrityViolationException, BadRequestException {
	    User user1 = new User();
	    user1.setUserId(1L);
	    user1.setEmail("DINESH@GMAIL.COM");
	    user1.setPhone("9874563214");

	    User user2 = new User();
	    user2.setEmail("DINESH@GMAIL.COM");
	    user2.setPhone("9874563214");
	    user2.setDesignation("Tester");
	    user2.setCustomerId("CUST789");
	    user2.setFirstName("Dinesh");
	    user2.setLastName("Kumar");
	    user2.setPassword("dineshpass");
	    user2.setRole("Tester");
	    user2.setStatus("enabled");

	    long s = 3;
	    when(uRepository.findById(s)).thenReturn(Optional.empty());

	    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
	        uService.updateUserService(s, user2);
	    });
	}

	@Test
	public void updateStatusTest1() throws NotFoundException {
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
	public void updateStatusTest2() {
	    long userId = 1L;
	    String status = "disabled";

	    when(uRepository.findById(userId)).thenReturn(Optional.empty());

	    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
	        uService.updateStatus(userId, status);
	    });
	}

	@Test
	public void updateStatusTest3() throws NotFoundException {
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
