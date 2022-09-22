package team.aliens.dms.global.error

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 *
 * 전역에 발생하는 Error 를 핸들링 해주는 GlobalErrorHandler
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
@RestControllerAdvice
class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindException): BindErrorResponse? = ErrorResponse.of(e)
}