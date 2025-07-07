document.addEventListener("DOMContentLoaded", function() {
	const editor = CodeMirror.fromTextArea(document.getElementById("code"), {
		lineNumbers: true,
		mode: "text/x-java",
		theme: "default",
		indentUnit: 6,
		smartIndent: true,
		autoCloseBrackets: true,
		extraKeys: {
			"Tab": cm => cm.execCommand("insertSoftTab")
		}
	});

	editor.setValue(starterCode);

	document.querySelector("button[type='reset']").addEventListener("click", function() {
		editor.setValue(starterCode);
		document.getElementById("submitResult").innerText = "실행결과가 여기에 표시됩니다.";
	});

	document.getElementById("submitBtn").addEventListener("click", function() {
		const code = editor.getValue();
		const problemId = document.querySelector("input[name='problemId']").value;

		const csrfName = document.querySelector("input[name='_csrf']").name;
		const csrfToken = document.querySelector("input[name='_csrf']").value;

		const formData = new FormData();
		formData.append("code", code);
		formData.append("problemId", problemId);
		formData.append(csrfName, csrfToken);

		fetch("/submit/code", {
			method: "POST",
			body: formData
		})
			.then(response => response.text())
			.then(result => {
				const scoreMatch = result.match(/@SCORE=(\d+)@/);
				const score = scoreMatch ? scoreMatch[1] : "0";

				const displayResult = result.replace(/@SCORE=\d+@/, "").trim();

				document.getElementById("submitResult").innerText = displayResult;

				console.log("displayResult : " + displayResult);
				if (displayResult.includes("실패") || displayResult.includes("오류")) {
					alert(`오답입니다.`);
				} else {
					alert(`정답입니다!\n현재 점수: ${score}점`);
				}
			})
			.catch(error => {
				alert("오류 발생: " + error);
			});

	});
});
