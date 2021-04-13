package com.hys.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.hys.blog.config.auth.PrincipalDetailsService;
import com.hys.blog.config.jwt.JwtAuthenticationFilter;
import com.hys.blog.config.jwt.JwtAuthorizationFilter;
import com.hys.blog.dao.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CorsFilter corsFilter;
	
	// 원하는 api에서 로그인 하기 위해서는 AuthenticationManager를 밖에서 사용해야 되는데 이때 이걸 해줘야 밖에서 사용 가능 하다.
	// 사용법 https://ziponia.github.io/2019/05/26/spring-security-authenticationmanager.html 참조 
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
	// 이 메소드는 인증을 담당할 프로바이더 구현체를 설정하는 메소드 이다!!!
	// 현재는 기본 DaoAuthenticationProvider를 사용하게 하고 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	// 세션 사용하지 않겠다.
		.and()
		.addFilter(corsFilter) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증 (O)
		.formLogin().disable()
		.httpBasic().disable()
		.addFilter(new JwtAuthenticationFilter(authenticationManager())) //JwtAuthenticationFilter를 추가하면 AuthenticationManager를 parameter로 던져야함 그래서  JwtAuthenticationFilter에
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
		.authorizeRequests()
		.antMatchers("/api/blog/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/blog/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();
		
	}
}
