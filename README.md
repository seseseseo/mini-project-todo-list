# 📅 TODO - 일정 관리 어플리케이션
TODO 는 일정관리 어플리케이션으로 CRUD 기능을 통해 일정의 생성, 조회, 수정, 삭제를 수행할 수 있습니다.
`JDBC`와  `3 Layer Architecture`를 적용하여 구현하였습니다.

###  💡 프로젝트 목표 
1. 구현하고자 하는 서비스의 전반적인 흐름을 이해하고 기능을 설계하기
2. API 명세서 작성하기
3. CRUD 기능이 포함된, REST API 구현하기
4. `3 Layer Architecture`에 따라 각 Layer의 목적에 맞게 프로젝트 개발하기
5. JDBC를 사용해 DB 연동과 기본적인 SQL 쿼리 작성에 익숙해지기


## 📑 API 명세서

| 기능            | 메서드 | URL                  | 요청 데이터 (Request)                                                                                                                | 응답 데이터 (Response)                                                                                                                                                                             | 상태 코드                              |
|----------------|-------|------------------------|---------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------|
| **일정생성**   | POST  | ``        | ```json {"title": "회의 준비", "author": "홍길동", "password": "1234", "date": "2025-03-21T15:30:00"} ```                                 | ```json {"id": 1, "title": "회의 준비", "author": "홍길동", "createdDate": "2025-03-21T15:30:00", "modifiedDate": "2025-03-21T15:30:00"} ```                   | 201 정상등록 <br>400 등록실패            |
| **일정조회** | GET   | ``        | 쿼리 파라미터: `?author=홍길동&date=2025-03-21`                                                                                          | ```json [ {"id": 1, "title": "회의 준비", "author": "홍길동", "createdDate": "2025-03-21T15:30:00", "modifiedDate": "2025-03-21T15:30:00"} ] ```               | 200 정상조회  <br> 404 조회실패               |
| **상세조회** | GET   | ``   | 없음                                                                                                                                   | ```json {"id": 1, "title": "회의 준비", "author": "홍길동", "createdDate": "2025-03-21T15:30:00", "modifiedDate": "2025-03-21T15:30:00"} ```                   | 200 정상조회 <br> 404 조회실패                   |
| **일정수정**   | PUT   | ``    | ```json {"title": "회의 수정", "author": "홍길동", "password": "1234"} ```                                                                | ```json {"id": 1, "title": "회의 수정", "author": "홍길동", "createdDate": "2025-03-21T15:30:00", "modifiedDate": "2025-03-21T16:45:00"} ```                   | 200 정상수정  <br> 404 수정실패 |
| **일정삭제**   | DELETE| ``    | ```json {"password": "1234"} ```                                                                                                         | ```json {"message": "일정이 삭제되었습니다."} ```                                                                                                             | 200 정상삭제 <br> 404 삭제실패 |

## 구현할 클래스
```
📂 src/main/java/com/example/todo
├─ 📁 controller/          클라이언트 요청을 처리하고 응답을 반환
│    └─ TodoController.java       # 목록 조회 관련 컨트롤러
│  
├─ 📁 service/                       # 비즈니스 로직 처리
│    └─ 📝 TodoService.java
│    └─ 📝 TodoServiceImpl.java
├─ 📁 repository/                    # DB 접근 및 CRUD 작업 수행하는 계층
│    └─ 📝 TodoRepository.java
│    └─ 📝 TodoRepositoryImpl.java
├─ 📁 entity/                         # 데이터 관련 계층
│    └─ 📝 TodoEntity.java
├─ 📁 dto/                      # 계층 간 데이터 전송을 위한 DTO 클래스
│    └─ 📝 TodoRequestDto.java
│    └─ 📝 TodoResponseDto.java
├─ 📁 util/                          # 공통적으로 사용되는 유틸리티 클래스
│    ├─ 📝 
│    └─ 📝 
└─ 📁 application/         # Main Application
     └─ 📝 TodoApplication.java
```
