package com.hys.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hys.blog.model.CommonDto;
import com.hys.blog.model.User;
import com.hys.blog.service.BlogService;

@RestController
public class BlogController {
	
	@Autowired 
	BlogService blogService;

	@GetMapping({"","/"})
	public String index() {
		return "<h1>메인화면페이지입니다.</h1>";
	}
	
	@PostMapping("/join")
	public CommonDto<?> join(@Valid @RequestBody User user, BindingResult bindingResult) {
		return new CommonDto<>(HttpStatus.CREATED.value(),blogService.join(user));
	}
	
	@PostMapping("/api/blog/user")
	public String user() {
		return "user";
	}
	
	// admin 권한만 접근 가능
	@PostMapping("/api/blog/admin")
	public String admin() {
		return "admin";
	}
	
}
