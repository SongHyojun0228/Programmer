package com.programmers.auth;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.programmers.solution.UserProblemSolving;
import com.programmers.solution.UserProblemSolvingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthService authService;
	private final UserProblemSolvingService userProblemSolvingService;

	// 🌕 로그인 페이지
	@GetMapping("/login")
	public String getLogin() {
		return "auth/login";
	}

	// 🌕 회원가입 페이지
	@GetMapping("/signup")
	public String getSignup(AuthCreateForm authCreateForm) {
		return "auth/sign_up";
	}

	// 🌕 회원가입
	@PostMapping("/signup")
	public String postSignup(@Valid AuthCreateForm authCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "auth/sign_up";
		}

		if (!authCreateForm.getPw().equals(authCreateForm.getCheckedPw())) {
			bindingResult.rejectValue("checkedPw", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "auth/sign_up";
		}

		if (this.authService.isExistingUser(authCreateForm.getId()) != null) {
			bindingResult.rejectValue("id", "idIsExisting", "이미 사용 중인 아이디(이메일)입니다.");
			return "auth/sign_up";
		}

		try {
			this.authService.createAuth(authCreateForm.getId(), authCreateForm.getPw(), authCreateForm.getName(),
					authCreateForm.getTel());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 이메일(아이디)입니다.");
			return "auth/sign_up";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "auth/sign_up";
		}

		return "redirect:/auth/login";
	}

	// 🌕 아이디 찾기 페이지
	@GetMapping("/find/id")
	public String getFindId() {
		System.out.println("아이디 찾기 페이지");
		return "auth/find_id";
	}

	// 🌕 아이디 찾기 완료 페이지
	@GetMapping("/find/id/complete")
	public String getFindIdComplete() {
		System.out.println("아이디 찾기 완료 페이지");
		return "auth/complete_find_id";
	}

	// 🌕 비밀번호 찾기 페이지
	@GetMapping("/find/password")
	public String getFindPw() {
		System.out.println("비밀번호 찾기 페이지");
		return "auth/find_pw";
	}

	// 🌕 비밀번호 변경 페이지
	@GetMapping("/find/password/complete")
	public String getFindPwComplete() {
		System.out.println("비밀번호 변경 페이지");
		return "auth/complete_find_pw";
	}

	// 🌕 마이 페이지
	@GetMapping("/mypage")
	public String getMypage(Principal principal, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		System.out.println("마이 페이지");
		Auth user = this.authService.isExistingUser(principal.getName());
		
		List<UserProblemSolving> solvedProblems = this.userProblemSolvingService
				.getSolvedProblemByUserId(user.getUserId());
		
		Page<UserProblemSolving> pagedSolvedProblems = this.userProblemSolvingService
				.getPagedSolvedProblemByUserId(user.getUserId(), page);
		
		model.addAttribute("user", user);
		model.addAttribute("solvedProblems", solvedProblems);
		model.addAttribute("pagedSolvedProblems", pagedSolvedProblems);
		
		
		return "auth/my_page";
	}

}
