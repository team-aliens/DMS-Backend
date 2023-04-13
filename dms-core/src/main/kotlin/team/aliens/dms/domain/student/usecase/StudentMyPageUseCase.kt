package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.QueryPhrasePort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import java.security.SecureRandom

@ReadOnlyUseCase
class StudentMyPageUseCase(
    private val securityPort: SecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val querySchoolPort: QuerySchoolPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val queryPhrasePort: QueryPhrasePort
) {

    fun execute(): StudentMyPageResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
        val school = querySchoolPort.querySchoolById(student.schoolId) ?: throw SchoolNotFoundException

        val (bonusPoint, minusPoint) =
            queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(student.gcn, student.name)

        val phrase = randomPhrase(bonusPoint, minusPoint)

        return StudentMyPageResponse(
            schoolName = school.name,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = phrase
        )
    }

    private fun randomPhrase(bonusPoint: Int, minusPoint: Int): String {
        val bonusPhrase = queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(
            type = PointType.BONUS, point = bonusPoint
        )
        val minusPhrase = queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(
            type = PointType.MINUS, point = minusPoint
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
