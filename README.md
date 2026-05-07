# creator-settlement-api
라이브클래스 크리에이터 정산 API 과제

## 실행 환경

- Java 17
- Spring Boot 3.5.14
- PostgreSQL 16
- MyBatis

---

## PostgreSQL Docker 실행

```bash
docker run --name creator-settlement-postgres \
  -e POSTGRES_DB=settlement_db \
  -e POSTGRES_USER=test \
  -e POSTGRES_PASSWORD=1 \
  -p 5433:5432 \
  -d postgres:16