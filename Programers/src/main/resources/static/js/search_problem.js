function fetchProblems(page = 0) {
	const keyword = document.getElementById("search_title").value;

	fetch(`/find_problem?search_title=${encodeURIComponent(keyword)}&page=${page}`)
		.then(res => res.json())
		.then(data => {
			const container = document.getElementById("problems_container");
			container.innerHTML = "";

			// 문제 목록 상단 헤더(한 번만 넣기)
			const headerHtml = `
        <div class="problem_items" id="problem_items_title">
          <p>상태</p>
          <p class="problem_title">제목</p>
          <p>난이도</p>
          <p>완료한 사람</p>
        </div>
      `;
			container.insertAdjacentHTML('beforeend', headerHtml);

			data.problems.forEach(problem => {
				const status = data.statuses[problem.problemId] || "미도전";
				const statusClass = status === "성공" ? "status_success"
					: status === "도전 중" ? "status_progress"
						: "status_not_started";

				container.insertAdjacentHTML('beforeend', `
          <div class="problem_items">
              <p class="${statusClass}">${status}</p>
              <a class="problem_link" href="/problem/${problem.problemId}">
                  <p class="problem_title">${problem.title}</p>
              </a>
              <p>Lv.${problem.difficulty}</p>
              <p>${problem.countOfUsersWhoHaveSolved}명</p>
          </div>
        `);
			});

			console.log(data);
			// 문제 개수만 바꾸기
			document.getElementById("count_of_problems").innerText = `문제 ${data.problems.length}개`;
		});
}

document.addEventListener("DOMContentLoaded", () => {
	document.getElementById("search_btn").addEventListener("click", () => fetchProblems(0));
});
