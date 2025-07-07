package com.programmers.problem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.auth.Auth;
import com.programmers.auth.AuthController;
import com.programmers.auth.AuthService;
import com.programmers.solution.UserProblemSolving;
import com.programmers.solution.UserProblemSolvingService;
import com.programmers.test.TestCase;
import com.programmers.test.TestCaseService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProblemController {

    private final AuthController authController;

	private final AuthService authService;
	private final ProblemService problemService;
	private final TestCaseService testCaseService;
	private final UserProblemSolvingService userProblemSolvingService;

	@GetMapping("/")
	public String getMain(Principal principal, Model model,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		Auth user = this.authService.isExistingUser(principal.getName());
		Page<Problem> paging = this.problemService.getAllPagingProblems(page);
		List<Problem> allProblems = this.problemService.getAllProblems();
		List<UserProblemSolving> solvedProblems = this.userProblemSolvingService
				.getSolvedProblemByUserId(user.getUserId());

		// 풀었는 지? 아닌 지? 구별
		Map<Integer, String> problemIdToStatus = new HashMap<>();
		for (UserProblemSolving ups : solvedProblems) {
			int pid = ups.getProblem().getProblemId();
			if (ups.getState() == 2) {
				problemIdToStatus.put(pid, "성공");
			} else if (ups.getState() == 1) {
				problemIdToStatus.put(pid, "도전 중");
			} else {
				problemIdToStatus.put(pid, "미도전");
			}
		}

		int userRank = this.authService.getUserRank(user.getUserId());

		model.addAttribute("user", user);
		model.addAttribute("allProblems", allProblems);
		model.addAttribute("paging", paging);
		model.addAttribute("userRank", userRank);
		model.addAttribute("solvedProblems", solvedProblems);
		model.addAttribute("problemStatuses", problemIdToStatus);

		return "main/index";
	}

	@GetMapping("/problem/{problemId}")
	public String getProblem(@PathVariable("problemId") Integer problemId, Principal principal, Model model) {
		Auth user = this.authService.isExistingUser(principal.getName());
		Problem problem = this.problemService.getProblem(problemId);

		ObjectMapper mapper = new ObjectMapper();

		try {
			// 입출력 예시
			List<Map<String, Object>> inputExamples = mapper.readValue(problem.getInputExample(),
					new TypeReference<>() {
					});
			List<Map<String, Object>> outputExamples = mapper.readValue(problem.getOutputExample(),
					new TypeReference<>() {
					});

			List<String> inputExamplesStr = inputExamples
					.stream().map(example -> example.entrySet().stream()
							.map(entry -> entry.getKey() + " = " + entry.getValue()).collect(Collectors.joining(", ")))
					.collect(Collectors.toList());

			List<String> outputExamplesStr = outputExamples
					.stream().map(example -> example.entrySet().stream()
							.map(entry -> entry.getKey() + " = " + entry.getValue()).collect(Collectors.joining(", ")))
					.collect(Collectors.toList());

			List<Map<String, String>> examplePairs = new ArrayList<>();
			for (int i = 0; i < inputExamplesStr.size(); i++) {
				Map<String, String> pair = new HashMap<>();
				pair.put("input", inputExamplesStr.get(i));
				pair.put("output", outputExamplesStr.get(i));
				examplePairs.add(pair);
			}

			// 파라미터 타입 & 명
			List<String> paramTypes = mapper.readValue(problem.getParameterType(), new TypeReference<>() {
			});
			List<String> paramNames = mapper.readValue(problem.getParameterName(), new TypeReference<>() {
			});

			model.addAttribute("returnType", problem.getReturnType());
			model.addAttribute("paramTypes", paramTypes);
			model.addAttribute("paramNames", paramNames);
			model.addAttribute("examplePairs", examplePairs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("user", user);
		model.addAttribute("problem", problem);

		return "main/problem";
	}

	@PostMapping("/submit/code")
	@ResponseBody
	public String saveJavaCode(@RequestParam("code") String code, @RequestParam("problemId") Integer problemId,
			@RequestParam(name = "className", required = false, defaultValue = "Solution") String className,
			Principal principal) throws IOException, InterruptedException {

		Auth user = this.authService.isExistingUser(principal.getName());

		ObjectMapper mapper = new ObjectMapper();

		// 🌕 Judge 저장 디렉토리
		Path dirPath = Paths.get("/tmp/judge");
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}

		// 🌕 사용자가 작성한 코드 저장
		Path userCodePath = dirPath.resolve(className + ".java");
		Files.write(userCodePath, code.getBytes(StandardCharsets.UTF_8));

		// 🌕 문제 입출력 예시 검사
		Problem problem = problemService.getProblem(problemId);
		List<Map<String, Object>> problemInputExamples = mapper.readValue(problem.getInputExample(),
				new TypeReference<>() {
				});
		List<Map<String, Object>> problemOutputExamples = mapper.readValue(problem.getOutputExample(),
				new TypeReference<>() {
				});

		// 🌕 테스트케이스 검사 ( 사용자는 어떤 매개변수 입력 테스트가 있는 지 모름 )
		TestCase testCase = testCaseService.getByProblemId(problemId);

		List<Map<String, Object>> testCaseInputs = new ArrayList<>();
		List<Map<String, Object>> testCaseOutputs = new ArrayList<>();

		testCaseInputs = mapper.readValue(testCase.getInputs(), new TypeReference<>() {
		});
		testCaseOutputs = mapper.readValue(testCase.getOutputs(), new TypeReference<>() {
		});

		// 🌕 문제 입출력 + 테스트케이스 합치기 = 모든 입출력 예시
		List<Map<String, Object>> allInputs = new ArrayList<>();
		List<Map<String, Object>> allOutputs = new ArrayList<>();

		allInputs.addAll(problemInputExamples);
		allInputs.addAll(testCaseInputs);

		allOutputs.addAll(problemOutputExamples);
		allOutputs.addAll(testCaseOutputs);

		int countOfParameter = allOutputs.get(0).size();
		System.out.println("매개변수 수 : " + countOfParameter);

//		public class Solution {
//		      public static String solution(String str1, String str2) {
//		            return str1 + str2;
//		      }
//		}

		// 🌕 Judge.java 생성 및 작성
		StringBuilder judgeCode = new StringBuilder();
		judgeCode.append("public class Judge {\n").append("  public static void main(String[] args) {\n");

		for (Map<String, Object> input : allInputs) {
			judgeCode.append("    {\n").append("      long start = System.nanoTime();\n");

			List<String> args = new ArrayList<>();

			for (Map.Entry<String, Object> entry : input.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (value instanceof List<?> listVal) {
					String arrayType = findOutArrayType(listVal);
					String values = listVal.stream()
							.map(v -> arrayType.equals("char") ? "'" + v + "'"
									: arrayType.equals("String") ? "\"" + v + "\"" : v.toString())
							.collect(Collectors.joining(", "));
					args.add("new " + arrayType + "[] {" + values + "}");
				} else if (value instanceof String strVal) {
					args.add("\"" + strVal + "\"");
				} else {
					args.add(String.valueOf(value));
				}
			}

			// 🌕 solution 호출
			judgeCode.append("      Object result = null;\n").append("      try {\n")
					.append("        result = Solution.solution(").append(String.join(", ", args)).append(");\n")
					.append("      } catch (Exception e) {\n")
					.append("        result = \"예외 발생: \" + e.getMessage();\n").append("      }\n")
					.append("      long end = System.nanoTime();\n")
					.append("      double timeMs = (end - start) / 1_000_000.0;\n")
					.append("      if (result instanceof int[]) result = java.util.Arrays.toString((int[]) result);\n")
					.append("      else if (result instanceof long[]) result = java.util.Arrays.toString((long[]) result);\n")
					.append("      else if (result instanceof double[]) result = java.util.Arrays.toString((double[]) result);\n")
					.append("      else if (result instanceof String[]) result = java.util.Arrays.toString((String[]) result);\n")
					.append("      System.out.println(result + \"::\" + String.format(\"%.4f\", timeMs) + \"ms\");\n");

			judgeCode.append("    }\n");
		}

		judgeCode.append("  }\n}\n");

		Path judgePath = dirPath.resolve("Judge.java");
		Files.write(judgePath, judgeCode.toString().getBytes(StandardCharsets.UTF_8));

		// 🌕 코드 컴파일
		ProcessBuilder pbCompile = new ProcessBuilder("javac", className + ".java", "Judge.java");
		pbCompile.directory(dirPath.toFile());
		pbCompile.redirectErrorStream(true);

		Process compile = pbCompile.start();
		String compileOutput = new String(compile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		int compileResult = compile.waitFor();

		if (compileResult != 0) {
			return "🌑 컴파일 오류 발생:\n" + compileOutput;
		}

		// 🌕 실행
		ProcessBuilder pbRun = new ProcessBuilder("java", "Judge");
		pbRun.directory(dirPath.toFile());
		pbRun.redirectErrorStream(true);

		Process run = pbRun.start();
		String runOutput = new String(run.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		int runResult = run.waitFor();

		// 🌕 결과 비교
		String[] actualOutputs = runOutput.trim().split("\\R"); // 줄 단위 split
		List<String> expectedOutputs = allOutputs.stream().map(m -> m.get("return").toString().trim())
				.collect(Collectors.toList());

		StringBuilder result = new StringBuilder();

		boolean isSolved = true;

		for (int i = 0; i < expectedOutputs.size(); i++) {
			String expected = expectedOutputs.get(i);
			String actualLine = (i < actualOutputs.length) ? actualOutputs[i].trim() : "(출력값 없음)";

			String[] parts = actualLine.split("::");
			String actualValue = parts[0];
			String elapsedTime = parts.length > 1 ? parts[1] : "N/A";

			if (expected.equals(actualValue)) {
				result.append("🌕 테스트 ").append(i + 1).append(" 통과").append(" ( 실행시간 : ").append(elapsedTime)
						.append(" )\n\n");
			} else {
				result.append("🌑 테스트 ").append(i + 1).append(" 실패").append(" ( 실행시간 : ").append(elapsedTime)
						.append(" )\n").append("   기대값: ").append(expected).append("\n").append("   실제값: ")
						.append(actualValue).append("\n\n");
				isSolved = false;
			}
		}

		if (isSolved && !this.userProblemSolvingService.isExistingSolvedProblemByUserId(user.getUserId(), problemId)) {
			this.userProblemSolvingService.addSolvedProblem(user.getUserId(), problemId);
			this.authService.addScore(user.getUserId(), problem.getScore());
			System.out.println("문제에 할당된 점수 : " + problem.getScore());
			this.problemService.addCountOfUsersWhoHaveSolved(problem.getProblemId());
		}

		this.authService.addCountOfComplie(user.getUserId());
		result.append("\n@SCORE=").append(user.getScore()).append("@");

		return result.toString();
	}

	@GetMapping("/find_problem")
	@ResponseBody
	public Map<String, Object> searchProblemsAjax(
	        @RequestParam("search_title") String searchTitle,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        Principal principal) {

		System.out.println("🌕 " + searchTitle + "로 문제 검색");
		
	    Auth user = this.authService.isExistingUser(principal.getName());
	    Page<Problem> paging = this.problemService.getProblemsBySearchTitle(searchTitle, page);
	    List<UserProblemSolving> solved = this.userProblemSolvingService.getSolvedProblemByUserId(user.getUserId());

	    Map<Integer, String> statusMap = new HashMap<>();
	    for (UserProblemSolving ups : solved) {
	        int pid = ups.getProblem().getProblemId();
	        if (ups.getState() == 2) statusMap.put(pid, "성공");
	        else if (ups.getState() == 1) statusMap.put(pid, "도전 중");
	        else statusMap.put(pid, "미도전");
	    }

	    Map<String, Object> result = new HashMap<>();
	    
	    result.put("problems", paging.getContent());
	    result.put("statuses", statusMap);
	    result.put("totalPages", paging.getTotalPages());
	    result.put("currentPage", paging.getNumber());
	    
	    System.out.println("problems : " + paging.getContent());
	    System.out.println("statuses : " + statusMap);
	    System.out.println("totalPages : " + paging.getTotalPages());
	    System.out.println("currentPage : " + paging.getNumber());
	    
	    return result;
	}

	// 배열인지 검사
	private String findOutArrayType(List<?> list) {
		if (list.isEmpty())
			return "Object";
		Object obj = list.get(0);

		if (obj instanceof Integer)
			return "int";
		if (obj instanceof String)
			return "String";

		return "Object";
	}

}
