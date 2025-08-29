#!/bin/bash

# 전체 배포 자동화 스크립트
# api.kkumgeurimi.r-e.kr 도메인으로 배포

set -e

echo "Kopring 애플리케이션 배포 시작"
echo "도메인: api.kkumgeurimi.r-e.kr"

# 환경 변수 확인
if [[ -z "$EC2_HOST" ]]; then
    echo "ERROR: EC2_HOST 환경 변수가 설정되지 않았습니다."
    echo "예: export EC2_HOST=your-ec2-ip"
    exit 1
fi

if [[ -z "$EC2_KEY" ]]; then
    echo "ERROR: EC2_KEY 환경 변수가 설정되지 않았습니다."
    echo "예: export EC2_KEY=path/to/your-key.pem"
    exit 1
fi

if [[ -z "$DB_URL" ]]; then
    echo "ERROR: DB_URL 환경 변수가 설정되지 않았습니다."
    echo "예: export DB_URL=jdbc:postgresql://your-rds-endpoint:5432/dbname"
    exit 1
fi

if [[ -z "$DB_USERNAME" ]]; then
    echo "ERROR: DB_USERNAME 환경 변수가 설정되지 않았습니다."
    exit 1
fi

if [[ -z "$DB_PASSWORD" ]]; then
    echo "ERROR: DB_PASSWORD 환경 변수가 설정되지 않았습니다."
    exit 1
fi

if [[ -z "$JWT_SECRET" ]]; then
    echo "ERROR: JWT_SECRET 환경 변수가 설정되지 않았습니다."
    exit 1
fi

if [[ -z "$REDIS_PASSWORD" ]]; then
    echo "ERROR: REDIS_PASSWORD 환경 변수가 설정되지 않았습니다."
    exit 1
fi

if [[ -z "$DOMAIN_NAME" ]]; then
    echo "ERROR: DOMAIN_NAME 환경 변수가 설정되지 않았습니다."
    echo "예: export DOMAIN_NAME=api.kkumgeurimi.r-e.kr"
    exit 1
fi

EC2_USER=${EC2_USER:-ubuntu}

echo "배포 정보: EC2=$EC2_HOST, USER=$EC2_USER"

# SSH 연결 테스트
echo "EC2 연결 테스트 중..."
if ! ssh -i "$EC2_KEY" -o ConnectTimeout=10 -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" "echo 'SSH 연결 성공'" 2>/dev/null; then
    echo "ERROR: EC2 인스턴스에 연결할 수 없습니다."
    echo "확인사항: EC2 실행상태, 보안그룹 SSH(22) 포트, SSH 키 경로, IP 주소"
    exit 1
fi

echo "EC2 연결 성공"

# 배포 파일들을 EC2로 복사
echo "배포 파일 복사 중..."
scp -i "$EC2_KEY" -o StrictHostKeyChecking=no -r \
    docker-compose.prod.yml \
    nginx/ \
    "$EC2_USER@$EC2_HOST":~/kopring/

echo "파일 복사 완료"

# EC2에서 초기 환경 설정
echo "EC2 환경 설정 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
    cd ~/kopring
    
    # Docker 환경 설정
    if ! command -v docker &> /dev/null; then
        echo "Docker 설치 중..."
        sudo apt update -y
        sudo apt install -y curl wget git
        
        # Docker 설치
        curl -fsSL https://get.docker.com -o get-docker.sh
        sudo sh get-docker.sh
        sudo usermod -aG docker $USER
        
        # Docker Compose 설치
        sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
        sudo chmod +x /usr/local/bin/docker-compose
        
        # Docker 서비스 시작
        sudo systemctl start docker
        sudo systemctl enable docker
        
        echo "Docker 설치 완료"
    else
        echo "Docker 이미 설치됨"
    fi
EOF

echo "환경 설정 완료"

# SSL 인증서 설정
echo "SSL 인증서 설정 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
    cd ~/kopring
    mkdir -p nginx/ssl
    
    # Let's Encrypt 인증서 발급 시도
    if command -v certbot &> /dev/null; then
        sudo certbot certonly --standalone -d api.kkumgeurimi.r-e.kr --non-interactive --agree-tos --email admin@kkumgeurimi.r-e.kr || {
            echo "Let's Encrypt 실패, 자체 서명 인증서 생성"
            openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
                -keyout nginx/ssl/privkey.pem \
                -out nginx/ssl/fullchain.pem \
                -subj "/C=KR/ST=Seoul/L=Seoul/O=Kopring/CN=api.kkumgeurimi.r-e.kr"
        }
    else
        sudo apt update
        sudo apt install -y certbot
        
        sudo certbot certonly --standalone -d api.kkumgeurimi.r-e.kr --non-interactive --agree-tos --email admin@kkumgeurimi.r-e.kr || {
            echo "Let's Encrypt 실패, 자체 서명 인증서 생성"
            openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
                -keyout nginx/ssl/privkey.pem \
                -out nginx/ssl/fullchain.pem \
                -subj "/C=KR/ST=Seoul/L=Seoul/O=Kopring/CN=api.kkumgeurimi.r-e.kr"
        }
    fi
    
    # 인증서 파일 복사
    if [ -f /etc/letsencrypt/live/api.kkumgeurimi.r-e.kr/fullchain.pem ]; then
        sudo cp /etc/letsencrypt/live/api.kkumgeurimi.r-e.kr/fullchain.pem nginx/ssl/
        sudo cp /etc/letsencrypt/live/api.kkumgeurimi.r-e.kr/privkey.pem nginx/ssl/
        sudo chown $USER:$USER nginx/ssl/*
    fi
EOF

echo "SSL 인증서 설정 완료"

# 환경 변수 설정
echo "환경 변수 설정 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
    cd ~/kopring
    
    # 환경 변수 파일 생성
    cat > .env << ENVEOF
# 프로덕션 환경 변수
DOCKER_REGISTRY=kopring
IMAGE_NAME=kopring-app
IMAGE_TAG=latest

# Spring Boot 환경변수 (application-prod.yml에서 사용)
SPRING_PROFILES_ACTIVE=prod
DB_URL=\${DB_URL}
DB_USERNAME=\${DB_USERNAME}
DB_PASSWORD=\${DB_PASSWORD}
JWT_SECRET=\${JWT_SECRET}
REDIS_PASSWORD=\${REDIS_PASSWORD}
DOMAIN_NAME=\${DOMAIN_NAME}
ENVEOF
EOF

echo "환경 변수 설정 완료"

# 애플리케이션 배포
echo "애플리케이션 배포 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
    cd ~/kopring
    
    # 기존 컨테이너 정리
    docker-compose -f docker-compose.prod.yml down || true
    docker image prune -f || true
    
    # 새로운 컨테이너 실행
    docker-compose -f docker-compose.prod.yml up -d
    
    # 컨테이너 상태 확인
    docker-compose -f docker-compose.prod.yml ps
EOF

echo "애플리케이션 배포 완료"

# 배포 확인
echo "배포 상태 확인 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
    cd ~/kopring
    
    echo "30초 대기 후 헬스 체크..."
    sleep 30
    
    # 헬스 체크
    if curl -f -s http://localhost:8080/actuator/health > /dev/null; then
        echo "애플리케이션 정상 실행 중"
    else
        echo "애플리케이션 시작 실패, 로그 확인:"
        docker-compose -f docker-compose.prod.yml logs app
    fi
EOF

echo "배포 완료!"
echo "애플리케이션 URL: https://api.kkumgeurimi.r-e.kr"
echo "헬스 체크: https://api.kkumgeurimi.r-e.kr/actuator/health"
echo "API 문서: https://api.kkumgeurimi.r-e.kr/swagger-ui/index.html"
echo ""
echo "SSH 접속: ssh -i $EC2_KEY $EC2_USER@$EC2_HOST"
