package com.hys.blog.service.serviceImpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hys.blog.dao.repository.UserRepository;
import com.hys.blog.model.RoleType;
import com.hys.blog.model.User;
import com.hys.blog.service.BlogService;

@Service("blogService")
public class BlogServiceImpl implements BlogService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	@Override
	public User join(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.ROLE_USER);
		
		return userRepository.save(user);
	}

	
}
