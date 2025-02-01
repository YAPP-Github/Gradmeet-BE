# Gradmeet 🔬


## 📎 Architecture
![스크린샷 2025-01-30 오후 8.10.17.png](..%2F..%2F..%2FDesktop%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202025-01-30%20%EC%98%A4%ED%9B%84%208.10.17.png)
<br>

## ✨ Tech Stack

| IDE | IntelliJ                         |
|:---|:---------------------------------|
| Language | Kotlin                           |
| Framework | Spring Boot 3.4.1, Gradle        |
| Authentication | Spring Security, JSON Web Tokens |
| Orm | Spring Data JPA                  |
| Database | MariaDB                          |
| External | Nginx, Docker, Redis             |
| CI/CD | Github Action                    |
| API Docs | Swagger                          |

<br>


## 📦 Package Structure

### domain: 도메인 계층

```text
- 비즈니스 로직을 처리하는 핵심 도메인 객체들 (model)
- 도메인 객체의 영속성 및 외부 시스템과의 인터페이스 (gateway)
```

|             | presentation | infrastructure | application | support | domain |
|-------------|---|---------|------|---------|--------|
| 사용가능한 모듈 여부 | - | -       | -    | O       | -      |

### util: 유틸 패키지

```text
- 비즈니스 로직과 직접적인 관련이 없는 공통적인 기능을 제공하는 유틸리티 클래스
- TsidUtils 등
```

|             | presentation | infrastructure | application | support | domain |
|-------------|---|---------|------|---------|--------|
| 사용가능한 모듈 여부 | - | -       | -    | -       | -      |

### application: 응용 서비스/UseCase 계층

```text
- 비즈니스 로직을 수행하는 서비스 및 유즈케이스 구현
- 도메인 객체를 활용하여 애플리케이션의 주요 동작을 처리
```

|             | presentation | infrastructure | application | support | domain |
|-------------|---|---------|------|---------|--------|
| 사용가능한 모듈 여부 | - | -       | -    | O       | O      |


### infrastructure : 외부 통신 계층

```text
- DB, 외부 API 등 외부 시스템과의 연결을 담당하는 계층
- JPA Repository, FeignClient, S3, Redis 등의 **구현체**가 위치
- 도메인 계층과 직접 연결되지 않고 gateway(인터페이스)를 통해 접근
```

|             | presentation | infrastructure | application | support | domain |
|-------------|---|---------|------|---------|--------|
| 사용가능한 모듈 여부 | - | -       | -    | O       | O      |

### presentation : 요청 처리 계층

```text
- 클라이언트의 요청을 받아 응답을 반환하는 역할
- 비즈니스 로직을 직접 수행하지 않고 application 계층을 호출하여 처리
- Controller, Request/Response DTO, ExceptionHandler 등이 위치
```

|             | presentation | infrastructure | application | support | domain |
|-------------|---|---------|-------------|---------|--------|
| 사용가능한 모듈 여부 | - | -       | O           | O       | O      |

<br>

## 👩🏻‍💻 Contributors
<div>

|                  [@Ji-soo708](https://github.com/Ji-soo708)                  |                                       [@chock-cho](https://github.com/chock-cho)                                        |
|:----------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------:|
| <img width="300" src="https://avatars.githubusercontent.com/u/69844138?v=4"> | <img width="300" src="https://avatars.githubusercontent.com/u/113707388?v=4"> |

</div>
