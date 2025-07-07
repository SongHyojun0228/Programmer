package com.programmers.test;

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
public class TestCase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_test_case")
	@SequenceGenerator(name = "seq_test_case", sequenceName = "SEQ_TEST_CASE", allocationSize = 1)
	private Integer testCaseId;
	
	@Column
	Integer problemId;
	
	@Column 
	String inputs;
	
	@Column 
	String outputs;
	
}
