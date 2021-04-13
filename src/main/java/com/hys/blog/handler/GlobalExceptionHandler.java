package com.hys.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(value=IllegalArgumentException.class)
	public ResponseEntity<String> handleArgumentException(IllegalArgumentException e) {
		return new ResponseEntity<String>("<h1>ERROR</h1>", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	// method에서   throw new Exception("errormessage")하면 됨
	
}
