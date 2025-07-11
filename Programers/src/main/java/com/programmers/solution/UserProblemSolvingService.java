package com.programmers.solution;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmers.auth.Auth;
import com.programmers.auth.AuthRepository;
import com.programmers.problem.Problem;
import com.programmers.problem.ProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProblemSolvingService {

	private final AuthRepository authRepository;
	private final ProblemRepository problemRepository;
	private final UserProblemSolvingRepository userProblemSolvingRepository;

	// 🌕 유저 별 푼 문제 보기 ( 페이징 )
	public Page<UserProblemSolving> getPagedSolvedProblemByUserId(Integer userId, int page) {
		int pageSize = 10;
		int startRow = page * pageSize;
		int endRow = startRow + pageSize;

		List<UserProblemSolving> content = this.userProblemSolvingRepository.getSolvedProblemsByUserIdAndPage(userId,
				startRow, endRow);

		int total = this.userProblemSolvingRepository.countSolvedProblemsByUserId(userId);

		Pageable pageable = PageRequest.of(page, pageSize);
		return new PageImpl<>(content, pageable, total);
	}

	public List<UserProblemSolving> getSolvedProblemByUserId(Integer userId) {
		return this.userProblemSolvingRepository.getAllSolvedProblemsByUserId(userId);
	}

	// 🌕 유저 별 푼 문제 수 보기
	public Integer getCountOfSolvingProblemByUserId(Integer userId) {
		return this.userProblemSolvingRepository.getCountOfSolvingProblemByUserId(userId);
	}

	// 🌕 푼 문제 추가하기
	public void addSolvedProblem(Integer userId, Integer problemId) {
		UserProblemSolving ups = new UserProblemSolving();

		Auth user = this.authRepository.getById(userId);
		Problem problem = this.problemRepository.getById(problemId);

		ups.setUser(user);
		ups.setProblem(problem);
		ups.setState(2);

		try {
			this.userProblemSolvingRepository.save(ups);
			System.out.println("🌕 해결한 문제 추가 성공");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("🌑 해결한 문제 추가 성공");
		}
	}

	// 🌕 유저 별 이미 푼 문제인 지 확인
	public boolean isExistingSolvedProblemByUserId(Integer userId, Integer problemId) {
		if (this.userProblemSolvingRepository.isExistingSolvedProblemByUserId(userId, problemId).isPresent()) {
			return true;
		}

		return false;
	}
}
