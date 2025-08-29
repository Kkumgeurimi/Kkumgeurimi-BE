# 수동배포 가이드

## 로컬 실행
```bash
docker-compose -f docker-compose.dev.yml up -d
./gradlew bootRun --args='--spring.profiles.active=dev'
```
- API 문서: http://localhost:8080/swagger-ui/index.html

## 운영 배포
```bash
# 빌드 및 배포
./gradlew build -x test
scp -i "./keys/kkumgeurimi-keypair.pem" -o StrictHostKeyChecking=no build/libs/kopring-0.0.1-SNAPSHOT.jar ubuntu@52.79.150.161:~/kopring/build/libs/
scp -i "./keys/kkumgeurimi-keypair.pem" -o StrictHostKeyChecking=no docker-compose.prod.yml ubuntu@52.79.150.161:~/kopring/
ssh -i "./keys/kkumgeurimi-keypair.pem" -o StrictHostKeyChecking=no ubuntu@52.79.150.161 "cd ~/kopring && docker-compose -f docker-compose.prod.yml restart app"
```

- 운영 사이트: https://api.kkumgeurimi.r-e.kr
- API 문서: https://api.kkumgeurimi.r-e.kr/swagger-ui/index.html

## 기술 스택
- Kotlin, Spring Boot 3.5.5, JPA, Security, JWT
- PostgreSQL, Redis, Docker, Nginx