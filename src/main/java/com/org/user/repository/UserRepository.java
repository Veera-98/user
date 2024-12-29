
package com.org.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	
	

}
