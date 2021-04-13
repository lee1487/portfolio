package com.hys.blog.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.blog.config.auth.PrincipalDetailsService;
import com.hys.blog.dao.repository.UserRepository;
import com.hys.blog.model.RoleType;
import com.hys.blog.model.User;
import com.hys.blog.service.BlogService;

/**
 * 컨트롤러만 테스트하는 법
 * WebMvc 어노테이션
 * (1) AutoConfigureMockMvc 내장
 * (2) Controller, ControllerAdvice, JsonComponent, Filter, WebMvcConfigurer 등을 빈으로 올린다.
 */
@WebMvcTest(controllers = {BlogController.class}, includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@AutoConfigureMockMvc(addFilters = false)
public class BlogControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BlogService blogService;
	
	@MockBean
	private PrincipalDetailsService principalDetailsService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private CorsFilter corsFilter;
	
	@Test
	public void join_test() throws Exception {
		// given
		LocalDateTime now = LocalDateTime.now();
		
		User user = new User(null, "lee1487", "1234", null, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", null, null);
		//Json데이터 만들기 
		String content = new ObjectMapper().writeValueAsString(user);
		
		// return값 미리 여기서 만듦
		User getTimeUser = new User(1L, "lee1487", "1234", RoleType.ROLE_USER, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", now, null);
		when(blogService.join(user)).thenReturn(getTimeUser);
		
		// when (테스트 실행) 
		ResultActions resultAction = mockMvc.perform(post("/join").contentType(MediaType.APPLICATION_JSON_UTF8)
							.content(content).accept(MediaType.APPLICATION_JSON_UTF8));
		System.out.println("creDTe : " + now);
		resultAction.andExpect(status().isOk())
						.andExpect(jsonPath("$.data.membNm").value("이현세"))
						.andExpect(jsonPath("$.data.creDte").value(now.toString()))
						.andDo(MockMvcResultHandlers.print());
					
		
		
	}
}
