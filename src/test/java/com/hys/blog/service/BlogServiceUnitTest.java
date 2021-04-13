package com.hys.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hys.blog.dao.repository.UserRepository;
import com.hys.blog.model.RoleType;
import com.hys.blog.model.User;
import com.hys.blog.service.serviceImpl.BlogServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BlogServiceUnitTest {

	@InjectMocks
	private BlogServiceImpl blogService;
	
	@Mock
	private UserRepository userRepository;
	
	// 실제 기능을 그대로 쓰고 싶을 때 사용 {@ExtendWith, @Mock, @Spy}
	@Spy
	private BCryptPasswordEncoder encoder;
	
	
	
	
	@Test
	public void join_test() {
		// given
		User user = new User(null, "lee1487", "1234", null, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", null, null);
		//user 값이 ServiceImpl에서 적용되는 값이 반영된다 
	
		// stub - 동작 지정
		LocalDateTime now = LocalDateTime.now();
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		System.out.println(encPassword);
		when(userRepository.save(user)).thenReturn(new User(1L, "lee1487", encPassword, RoleType.ROLE_USER, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", now, null));

		// test execute
		User userEntity = blogService.join(user); // when().thenReturn값이 저장됨
		
		// then
		assertEquals(userEntity.getRole(),user.getRole());
		assertEquals(encoder.matches(rawPassword, userEntity.getPassword()), encoder.matches(rawPassword, user.getPassword()));
		
	}
}
