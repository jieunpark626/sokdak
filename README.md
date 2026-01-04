# Sokdak Core

인증 기능과 일기(Journal) 기능을 제공하는 Spring Boot 기반 서버입니다.

---

## 🚀 How to Run

### 1. IntelliJ에서 실행

1. PostgreSQL이 실행 중이어야 합니다.
2. 아래 환경 변수를 설정한 후 실행합니다.

```text
SPRING_PROFILES_ACTIVE
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
APP_PORT
```
3. SokdakCoreApplication을 실행합니다.

---

### 2. Docker로 실행
**prod**
```text
./gradlew clean build -x test
docker compose \        
  --env-file .env.prod \
  -f docker-compose.prod.yml \
  up --build -d          
```

**dev**
```text
./gradlew clean build -x test
docker compose \        
  --env-file .env.dev \
  -f docker-compose.dev.yml \
  up --build -d          
```

