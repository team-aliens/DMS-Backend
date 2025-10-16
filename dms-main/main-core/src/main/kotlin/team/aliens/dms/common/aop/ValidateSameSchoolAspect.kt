package team.aliens.dms.common.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException

@UseCase
@Aspect
class ValidateSameSchoolAspect(
    private val securityPort: SecurityPort,
) {
    @AfterReturning(
        pointcut = "within(*..*Service) && execution (team.aliens.dms.common.model.SchoolIdDomain+ get*(..))",
        returning = "ret"
    )
    fun validateSameSchool(joinPoint: JoinPoint, ret: SchoolIdDomain) {
        if (securityPort.isAuthenticated() && ret.schoolId != securityPort.getCurrentUserSchoolId()) {
            throw SchoolMismatchException
        }
    }
}
