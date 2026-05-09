# 라이브 클래스 정산 과제

---

# 1. 프로젝트 개요

라이브 클래스 플랫폼의 판매 및 취소 데이터를 기반으로 크리에이터 정산 기능과 운영자 정산 기능을 구현하였습니다.

과제 요구사항의 핵심인 다음 항목에 집중하여 구현하였습니다.

- 판매 및 취소 데이터 처리
- 부분 환불 처리
- 월 경계 취소 처리
- 정산 계산 로직
- 운영자 집계 기능
- API 예외 처리
- 테스트 코드 작성

또한 단순 기능 구현이 아닌, 테스트 코드 기반 검증과 예외 상황 처리까지 고려하여 구현하였습니다.

---

# 2. 기술 스택

- Java 17
- Spring Boot
- MyBatis
- PostgreSQL
- Gradle
- JUnit5
- MockMvc

---

# 3. 실행 방법

## DB Docker 셋팅 및 테이블, 데이터 설정
```
1. PostgreSQL Docker 실행
docker run --name creator-settlement-postgres \
  -e POSTGRES_DB=settlement_db \
  -e POSTGRES_USER=test \
  -e POSTGRES_PASSWORD=1 \
  -p 5433:5432 \
  -d postgres:16
  
2. sql 파일 실행
- src/main/resources/schema.sql 실행
- src/main/resources/data.sql 실행
```

## 애플리케이션 실행

```
프로젝트 root 경로 이동
./gradlew clean build  
java -jar build/libs/assignment-0.0.1-SNAPSHOT.jar
```

## 테스트 실행

```
./gradlew test
```

---

# 4. 요구사항 해석 및 가정

## 부분 환불 처리

부분 환불은 기존 결제 금액보다 작은 취소 금액이 들어오는 경우로 해석하였으며, 
정산 시 환불 금액만큼 차감되도록 구현하였습니다.

예시:
- 결제 금액: 80,000원
- 환불 금액: 30,000원
- 순 판매 금액: 50,000원

---

## 월 경계 취소 처리

판매 발생 월과 취소 발생 월이 다른 경우,
각 발생 월 기준으로 정산에 반영하도록 구현하였습니다.

예시:
- 1월 판매 → 1월 정산 반영
- 2월 취소 → 2월 정산 반영

---

## 빈 월 조회 처리

특정 월에 판매 데이터가 존재하지 않는 경우,
null 반환 대신 0원 응답 DTO를 반환하도록 처리하였습니다.

---

## 운영자 정산 기준

운영자 정산은 판매 원천 데이터를 기준으로
크리에이터별 정산 예정 금액을 집계하도록 구현하였습니다.

---

## 인증/인가 처리

과제 요구사항 기준에 맞춰 인증/인가는 최소화하였으며,
정산 및 판매 데이터 처리 로직 구현에 집중하였습니다.

---

# 5. 설계 결정과 이유

## 판매/취소 데이터를 별도 상태로 저장

판매(PAID)와 취소(CANCELED)를 각각 별도 데이터로 저장하였습니다.

이를 통해:
- 부분 환불 처리
- 월 경계 취소 처리
- 정산 이력 추적

이 가능하도록 설계하였습니다.

---

## Service 계층 중심 비즈니스 로직 처리

정산 계산 및 예외 검증 로직은 Service 계층에서 처리하도록 구성하였습니다.

Controller는 요청/응답 처리 역할만 담당하도록 분리하였습니다.

---

## 공통 API 예외 처리

GlobalExceptionHandler를 사용하여
공통 API 예외 응답 구조를 적용하였습니다.

예외 발생 시:
- HTTP 상태 코드
- 에러 코드
- 메시지

형태로 일관된 응답을 반환하도록 구현하였습니다.

---

## Java 재계산 기반 테스트 검증

정산 테스트에서는 단순 하드코딩 검증이 아닌,
판매 원천 데이터를 Java Stream으로 재계산한 결과와
SQL 집계 결과를 비교하도록 구현하였습니다.

이를 통해:
- 정산 계산 검증
- 운영자 집계 검증
- 부분 환불 검증

신뢰성을 높이고자 하였습니다.

---

# 6. 미구현 / 제약사항

- 인증/인가 기능 미구현
- 동시성 처리 미고려
- 대용량 데이터 최적화 미적용
- 플랫폼 수수료율은 고정 비율 기준으로 처리
- 페이징 기능 미구현
- 실시간 정산 기능 미구현

---

# 7. AI 활용 범위

과제 구현 과정에서 ChatGPT를 활용하여 다음 내용을 보조받았습니다.

- Java Stream 기반 집계 로직 검증 아이디어
- README 문서 정리
- 테스트 코드 구조 작성
- 예외 처리 구조 설계
- 코드 리뷰 및 리팩토링 방향 검토

최종 구현 및 구조 결정은 직접 검토 후 반영하였습니다.

---

# 8. API 목록 및 예시

## 판매 등록 예시

### Request

```http
POST /api/sales/pay
```

```json
{
  "saleNum": "sale-999",
  "courseId": "course-1",
  "studentId": "student-999",
  "amount": 50000,
  "occurredAt": "2025-03-01T10:00:00"
}
```

---

## 판매 취소 예시

### Request

```http
POST /api/sales/cancel
```

```json
{
  "saleNum": "sale-1",
  "amount": 10000,
  "occurredAt": "2025-03-05T10:00:00"
}
```

---

## API 예외 응답 예시

### Response

```json
{
  "code": "BAD_REQUEST",
  "message": "이미 취소된 판매번호입니다."
}
```

---

## 판매 내역 조회

### Request

```http
GET /api/sales?creatorId=creator-1&fromDate=2025-03-01&toDate=2025-03-31
```

---

## 크리에이터 월별 정산 조회

### Request

```http
GET /api/settlements/creators/creator-1?yearMonth=2025-03
```

---

## 운영자 정산 조회

### Request

```http
GET /api/settlements/admin?fromDate=2025-03-01&toDate=2025-03-31
```

---

# 9. 데이터 모델 설명

## creator

크리에이터 정보 관리

| 컬럼 | 설명 |
|---|---|
| creator_id | 크리에이터 ID |
| creator_nm | 크리에이터명 |
| platform_commission | 플랫폼 수수료율 |

---

## course

강의 정보 관리

| 컬럼 | 설명 |
|---|---|
| course_id | 강의 ID |
| creator_id | 크리에이터 ID |

---

## sale_record

판매 및 취소 이력 관리

| 컬럼 | 설명 |
|---|---|
| sale_num | 판매번호 |
| course_id | 강의 ID |
| student_id | 수강생 ID |
| amount | 금액 |
| sale_status | PAID / CANCELED |
| occurred_at | 발생일시 |

---

# 10. 테스트 실행 방법

## 전체 테스트 실행

```bash
./gradlew test
```

---

## 주요 테스트 항목

### SaleServiceTest

- 판매 등록 테스트
- 취소 등록 테스트
- 판매 내역 조회 테스트

---

### SettlementServiceTest

- 정산 계산 검증
- 부분 환불 처리 검증
- 월 경계 취소 처리 검증
- 빈 월 조회 검증
- 운영자 정산 집계 검증

---

### SaleControllerTest

- 존재하지 않는 강의 결제 예외 테스트
- 중복 결제 승인 예외 테스트
- 존재하지 않는 판매번호 취소 예외 테스트
- 중복 취소 예외 테스트
- 환불 금액 초과 예외 테스트