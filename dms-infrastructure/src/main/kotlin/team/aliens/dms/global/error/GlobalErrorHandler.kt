package team.aliens.dms.global.error

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindingResult): ValidationErrorResponse? = ErrorResponse.of(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolationException(e: ConstraintViolationException): ValidationErrorResponse? = ErrorResponse.of(e)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotViolationException(e: BindingResult): ValidationErrorResponse? = ErrorResponse.of(e)
}
