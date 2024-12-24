package com.org.user1.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.org.user1.controller.UserController;
import com.org.user1.exception.ConflictException;
import com.org.user1.model.User;
import com.org.user1.service.UserService;

public class UserControllerTest {

	    @InjectMocks
	    private UserController userController;

	    @Mock
	    private UserService userService;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void createUserTest_Success() throws BadRequestException, ConflictException {
	        User user = new User();
	        user.setFirstName("Dinesh");
	        when(userService.createUserService(user)).thenReturn(user);

	        ResponseEntity<?> response = userController.createUser(user);

	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
	        assertEquals("201 Created: User created successfully", responseBody.get("message"));
	    }

	    @Test
	    public void createUserTest_ConflictException() throws BadRequestException, ConflictException {
	        User user = new User();
	        when(userService.createUserService(user)).thenThrow(new ConflictException("Email already in use"));

	        ResponseEntity<?> response = userController.createUser(user);

	        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	        assertEquals("Email already in use", response.getBody());
	    }

	    @Test
	    public void getUsersTest_Success() throws BadRequestException {
	        List<User> userList = new ArrayList<>();
	        userList.add(new User());
	        Page<User> userPage = new PageImpl<>(userList);
	        when(userService.getUsers(1, 10)).thenReturn(userPage);

	        ResponseEntity<?> response = userController.getUsers(1, 10);

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(userPage, response.getBody());
	    }

	    @Test
	    public void getUsersTest_BadRequestException() throws BadRequestException {
	        when(userService.getUsers(0, 10)).thenThrow(new BadRequestException("Invalid query parameters"));

	        ResponseEntity<?> response = userController.getUsers(0, 10);

	        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	        assertEquals("400 Bad Request : Invalid query parameters", response.getBody());
	    }

	    @Test
	    public void updateUserTest_Success() throws BadRequestException, NotFoundException {
	        User user = new User();
	        user.setFirstName("Dinesh");
	        when(userService.updateUserService(1L, user)).thenReturn(user);

	        ResponseEntity<?> response = userController.updateUser(1L, user);

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(user, response.getBody());
	    }

	    @Test
	    public void updateUserTest_NotFoundException() throws BadRequestException, NotFoundException {
	        User user = new User();
	        when(userService.updateUserService(1L, user)).thenThrow(new NotFoundException());

	        ResponseEntity<?> response = userController.updateUser(1L, user);

	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals("404 Not Found: User not found ", response.getBody());
	    }

	    @Test
	    public void updateStatusTest_Success() throws NotFoundException {
	        User user = new User();
	        user.setStatus("enabled");
	        when(userService.updateStatus(1L, "enabled")).thenReturn(user);

	        ResponseEntity<?> response = userController.updateStatus(1L, "enabled");

	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(user, response.getBody());
	    }

	    @Test
	    public void updateStatusTest_InvalidStatus() {
	        ResponseEntity<?> response = userController.updateStatus(1L, "invalid_status");

	        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	        assertEquals("Invalid status. Use 'enabled' or 'disabled'.", response.getBody());
	    }

	    @Test
	    public void updateStatusTest_NotFoundException() throws NotFoundException {
	        when(userService.updateStatus(1L, "enabled")).thenThrow(new NotFoundException());

	        ResponseEntity<?> response = userController.updateStatus(1L, "enabled");

	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals("User not found", response.getBody());
	    }
	} 
