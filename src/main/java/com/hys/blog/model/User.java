package com.hys.blog.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "아이디를 입력하지 않았습니다.")
	@NotBlank(message = "아이디를 입력하지 않았습니다.")
	@Size(max=20, message="아이디 길이를 초과하였습니다.")
	private String username;
	@NotNull(message = "비밀번호가 없습니다.")
	private String password;
	private RoleType role;
	private String membNm;
	private String email;
	private String moblNo;
	private String zipcd;
	private String addr;
	private String detlAddr;
	
	@CreationTimestamp
	private LocalDateTime creDte;
	private LocalDateTime modDte;
	
	
	 
	
	
	
}
