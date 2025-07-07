package com.programmers.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {

    Optional<TestCase> findByProblemId(Integer problemId);
	
}
