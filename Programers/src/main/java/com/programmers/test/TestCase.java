package com.programmers.test;

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
public class TestCase {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_test_case")
	@SequenceGenerator(name = "seq_test_case", sequenceName = "SEQ_TEST_CASE", allocationSize = 1)
	private Integer testCaseId;
	
	@ManyToOne
	@JoinColumn(name = "problem_id")
	private Problem problem;
	
	@Column 
	String inputs;
	
	@Column 
	String outputs;
	
}
