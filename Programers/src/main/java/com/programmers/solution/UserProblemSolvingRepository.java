package com.programmers.solution;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProblemSolvingRepository extends JpaRepository<UserProblemSolving, Integer> {

	// 해결한 문제 수 구하기
	@Query("""
			SELECT count(ups)
			FROM UserProblemSolving ups
			WHERE ups.state = 2
			AND ups.user.userId = :userId
			""")
	Integer getCountOfSolvingProblemByUserId(@Param("userId") Integer userId);

	// 페이징된 해결한 문제 구하기
	@Query("""
			SELECT ups
			FROM UserProblemSolving ups
			WHERE ups.state = 2
			AND ups.user.userId = :userId
			""")
	Page<UserProblemSolving> getPagedAllSolvedProblemsByUserId(@Param("userId") Integer userId, Pageable pageable);

	// 해결한 문제 구하기
	@Query("""
			SELECT ups
			FROM UserProblemSolving ups
			WHERE ups.state = 2
			AND ups.user.userId = :userId
			""")
	List<UserProblemSolving> getAllSolvedProblemsByUserId(@Param("userId") Integer userId);

	// 해결한 문제인지 확인
	@Query("""
			SELECT ups
			FROM UserProblemSolving ups
			WHERE ups.state = 2
			AND ups.user.userId = :userId
			AND ups.problem.problemId = :problemId
			""")
	Optional<UserProblemSolving> isExistingSolvedProblemByUserId(@Param("userId") Integer userId,
			@Param("problemId") Integer problemId);

}
