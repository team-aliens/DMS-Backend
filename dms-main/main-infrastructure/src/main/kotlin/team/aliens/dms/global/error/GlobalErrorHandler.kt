package team.aliens.dms.global.error

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import team.aliens.dms.common.error.DmsException

@RestControllerAdvice
class GlobalErrorHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 컨트롤러/유스케이스/서비스에서 던진 DmsException은 여기서 처리한다.
     * 필터 체인에서 던진 예외는 ExceptionFilter 담당.
     **/
    @ExceptionHandler(DmsException::class)
    protected fun handleDmsException(e: DmsException): ResponseEntity<ErrorResponse> {
        log.info("business exception: {} {}", e.errorProperty.code(), e.errorProperty.message())
        return ResponseEntity
            .status(e.errorProperty.status())
            .body(ErrorResponse.of(e.errorProperty))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindingResult): ValidationErrorResponse? = ErrorResponse.of(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolationException(
        e: ConstraintViolationException
    ): ValidationErrorResponse? = ErrorResponse.of(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotViolationException(
        e: BindingResult
    ): ValidationErrorResponse? = ErrorResponse.of(e)
}
