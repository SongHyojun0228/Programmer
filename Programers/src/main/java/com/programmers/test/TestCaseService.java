package com.programmers.test;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestCaseService {

	private final TestCaseRepository testCaseRepository;

	public TestCase getByProblemId(Integer problemId) {
		if (testCaseRepository.findByProblemId(problemId).isPresent()) {
			return testCaseRepository.findByProblemId(problemId).get();
		}
		return null;
	}

	public String getInputsJsonByProblemId(Integer problemId) {
		return testCaseRepository.findByProblemId(problemId).map(TestCase::getInputs).orElse("[]");
	}

	public String getOutputsJsonByProblemId(Integer problemId) {
		return testCaseRepository.findByProblemId(problemId).map(TestCase::getOutputs).orElse("[]");
	}
}
