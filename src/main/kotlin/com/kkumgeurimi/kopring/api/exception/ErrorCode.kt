package com.kkumgeurimi.kopring.api.exception

enum class ErrorCode(
    val status: Int,
    val message: String
) {
    // Default
    ERROR(400, "요청 처리에 실패했습니다."),

    // 400 Bad Request - 입력 에러
    INVALID_INPUT_FORMAT(400, "유효하지 않은 형식입니다."),
    INVALID_INPUT_LENGTH(400, "입력 길이가 잘못되었습니다."),
    INVALID_INPUT_VALUE(400, "입력값이 잘못되었습니다."),
    MISSING_PARAMETER(400, "필수 파라미터가 누락되었습니다."),
    INVALID_ENUM_VALUE(400, "enum 값이 잘못되었습니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN_FORMAT(400, "올바르지 않은 토큰 형식입니다."),

    // 401 Unauthorized - 인증 에러
    NOT_AUTHENTICATED(401, "로그인 상태가 아닙니다."),
    INVALID_CREDENTIALS(401, "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(401, "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, "만료된 리프레시 토큰입니다."),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다."),
    BLACKLISTED_TOKEN(401, "이미 로그아웃된 토큰입니다."),

    // 403 Forbidden - 권한 에러
    UNAUTHORIZED_REQUEST(403, "권한이 없습니다."),
    ACCESS_DENIED(403, "접근이 거부되었습니다."),

    // 404 Not Found - 리소스 없음
    STUDENT_NOT_FOUND(404, "존재하지 않는 학생입니다."),
    RESOURCE_NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
    PROGRAM_NOT_FOUND(404, "프로그램이 존재하지 않습니다."),
    PROGRAM_LIKE_NOT_FOUND(404, "프로그램 찜이 존재하지 않습니다."),
    POST_NOT_FOUND(404, "게시글이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(404, "댓글이 존재하지 않습니다."),

    // 409 Conflict - 중복 리소스
    DUPLICATE_EMAIL(409, "이미 존재하는 이메일입니다."),
    STUDENT_ALREADY_EXISTS(409, "이미 존재하는 학생입니다."),
    DUPLICATE_PROGRAM_REGISTRATION(409, "이미 신청한 프로그램입니다."),
    DUPLICATE_PROGRAM_LIKE(409, "이미 찜한 프로그램입니다."),
    DUPLICATE_POST_LIKE(409, "이미 좋아요한 게시글입니다."),

    // 422 Unprocessable Entity - 검증 에러
    VALIDATION_FAILED(422, "입력값 검증에 실패했습니다."),

    // 500 Internal Server Error - 서버 에러
    REDIS_ERROR(500, "서버에서 Redis 사용 중 문제가 발생했습니다."),
    DATABASE_ERROR(500, "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    JWT_PROCESSING_ERROR(500, "JWT 토큰 처리 중 오류가 발생했습니다.")
}
