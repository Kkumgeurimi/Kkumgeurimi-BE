pipeline {
    agent any
    
    environment {
        // Jenkins에서 설정할 환경 변수들
        EC2_HOST = credentials('ec2-host')
        EC2_KEY = credentials('ec2-ssh-key')
        EC2_USER = 'ubuntu'
        
        // 데이터베이스 설정
        DB_URL = credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        
        // JWT 및 Redis 설정
        JWT_SECRET = credentials('jwt-secret')
        REDIS_PASSWORD = credentials('redis-password')
        
        // 도메인 설정
        DOMAIN_NAME = 'api.kkumgeurimi.r-e.kr'
        
        // 프로젝트 설정
        JAR_FILE = 'kopring-0.0.1-SNAPSHOT.jar'
        REMOTE_DIR = '~/kopring'
    }
    
    tools {
        // Gradle과 JDK 설정 (Jenkins에서 미리 설정되어 있어야 함)
        gradle 'Gradle-8.0'
        jdk 'JDK-17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '소스 코드 체크아웃'
                checkout scm
            }
        }
        
        stage('Environment Check') {
            steps {
                echo 'Java 및 Gradle 버전 확인'
                sh 'java -version'
                sh './gradlew --version'
                
                echo '환경 변수 확인'
                sh 'echo "EC2_HOST: ${EC2_HOST}"'
                sh 'echo "DOMAIN_NAME: ${DOMAIN_NAME}"'
            }
        }
        
        stage('Test') {
            steps {
                echo '테스트 실행'
                sh './gradlew test'
            }
            post {
                always {
                    // 테스트 결과 발행
                    publishTestResults testResultsPattern: 'build/test-results/test/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'build/reports/tests/test',
                        reportFiles: 'index.html',
                        reportName: 'Test Report'
                    ])
                }
            }
        }
        
        stage('Build') {
            steps {
                echo '애플리케이션 빌드'
                sh './gradlew clean build -x test'
            }
            post {
                success {
                    echo '빌드 성공'
                    archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                }
            }
        }
        
        stage('SSH Connection Test') {
            steps {
                echo 'EC2 연결 테스트'
                script {
                    try {
                        sh '''
                            ssh -i "${EC2_KEY}" -o ConnectTimeout=10 -o StrictHostKeyChecking=no \
                            "${EC2_USER}@${EC2_HOST}" "echo 'SSH 연결 성공'"
                        '''
                        echo 'EC2 연결 성공'
                    } catch (Exception e) {
                        error("EC2 연결 실패: ${e.message}")
                    }
                }
            }
        }
        
        stage('Deploy') {
            when {
                anyOf {
                    branch 'main'
                    branch 'master'
                }
            }
            steps {
                echo '배포 시작'
                
                script {
                    // 배포 디렉토리 생성
                    sh '''
                        ssh -i "${EC2_KEY}" -o StrictHostKeyChecking=no "${EC2_USER}@${EC2_HOST}" \
                        "mkdir -p ${REMOTE_DIR}/build/libs ${REMOTE_DIR}/nginx"
                    '''
                    
                    // JAR 파일 복사
                    sh '''
                        scp -i "${EC2_KEY}" -o StrictHostKeyChecking=no \
                        build/libs/${JAR_FILE} \
                        "${EC2_USER}@${EC2_HOST}:${REMOTE_DIR}/build/libs/"
                    '''
                    
                    // Docker Compose 파일 복사
                    sh '''
                        scp -i "${EC2_KEY}" -o StrictHostKeyChecking=no \
                        docker-compose.prod.yml \
                        "${EC2_USER}@${EC2_HOST}:${REMOTE_DIR}/"
                    '''
                    
                    // Nginx 설정 파일 복사
                    sh '''
                        scp -i "${EC2_KEY}" -o StrictHostKeyChecking=no \
                        nginx/nginx.prod.conf \
                        "${EC2_USER}@${EC2_HOST}:${REMOTE_DIR}/nginx/"
                    '''
                    
                    // .env 파일 생성 (환경 변수들을 서버에 전달)
                    sh '''
                        ssh -i "${EC2_KEY}" -o StrictHostKeyChecking=no "${EC2_USER}@${EC2_HOST}" \
                        "cd ${REMOTE_DIR} && cat > .env << EOF
DB_URL=${DB_URL}
DB_USERNAME=${DB_USERNAME}
DB_PASSWORD=${DB_PASSWORD}
JWT_SECRET=${JWT_SECRET}
REDIS_PASSWORD=${REDIS_PASSWORD}
DOMAIN_NAME=${DOMAIN_NAME}
EOF"
                    '''
                    
                    // Docker 컨테이너 재시작
                    sh '''
                        ssh -i "${EC2_KEY}" -o StrictHostKeyChecking=no "${EC2_USER}@${EC2_HOST}" \
                        "cd ${REMOTE_DIR} && docker-compose -f docker-compose.prod.yml down && \
                         docker-compose -f docker-compose.prod.yml up -d"
                    '''
                }
                
                echo '배포 완료'
            }
        }
        
        stage('Health Check') {
            steps {
                echo '애플리케이션 상태 확인'
                script {
                    // 애플리케이션이 시작될 때까지 대기
                    sleep(time: 30, unit: 'SECONDS')
                    
                    try {
                        sh '''
                            ssh -i "${EC2_KEY}" -o StrictHostKeyChecking=no "${EC2_USER}@${EC2_HOST}" \
                            "curl -f https://${DOMAIN_NAME}/actuator/health || curl -f http://localhost:8080/actuator/health"
                        '''
                        echo '애플리케이션 정상 작동 확인'
                    } catch (Exception e) {
                        echo "Health check 실패: ${e.message}"
                        echo "애플리케이션 로그 확인 중..."
                        sh '''
                            ssh -i "${EC2_KEY}" -o StrictHostKeyChecking=no "${EC2_USER}@${EC2_HOST}" \
                            "cd ${REMOTE_DIR} && docker-compose -f docker-compose.prod.yml logs --tail=50 app"
                        '''
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo '파이프라인 완료'
            cleanWs()
        }
        success {
            echo '배포 성공!'
            // 슬랙 알림 등을 여기에 추가할 수 있음
        }
        failure {
            echo '배포 실패!'
            // 실패 시 알림 등을 여기에 추가할 수 있음
        }
    }
}
