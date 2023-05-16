package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.dto.StudentResponse
import team.aliens.dms.domain.user.service.UserService
import java.security.SecureRandom

@ReadOnlyUseCase
class StudentMyPageUseCase(
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val pointService: PointService
) {

    fun execute(): StudentResponse {

        val student = userService.getCurrentStudent()
        val school = schoolService.getSchoolById(student.schoolId)

        val (bonusPoint, minusPoint) =
            pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(student.gcn, student.name)

        val phrase = randomPhrase(bonusPoint, minusPoint)

        return StudentResponse.of(
            schoolName = school.name,
            student = student,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = phrase
        )
    }

    private fun randomPhrase(bonusPoint: Int, minusPoint: Int): String {
        val bonusPhrase = pointService.queryAllPhraseByPointTypeAndStandardPoint(
            type = PointType.BONUS, standardPoint = bonusPoint
        )
        val minusPhrase = pointService.queryAllPhraseByPointTypeAndStandardPoint(
            type = PointType.MINUS, standardPoint = minusPoint
        )

        val phrases = listOf<Phrase>()
            .plus(bonusPhrase)
            .plus(minusPhrase)

        val phrase = if (phrases.isNotEmpty()) {
            val randomIndex = SecureRandom().nextInt(phrases.size)
            phrases[randomIndex].content
        } else Phrase.NO_PHRASE

        return phrase
    }
}
