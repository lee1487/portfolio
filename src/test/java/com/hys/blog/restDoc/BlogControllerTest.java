package com.hys.blog.restDoc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.blog.model.User;

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080)
public class BlogControllerTest extends AbstractControllerTest {
	
	@Test
	public void join_test() throws Exception {
		User user = new User(null, "lee1487", "1234", null, "이현세", "dlgustp1487@naver.com", "01028461487", "08759", "서울 관악구 관천로 72", "굿모닝오피스텔 706호", null, null);
		
		String content = new ObjectMapper().writeValueAsString(user);
		
		mockMvc.perform(post("/join")
				.content(content)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(restDocumentationResultHandler);
	}
	
	

}
