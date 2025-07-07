package com.programmers.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

// 🌕 회원가입 시 생성 폼
@Getter
@Setter
public class AuthCreateForm {

	@NotEmpty(message = "아이디는 필수 항목입니다.")
	private String id;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String pw;
	
	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String checkedPw;
	
	@NotEmpty(message = "이름은 필수 항목입니다.")
	private String name;
	
	private String tel;
	
}
