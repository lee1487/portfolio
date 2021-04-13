package com.hys.blog.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hys.blog.config.auth.PrincipalDetails;
import com.hys.blog.model.User;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login이 더이상 동작 안하기 때문에 principalDetailsService가 동학하도록 필터를 만들어야함 여기서! 

// 원래 이 필터는 /login이라 요청하면 username,password를 post로 전송하면 동작함
// 근데 지금은 formLogin 막아놨기때문에 사용 불가능한데 이것을 다시 SecurityConfig에 addFilter하면 작동함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// 1. username, password 받아서
		// 2. 정상인지 로그인 시도를 해보는 것이다. authenticationManager로 로그인 시도를 하면!! 
		// principalDetailsService가 호출 loadUserByUsername()함수 실행됨.
		// 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
		// 4. JWT 토큰을 만들어서 응답해주면 됨.
		System.out.println("JwtAuthenticationFilter 진입");
		try {
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// principalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨.
			// DB에 있는 username과 password가 일치한다.
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는 거임
			return authentication;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨.
	// 이때 JWT 토큰을 만들어서 request 요청한 사용자에게 JWT토큰을 response해주면 된다.
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		// RSA 방식은 아니고 Hash암호방식
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX +jwtToken);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		System.out.println("인증실패: " + failed.getMessage());
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
}
