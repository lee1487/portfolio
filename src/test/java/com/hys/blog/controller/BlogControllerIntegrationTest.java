package com.hys.blog.controller;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.blog.model.RoleType;
import com.hys.blog.model.User;
import com.hys.blog.service.BlogService;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BlogControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE user AUTO_INCREMENT = 1").executeUpdate();
	}
	
	@Test
	public void join_test() throws Exception {
		// given
		String content = new ObjectMapper().writeValueAsString(new User(null, "lee1487", "1234", null, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", null, null));
		
		// when (테스트 실행) 
		ResultActions resultAction = mockMvc.perform(post("/join").contentType(MediaType.APPLICATION_JSON_UTF8)
							.content(content).accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then (검증)
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.data.membNm").value("이현세"))
						.andDo(MockMvcResultHandlers.print());
					
		
		
	}
}
