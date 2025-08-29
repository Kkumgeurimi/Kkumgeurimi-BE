package com.kkumgeurimi.kopring.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        ex: CustomException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = ex.errorCode.status,
            errorCode = ex.errorCode.name,
            message = ex.errorCode.message,
            path = request.requestURI
        )
        
        logger.info("메시지: [{}] {}", errorDto.errorCode, errorDto.message)
        return ResponseEntity.status(HttpStatusCode.valueOf(ex.errorCode.status)).body(errorDto)
    }
    
    /**
     * Bean Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBindException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val fieldError = ex.bindingResult.fieldErrors.firstOrNull()
        val errorMessage = fieldError?.let { "${it.field}: ${it.defaultMessage}" } 
            ?: "입력값 검증에 실패했습니다"
        
        val errorCode = ErrorCode.VALIDATION_FAILED
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = errorMessage,
            path = request.requestURI
        )
        
        logger.warn("Validation failed: {}", errorMessage)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
    
    /**
     * 타입 미스매치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        ex: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorCode = ErrorCode.INVALID_ENUM_VALUE
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = "${ex.name}: ${errorCode.message}",
            path = request.requestURI
        )
        
        logger.warn("Type mismatch: {}", ex.message)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
    
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingRequestParamException(
        ex: MissingServletRequestParameterException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorCode = ErrorCode.MISSING_PARAMETER
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = "${ex.parameterName}: ${errorCode.message}",
            path = request.requestURI
        )
        
        logger.warn("Missing parameter: {}", ex.parameterName)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
    
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(
        ex: BadCredentialsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorCode = ErrorCode.INVALID_CREDENTIALS
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = errorCode.message,
            path = request.requestURI
        )
        
        logger.warn("Bad credentials: {}", ex.message)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
    
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorCode = ErrorCode.INVALID_INPUT_VALUE
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = ex.message ?: errorCode.message,
            path = request.requestURI
        )
        
        logger.warn("IllegalArgumentException: {}", ex.message)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorDto> {
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val errorDto = ErrorDto(
            timestamp = LocalDateTime.now().toString(),
            status = errorCode.status,
            errorCode = errorCode.name,
            message = errorCode.message,
            path = request.requestURI
        )
        
        logger.error("Unexpected error occurred", ex)
        return ResponseEntity.status(HttpStatusCode.valueOf(errorCode.status)).body(errorDto)
    }
}
