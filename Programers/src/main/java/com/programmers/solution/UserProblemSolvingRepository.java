package com.programmers.solution;

import java.util.List;
import java.util.Optional;

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
	@Query(value = """
			SELECT * FROM (
				SELECT inner_query.*, ROWNUM rnum
				FROM (
					SELECT ups.*
					FROM user_problem_solving ups
					JOIN problem p ON ups.problem_id = p.problem_id
					WHERE ups.state = 2 AND ups.user_id = :userId
					ORDER BY p.difficulty ASC, p.problem_id ASC
				) inner_query
				WHERE ROWNUM <= :endRow
			)
			WHERE rnum > :startRow
			""", nativeQuery = true)
	List<UserProblemSolving> getSolvedProblemsByUserIdAndPage(@Param("userId") Integer userId,
			@Param("startRow") int startRow, @Param("endRow") int endRow);

	@Query(value = """
			SELECT COUNT(*) FROM user_problem_solving
			WHERE state = 2 AND user_id = :userId
			""", nativeQuery = true)
	int countSolvedProblemsByUserId(@Param("userId") Integer userId);

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
