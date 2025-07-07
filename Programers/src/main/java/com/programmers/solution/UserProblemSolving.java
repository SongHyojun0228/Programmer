package com.programmers.solution;

import com.programmers.auth.Auth;
import com.programmers.problem.Problem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserProblemSolving {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_problem_solving")
	@SequenceGenerator(name = "seq_user_problem_solving", sequenceName = "SEQ_USER_PROBLEM_SOLVING", allocationSize = 1)
	private Integer UserProblemSolvingId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Auth user;

	
	@ManyToOne
	@JoinColumn(name = "problem_id")
	private Problem problem;

	
	@Column
	Integer state;

}
