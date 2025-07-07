package com.programmers.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

// 유저 엔티티 
@Entity
@Getter
@Setter
public class Auth { 
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_auth")
	@SequenceGenerator(name = "seq_auth", sequenceName = "SEQ_AUTH", allocationSize = 1)
	private Integer userId;
	
	@Column(unique = true)
	private String id;
	
	@Column
	private String pw;
	
	@Column
	private String name;
	 
	@Column
	private String tel;
	
	@Column(name = "role", insertable = false)
	private Integer role;
	
	@Column
	private Integer score;
	
	@Column
	private Integer countOfCompile;
	
}
