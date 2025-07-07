package com.programmers.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
				.requestMatchers("/auth/login", "/auth/signup", "/auth/find/password", "/auth/find/password/complete",
						"/auth/find/id", "/auth/find/id/complete")
				.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/auth/login").usernameParameter("id").passwordParameter("pw")
						.defaultSuccessUrl("/"))
				.logout(logout -> logout
			            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
			            .logoutSuccessUrl("/")
			            .invalidateHttpSession(true)
			        );
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 🌕 AuthenticationManager :
	// 스프링 시큐리티의 인증처리
	// UserSecurityService와 PasswordEncoder를 내부적으로 사용하여 인증과 권한 부여 프로세스 처리
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
