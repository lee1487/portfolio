package com.hys.blog.dao.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.hys.blog.model.User;

@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE) // 실제 DB테스트 Replace.NONE, 내장 DB테스트 Replace.ANY -> 내장 DB가 설정되어 있어야 함
@DataJpaTest
public class UserRepositoryUnitTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void save_test() {
		// given
		User user = new User(null, "lee1487", "1234", null, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", null, null);
		
		// when 
		User userEntity = userRepository.save(user);
		
		assertEquals(1L, userEntity.getId());
		
	}
}
