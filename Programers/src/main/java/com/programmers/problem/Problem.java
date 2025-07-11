package com.programmers.problem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_problem")
	@SequenceGenerator(name = "seq_problem", sequenceName = "SEQ_PROBLEM", allocationSize = 1)
	private Integer problemId;

	@Column(unique = true)
	private String title;

	@Column
	private String description;

	@Column
	private String limit;

//	@Column
//	private Integer timeLimit;
//
//	@Column
//	private Integer memoryLimit;

	@Column
	private String inputExample;

	@Column
	private String outputExample;

	@Column
	private String ioExampleDescription;

	@Column
	private String caution;

	@Column
	private Integer difficulty;

	@Column
	private Integer score;

	@Column
	String parameterType;
	
	@Column
	String parameterName;
	
	@Column
	String returnType;
	
	@Column
	Integer countOfUsersWhoHaveSolved;
}
