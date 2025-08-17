package team.aliens.dms.common.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotNullElementsValidator::class])
annotation class NotNullElements(
    val message: String = "List cannot contain null fields",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class NotNullElementsValidator : ConstraintValidator<NotNullElements?, List<Any?>> {

    override fun initialize(notNullElements: NotNullElements?) {}

    override fun isValid(
        objects: List<Any?>,
        context: ConstraintValidatorContext,
    ) = objects.stream().allMatch { it != null }
}
