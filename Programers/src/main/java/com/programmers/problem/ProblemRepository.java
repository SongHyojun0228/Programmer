package com.programmers.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
	
	Page<Problem> findAll(Pageable pageable);

	List<Problem> findAllByOrderByDifficultyAscProblemIdAsc();

	Page<Problem> findByTitleContainingIgnoreCaseOrderByDifficultyAscProblemIdAsc(String keyword, Pageable pageable);

}
