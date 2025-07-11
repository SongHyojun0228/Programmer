package com.programmers.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthRepository extends JpaRepository<Auth, Integer> {

	// 🌕 유저 아이디(이메일)로 찾기
	@Query("""
			    SELECT a FROM Auth a
			    WHERE a.id = :id
			""")
	Optional<Auth> findById(@Param("id") String id);

	// 🌕 해당 유저 점수보다 높은 유저 수
	@Query("""
			    SELECT COUNT(a) FROM Auth a
			    WHERE a.score > :score
			""")
	int countUsersWithHigherScore(@Param("score") Integer score);
}
