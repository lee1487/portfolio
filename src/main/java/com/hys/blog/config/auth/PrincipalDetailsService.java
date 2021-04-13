package com.hys.blog.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hys.blog.dao.repository.UserRepository;
import com.hys.blog.model.User;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	
	private UserRepository userRepository;
	
	public PrincipalDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username)
				.orElseThrow(() -> {
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
				});
		
		
		return new PrincipalDetails(userEntity);
	}

}
