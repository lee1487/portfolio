package com.hys.blog.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hys.blog.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> findByUsername(String username);
}
