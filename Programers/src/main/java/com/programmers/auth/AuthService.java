package com.programmers.auth;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final AuthRepository authRepository;

	public Auth isExistingUser(String id) {
		Optional<Auth> _user = this.authRepository.findById(id);
		if (_user.isEmpty())
			return null;

		return _user.get();
	}

	// 회원가입 -> 유저생성
	void createAuth(String id, String pw, String name, String tel) {
		Auth auth = new Auth();

		auth.setId(id);
		auth.setPw(passwordEncoder.encode(pw));
		auth.setName(name);
		auth.setTel(tel);
		auth.setScore(0);
		auth.setCountOfCompile(0);

		try {
			this.authRepository.save(auth);
			System.out.println("🌕 Auth 테이블에 새 유저 등록");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("🌕 Auth 테이블에 새 유저 등록 실패");
		}
	}
	
	// 점수에 따른 유저 등수
	public Integer getUserRank(Integer userId) {
		Auth user = this.authRepository.getById(userId);
	    int higherScoreCount = this.authRepository.countUsersWithHigherScore(user.getScore());
	    return higherScoreCount + 1;
	}

	// 점수 추가
	@Transactional
	public void addScore(Integer userId, Integer score) {
		Auth user = this.authRepository.getById(userId);
		System.out.println("🌕 유저 정보: " + user.getUserId() + ", " + user.getId());
		System.out.println("🌕 기존 점수 : " + user.getScore());
		System.out.println("🌕 추가 점수 : " + score);
		user.setScore(user.getScore() + score);
		System.out.println("🌕 총 점수 : " + user.getScore());
	}

	// 컴파일 횟수 추가
	@Transactional
	public void addCountOfComplie(Integer userId) {
		Auth user = this.authRepository.getById(userId);
		System.out.println("🌕 현재 컴파일 횟수 : " + user.getCountOfCompile());
		Integer newCountOfComplie = user.getCountOfCompile() + 1;
		user.setCountOfCompile(newCountOfComplie);
		System.out.println("🌕 컴파일 횟수 추가, 현재 컴파일 횟수 : " + user.getCountOfCompile());
	}

}
