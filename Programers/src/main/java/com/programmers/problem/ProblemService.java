package com.programmers.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

	private final ProblemRepository problemRepository;

	List<Problem> getAllProblems() {
		return this.problemRepository.findAllByOrderByDifficultyAscProblemIdAsc();
	}

	public Page<Problem> getAllPagingProblems(int page) {
		int pageSize = 10;
		int startRow = page * pageSize;
		int endRow = startRow + pageSize;

		List<Problem> problems = this.problemRepository.findProblemsByPage(startRow, endRow);
		int total = this.problemRepository.countAllProblems();

		return new PageImpl<>(problems, PageRequest.of(page, pageSize), total);
	}

	Problem getProblem(Integer problemId) {
		return this.problemRepository.getById(problemId);
	}

	@Transactional
	public void addCountOfUsersWhoHaveSolved(Integer problemId) {
		Problem problem = this.getProblem(problemId);

		problem.setCountOfUsersWhoHaveSolved(problem.getCountOfUsersWhoHaveSolved() + 1);
	}

	public Page<Problem> getProblemsBySearchTitle(String keyword, int page) {
		int size = 10;
		int startRow = page * size;
		int endRow = (page + 1) * size;

		List<Problem> content = problemRepository.searchProblemsByTitleWithPagination(keyword, startRow, endRow);
		int total = problemRepository.countProblemsByKeyword(keyword);

		return new PageImpl<>(content, PageRequest.of(page, size), total);
	}

}
