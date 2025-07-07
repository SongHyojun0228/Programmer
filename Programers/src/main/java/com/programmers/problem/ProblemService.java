package com.programmers.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		Pageable pageable = PageRequest.of(page, 10,
				Sort.by("difficulty").ascending().and(Sort.by("problemId").ascending()));

		return this.problemRepository.findAll(pageable);
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
		Pageable pageable = PageRequest.of(page, 10);
		return problemRepository.findByTitleContainingIgnoreCaseOrderByDifficultyAscProblemIdAsc(keyword, pageable);
	}
}
