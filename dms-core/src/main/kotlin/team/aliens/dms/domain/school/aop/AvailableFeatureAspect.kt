package team.aliens.dms.domain.school.aop

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.exception.FeatureNotAvailableException
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
@Aspect
class AvailableFeatureAspect(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val querySchoolPort: QuerySchoolPort
) {

    @Before(
        "within(team.aliens.dms.domain.meal.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeMealService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.mealService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.notice.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeNoticeService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.noticeService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.point.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforePointService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.pointService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.studyroom.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeStudyRoomService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.studyRoomService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.remain.usecase.*) && " +
            "!@annotation(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeRemainRoomService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.remainService) {
            throw FeatureNotAvailableException
        }
    }

    private fun getAvailableFeature(): AvailableFeature {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        return querySchoolPort.queryAvailableFeaturesBySchoolId(currentUser.schoolId) ?: throw FeatureNotFoundException
    }
}
