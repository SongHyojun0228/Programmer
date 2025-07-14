# **💻 Programmer**
> **👨🏻‍💻 사용자가 문제를 선택하여 Java 언어로 직접 코드를 작성, 제출 할 수 있는 플랫폼**

## 배포 사이트 경로
> [**http://15.165.162.229:2300/**](http://15.165.162.229) 

## API 명세서
> [**https://spotless-seeker-00f.notion.site/Programmers-API-22d13ddc688b8058b0d6c8d3a4694bf7?pvs=73**](https://www.notion.so/Programmers-API-22d13ddc688b8058b0d6c8d3a4694bf7?pvs=21)

<br><br>

## 🗳️ 선정 이유
- Java에 친숙해지고 공부하기 위해 많이 풀었던 **프로그래머스의 컴파일러**에 대한 궁금증이 시작이었습니다.
- 코드를 입력하고 제출했을 때 어떤 식으로 제가 쓴 코드가 실행되고 여러 테스트 채점이 되는 지 문제 풀이 플랫폼을 구현하여 이해하고자 했습니다.

<br><br>

## **⩤ 주요 기능**
### **1. 🔎 문제 풀이 목록 및 검색 기능**
- **`AJAX 기반 실시간 검색`**
    - 사용자가 입력한 문제 제목에 따라 페이지 전체 새로고침 없이 실시간 검색 결과 반환
- **`페이지네이션 처리`**
- **유저 맞춤형 정보 표시**:
    - 각 문제에 대해 해당 사용자의 풀이 상태 표시
    - 난이도 및 해결한 유저 수 함께 제공
- **검색 결과도 페이징 적용**

### **2. 📑 문제 풀이 페이지**
- **문제 설명, 입출력 예시, 제한사항, 함수 시그니처를 `JSON` 기반으로 파싱 후 시각화**
- **`사용자가 입력한 코드를 서버에 저장하고, Java 컴파일 및 실행을 통해 실시간 채점 수행`**
- **각 테스트케이스 실행 시간(ms 단위)을 측정 및 출력**
- **`기대값과 실제 출력값을 비교`하여 통과/실패 여부를 시각적으로 반환**
- **문제 해결 시 유저 점수 증가, 해결한 문제 리스트 및 순위에 반영**

### **3. 🙆🏻 회원 기능**
- 유저 별 푼 문제 확인
- 유저 순위, 점수, 컴파일 횟수 표시

### **4. 🌐 배포**
- **`AWS Lightsail (Ubuntu)`** 위에 배포, **Oracle XE** 기반의 문제 및 유저 데이터 저장
- **URL: http://15.165.162.229:2300/**

<br><br>

## **⚒️ 사용 언어 및 툴**
- **Frontend : HTML, CSS, JavaScript**
- **Template : Thymeleaf**
- **Backend : Spring Security, Spring Boot, Spring MVC, JPA**
- **Database : Oracle**
- **Build Tool : Gradle**
- **Infra: AWS Lightsail (Ubuntu), Oracle XE, 포트 설정 및 외부 접속 구성**

<br><br>

## **📈 성장점**
- **`AWS Lightsail에 Ubuntu 환경`을 구성**하고, Oracle 데이터베이스 설치 및 Spring Boot 애플리케이션을 직접 배포함으로써 클라우드 기반 서비스 운영 경험을 쌓을 수 있었습니다.
- **`AJAX`를 이용한 검색 기능과 페이징 처리**, Thymeleaf를 활용한 서버 사이드 렌더링을 경험하며 프론트엔드와 백엔드 간 통신 구조를 자연스럽게 익혔습니다.
- **사용자가 작성한 코드를 파일로 저장하고, Java 컴파일 및 실행하는 흐름을 직접 구현함으로써 서버에서의 동적 코드 실행, 에러 처리 등의 기능을 설계하고 경험할 수 있었습니다.**
- 프로젝트를 진행하며 **Java와 Spring Boot**에 대한 이해도가 높아졌습니다.

<br><br>

## 📆 기간
- 2025년 07월 01일 ~ 2025년 07월 10일

<br><br>

## 사진
| 로그인 | 마이페이지 |
|:-:|:-:|
| <img width="1708" alt="로그인" src="https://github.com/user-attachments/assets/692ba8de-bd0f-438f-a49a-14e11acb464b" /> | <img width="1708" alt="마이페이지" src="https://github.com/user-attachments/assets/83e8e3f3-640e-4b45-82c4-4965b3abcf6c" /> |

---

| 메인화면 | 메인화면 검색 |
|:-:|:-:|
| <img width="1708" alt="메인" src="https://github.com/user-attachments/assets/e4343f61-8168-4943-9b04-148a4d09ba10" /> | <img width="1708" alt="메인 검색" src="https://github.com/user-attachments/assets/d3205563-53fd-4314-aa3a-fe401dd6271b" /> |

---

| 문제 풀이 성공 | 문제 풀이 실페 |
|:-:|:-:|
| <img width="1708" alt="성공" src="https://github.com/user-attachments/assets/08ecc8ff-8036-4f5d-81ab-3cb4a84a3d5d" /> | <img width="1708" alt="실패" src="https://github.com/user-attachments/assets/c7e203c7-dfa3-4458-a397-b8a40a8f009d" /> |

