package team.aliens.dms.domain.school.aop

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.exception.FeatureNotAvailableException
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@Component
@Aspect
class AvailableFeatureAspect(
    private val schoolSecurityPort: SchoolSecurityPort,
    private val schoolQueryUserPort: SchoolQueryUserPort,
    private val querySchoolPort: QuerySchoolPort
) {

    @Before("within(team.aliens.dms.domain.meal.usecase.*)")
    fun beforeMealService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.mealService) {
            throw FeatureNotAvailableException
        }
    }

    @Before("within(team.aliens.dms.domain.notice.usecase.*)")
    fun beforeNoticeService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.noticeService) {
            throw FeatureNotAvailableException
        }
    }

    @Before("within(team.aliens.dms.domain.point.usecase.*)")
    fun beforePointService()  {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.pointService) {
            throw FeatureNotAvailableException
        }
    }

    @Before("within(team.aliens.dms.domain.studyroom.usecase.*)")
    fun beforeStudyRoomService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.studyRoomService) {
            throw FeatureNotAvailableException
        }
    }

    private fun getAvailableFeature(): AvailableFeature {
        val currentUserId = schoolSecurityPort.getCurrentUserId()
        val currentUser = schoolQueryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        return querySchoolPort.queryAvailableFeaturesBySchoolId(currentUser.schoolId) ?: throw FeatureNotFoundException
    }
}