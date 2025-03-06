# Gradmeet 🔬


## 📎 Architecture
<img width="880" alt="architecture" src="https://github.com/user-attachments/assets/5ad0d8ac-39a3-4c02-bfd9-23ecdb5fdc69" />


<br><br>

## ✨ Tech Stack

| IDE | IntelliJ                         |
|:---|:---------------------------------|
| Language | Kotlin                           |
| Framework | Spring Boot 3.4.1, Gradle        |
| Authentication | Spring Security, JSON Web Tokens |
| ORM | Spring Data JPA                  |
| Database | MariaDB                          |
| External | Nginx, Docker, Redis, AWS SES             |
| CI/CD | Github Action                    |
| API Docs | Swagger                          |

<br>


## 📦 Module Structure

### domain: 도메인 모듈

```text
- 비즈니스 로직을 처리하는 핵심 도메인 객체들 (model)
- 도메인 객체의 영속성 및 외부 시스템과의 인터페이스 (gateway)
```

|             | infrastructure | presentation | application | domain |
|-------------|---|---------|------|--------|
| 사용 가능한 모듈 여부 | - | -       | -    | -      |


### application: 응용 서비스/UseCase 모듈

```text
- 비즈니스 로직을 수행하는 서비스 및 유즈케이스 구현
- 도메인 객체를 활용하여 애플리케이션의 주요 동작을 처리
```

|             | infrastructure | presentation | application | domain |
|-------------|---|---------|------|--------|
| 사용 가능한 모듈 여부 | - | -       | -    | O      |

### presentation : 요청 처리 모듈

```text
- 클라이언트의 요청을 받아 응답을 반환하는 역할
- 비즈니스 로직을 직접 수행하지 않고 application 계층을 호출하여 처리
- Controller, Request/Response DTO, ExceptionHandler 등이 위치
```

|             | infrastructure | presentation | application | domain |
|-------------|---|---------|-------------|--------|
| 사용 가능한 모듈 여부 | - | -       | O           | O      |

### infrastructure : 외부 통신 모듈

```text
- DB, 외부 API 등 외부 시스템과의 연결을 담당하는 계층
- JPA Repository, FeignClient, S3, Redis 등의 구현체가 위치
- 도메인 계층과 직접 연결되지 않고 gateway(인터페이스)를 통해 접근
```

|             | infrastructure | presentation | application | domain |
|-------------|---|---------|------|--------|
| 사용 가능한 모듈 여부 | - | O       | O    | O      |


<br>

## 👩🏻‍💻 Contributors
<div>

|                  [@Ji-soo708](https://github.com/Ji-soo708)                  |                                       [@chock-cho](https://github.com/chock-cho)                                        |
|:----------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------:|
| <img width="300" src="https://avatars.githubusercontent.com/u/69844138?v=4"> | <img width="300" src="https://avatars.githubusercontent.com/u/113707388?v=4"> |

</div>
