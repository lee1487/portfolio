package com.hys.blog.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hys.blog.model.CommonDto;

@Component
@Aspect
public class BindingAdvice {
	
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);
	
	@Before("execution(* com.hys.blog.controller..*Controller.*(..))")
	public void testCheck() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println("주소 : " + request.getRequestURI());
		// request 값 처리 못하나요?
		// log 처리는? 파일로 남기죠?
		System.out.println("성공시 전처리 로그를 남겼습니다.");
	}
	
	@After("execution(* com.hys.blog.controller..*Controller.*(..))")
	public void testCheck2() {
		System.out.println("성공시 후처리 로그를 남겼습니다.");
	}

	@Around("execution(* com.hys.blog.controller..*Controller.*(..))") // com.hys.blog.controller 안에 모든 컨트롤러의 모든 함수에 적용
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		//어떤 Controller type 의 어떤 method인지 찾기 
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();
		
		// method의 모든 파라미터를 받음 
		Object[] args = proceedingJoinPoint.getArgs();
				
		// 받은 파라미터가 bindingResult일때 validation check할 수 있게 로직 구성 
		for (Object arg : args) {
			
			// 서비스 : 정상적인 화면 -> 사용자요청 
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨 error, warn, info, debug
						log.warn(type + "." + method + "() => 필드 : " + error.getField() + ", 메세지: " + error.getDefaultMessage());
						// DB연결 -> DB남기기
						// File file = new File();
					}
					
					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(),errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라
	}
}
