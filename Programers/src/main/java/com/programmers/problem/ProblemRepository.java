package com.programmers.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query("""
			    SELECT p FROM Problem p
			    ORDER BY p.difficulty ASC, p.problemId ASC
			""")
	List<Problem> findAllByOrderByDifficultyAscProblemIdAsc();

	@Query(value = """
			SELECT COUNT(*) FROM problem
			WHERE INSTR(LOWER(title), LOWER(:keyword)) > 0
			""", nativeQuery = true)
		int countProblemsByKeyword(@Param("keyword") String keyword);

		@Query(value = """
			SELECT * FROM (
				SELECT inner_query.*, ROWNUM rnum
				FROM (
					SELECT * FROM problem
					WHERE INSTR(LOWER(title), LOWER(:keyword)) > 0
					ORDER BY difficulty ASC, problem_id ASC
				) inner_query
				WHERE ROWNUM <= :endRow
			)
			WHERE rnum > :startRow
			""", nativeQuery = true)
		List<Problem> searchProblemsByTitleWithPagination(
			@Param("keyword") String keyword,
			@Param("startRow") int startRow,
			@Param("endRow") int endRow
		);

	@Query(value = """
			SELECT * FROM (
			    SELECT inner_query.*, ROWNUM rnum
			    FROM (
			        SELECT * FROM problem ORDER BY difficulty ASC, problem_id ASC
			    ) inner_query
			    WHERE ROWNUM <= :endRow
			)
			WHERE rnum > :startRow
			""", nativeQuery = true)
	List<Problem> findProblemsByPage(@Param("startRow") int startRow, @Param("endRow") int endRow);

	@Query(value = "SELECT COUNT(*) FROM problem", nativeQuery = true)
	int countAllProblems();

}
