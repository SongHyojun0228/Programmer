package com.programmers.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthSecurityService implements UserDetailsService {

	private final AuthRepository authRepository;

	// username = id
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Auth> _user = this.authRepository.findById(username);

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		if (_user.isEmpty()) {
			throw new UsernameNotFoundException("시용자를 찾을 수 없습니다.");
		}
		Auth user = _user.get();
		
		if(1 == user.getRole()) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		
		System.out.println("🌕 로그인 성공");
		System.out.println("🌕 >>> ID : " + user.getId());
		System.out.println("🌕 >>> NAME : " + user.getName());
		System.out.println("🌕 >>> TEL : " + user.getTel());

		return new User(user.getId(), user.getPw(), authorities);
	}
	
}
