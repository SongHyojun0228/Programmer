package com.programmers.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

	@Query("""
			    SELECT t FROM TestCase t
			    WHERE t.problem.problemId = :problemId
			""")
	Optional<TestCase> findByProblemId(@Param("problemId") Integer problemId);

}
