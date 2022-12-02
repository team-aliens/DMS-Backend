package team.aliens.dms.global.error

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import team.aliens.dms.common.error.ErrorProperty


data class ErrorResponse(
    val status: Int,
    val message: String
) {

    companion object {
        fun of(errorProperty: ErrorProperty) = ErrorResponse(
            errorProperty.status(),
            errorProperty.message()
        )

        fun of(e: BindingResult): ValidationErrorResponse {
            val errorMap = HashMap<String, String?>()

            for (error: FieldError in e.fieldErrors) {
                errorMap[error.field] = error.defaultMessage
            }

            return ValidationErrorResponse(
                status = GlobalErrorCode.BAD_REQUEST.status(),
                fieldError = listOf(errorMap)
            )
        }

        fun of(e: ConstraintViolationException): ValidationErrorResponse {
            val errorMap = HashMap<String, String?>()

            for(error: ConstraintViolation<*> in e.constraintViolations) {
                val errorPropertyPath = error.propertyPath.toString()

                /**
                 * error.propertyPath 는 예외가 발생한 메소드 이름부터 필드 이름까지 전부 불러오기 때문에 .을 기준으로 메소드 이름을 제거
                 **/
                val index = errorPropertyPath.indexOf(".")
                val errorField = errorPropertyPath.substring(index.plus(1))

                errorMap[errorField] = error.message
            }

            return ValidationErrorResponse(
                status = GlobalErrorCode.BAD_REQUEST.status(),
                fieldError = listOf(errorMap)
            )
        }
    }
}

data class ValidationErrorResponse(
    val status: Int,
    val fieldError: List<Map<String, String?>>
)