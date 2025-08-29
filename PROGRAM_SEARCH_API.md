# 프로그램 검색 API 문서

## 개요
프로그램 검색 API는 다양한 필터와 정렬 옵션을 사용하여 프로그램을 검색할 수 있는 API입니다.

## 엔드포인트
```
GET /programs/search
```

## 요청 파라미터

| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|---------|------|------|--------|------|
| interestCategory | String | 아니오 | null | 관심 카테고리 (직무/전공) |
| programType | String | 아니오 | "all" | 체험유형 |
| cost | String | 아니오 | "all" | 비용 |
| startDate | String | 아니오 | null | 시작 날짜 (YYYY-MM-DD) |
| endDate | String | 아니오 | null | 종료 날짜 (YYYY-MM-DD) |
| sortBy | String | 아니오 | "latest" | 정렬 기준 |
| page | Integer | 아니오 | 1 | 페이지 번호 |
| size | Integer | 아니오 | 10 | 페이지 크기 |

## 파라미터 상세 설명

### interestCategory (관심 카테고리)
- `null` 또는 파라미터 없음: 전체
- `0`: 인문·사회과학 연구직
- `1`: 자연·생명과학 연구직
- `2`: 정보통신 연구개발직 및 공학기술직
- `3`: 건설·채굴 연구개발직 및 공학기술직
- `4`: 제조 연구개발직 및 공학기술직
- `5`: 사회복지·종교직
- `6`: 교육직
- `7`: 법률직
- `8`: 경찰·소방·교도직
- `9`: 군인
- `10`: 보건·의료직
- `11`: 예술·디자인·방송직
- `12`: 스포츠·레크리에이션직
- `13`: 경호·경비직
- `14`: 돌봄 서비스직(간병·육아)
- `15`: 청소 및 기타 개인서비스직
- `16`: 미용·예식 서비스직
- `17`: 여행·숙박·오락 서비스직
- `18`: 음식 서비스직
- `19`: 영업·판매직
- `20`: 운전·운송직
- `21`: 건설·채굴직
- `22`: 식품 가공·생산직
- `23`: 인쇄·목재·공예 및 기타 설치·정비·생산직
- `24`: 제조 단순직
- `25`: 기계 설치·정비·생산직
- `26`: 금속·재료 설치·정비·생산직(판금·단조·주조·용접·도장 등)
- `27`: 전기·전자 설치·정비·생산직
- `28`: 정보통신 설치·정비직
- `29`: 화학·환경 설치·정비·생산직
- `30`: 섬유·의복 생산직
- `31`: 농림어업직

### programType (체험유형)
- `all`: 전체
- `field_company`: 현장체험
- `job_practice`: 직무실습
- `field_academic`: 현장학습
- `subject`: 과목연계
- `camp`: 캠프
- `lecture`: 강의
- `dialogue`: 대화

### cost (비용)
- `all`: 전체
- `free`: 무료
- `paid`: 유료

### sortBy (정렬 기준)
- `latest`: 최신순
- `popular`: 인기순
- `deadline`: 마감순

## 응답 형식

```json
{
  "content": [
    {
      "programId": "string",
      "programTitle": "string",
      "provider": "string",
      "targetAudience": "string",
      "programType": "string",
      "startDate": "string",
      "endDate": "string",
      "relatedMajor": "string",
      "price": "string",
      "imageUrl": "string",
      "eligibleRegion": "string",
      "venueRegion": "string",
      "operateCycle": "string",
      "interestCategory": "string",
      "interestText": "string",
      "likeCount": 0,
      "registrationCount": 0,
      "createdAt": "2025-08-30T00:00:00",
      "modifiedAt": "2025-08-30T00:00:00"
    }
  ],
  "pageNumber": 1,
  "pageSize": 10,
  "totalElements": 100,
  "totalPages": 10,
  "hasNext": true,
  "hasPrevious": false
}
```

## 예시 요청

### 1. 정보통신 연구개발직 및 공학기술직 무료 프로그램 검색
```
GET /programs/search?interestCategory=2&cost=free&sortBy=latest&page=1&size=10
```

### 2. 특정 기간 동안의 현장체험 프로그램 검색
```
GET /programs/search?programType=field_company&startDate=2025-09-01&endDate=2025-12-31&sortBy=popular&page=1&size=10
```

### 3. 전체 프로그램을 인기순으로 정렬
```
GET /programs/search?sortBy=popular&page=1&size=20
```

### 4. 무료 프로그램을 마감순으로 정렬
```
GET /programs/search?cost=free&sortBy=deadline&page=1&size=10
```

### 5. 보건·의료직 프로그램 검색
```
GET /programs/search?interestCategory=10&programType=field_company&page=1&size=10
```

### 6. 교육직 프로그램 검색 (최신순)
```
GET /programs/search?interestCategory=6&sortBy=latest&page=1&size=10
```

## 에러 응답

```json
{
  "timestamp": "2025-08-30T00:00:00",
  "status": 400,
  "errorCode": "INVALID_INPUT_VALUE",
  "message": "관심 카테고리는 0-31 사이의 숫자여야 합니다.",
  "path": "/programs/search"
}
```

## 에러 코드

| 에러 코드 | 상태 코드 | 설명 |
|----------|----------|------|
| INVALID_INPUT_VALUE | 400 | 입력값이 잘못되었습니다 |
| INVALID_INPUT_FORMAT | 400 | 입력 형식이 올바르지 않습니다 |
| INVALID_ENUM_VALUE | 400 | enum 값이 잘못되었습니다 |

## 구현된 기능

1. **필터링**: 관심 카테고리, 체험유형, 비용, 날짜 범위로 필터링
2. **정렬**: 최신순, 인기순, 마감순으로 정렬
3. **페이지네이션**: 페이지 번호와 크기로 결과 분할
4. **유효성 검사**: 모든 입력 파라미터에 대한 유효성 검사
5. **에러 처리**: 적절한 에러 메시지와 상태 코드 반환
6. **좋아요/등록자 수**: 각 프로그램의 좋아요 수와 등록자 수 포함

## 데이터베이스 쿼리 최적화

- JPA Query 어노테이션을 사용한 효율적인 쿼리 작성
- 정렬 기준별로 최적화된 쿼리 분리
- LEFT JOIN을 사용한 좋아요 수 집계
- 인덱스를 고려한 쿼리 구조 설계

## Swagger UI 사용법

1. **로컬 서버**: `http://localhost:8080` - 개발 환경에서 테스트
2. **프로덕션 서버**: `https://api.kkumgeurimi.r-e.kr` - 프로덕션 환경에서 테스트

Swagger UI에서 서버를 선택하여 API를 테스트할 수 있습니다.

## 관심 카테고리 매핑 참고

프로그램의 `interestCategory` 필드에 저장되는 값은 다음과 같이 매핑됩니다:

- `HUM_SOC_RESEARCH` ← `0`
- `NAT_BIO_RESEARCH` ← `1`
- `ICT_RND_ENGINEERING` ← `2`
- `CONSTR_MINING_RND_ENGINEERING` ← `3`
- `MANUFACTURING_RND_ENGINEERING` ← `4`
- `WELFARE_RELIGION` ← `5`
- `EDUCATION` ← `6`
- `LAW` ← `7`
- `POLICE_FIRE_CORRECTION` ← `8`
- `MILITARY` ← `9`
- `HEALTH_MEDICAL` ← `10`
- `ARTS_DESIGN_MEDIA` ← `11`
- `SPORTS_RECREATION` ← `12`
- `SECURITY_GUARD` ← `13`
- `CARE_SERVICE` ← `14`
- `CLEANING_PERSONAL_SERVICE` ← `15`
- `BEAUTY_WEDDING_SERVICE` ← `16`
- `TRAVEL_LODGING_ENTERTAINMENT` ← `17`
- `FOOD_SERVICE` ← `18`
- `SALES` ← `19`
- `DRIVING_TRANSPORT` ← `20`
- `CONSTRUCTION_MINING` ← `21`
- `FOOD_PROCESSING_PRODUCTION` ← `22`
- `PRINT_WOOD_CRAFT_ETC_INSTALL_MAINT_PROD` ← `23`
- `MANUFACTURING_SIMPLE` ← `24`
- `MACHINE_INSTALL_MAINT_PROD` ← `25`
- `METAL_MATERIAL_INSTALL_MAINT_PROD` ← `26`
- `ELECTRICAL_ELECTRONIC_INSTALL_MAINT_PROD` ← `27`
- `ICT_INSTALL_MAINT` ← `28`
- `CHEM_ENV_INSTALL_MAINT_PROD` ← `29`
- `TEXTILE_APPAREL_PROD` ← `30`
- `AGRI_FISHERY` ← `31`
