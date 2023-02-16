package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.StudentQueryPhrasePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import java.security.SecureRandom

@ReadOnlyUseCase
class StudentMyPageUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryPointPort: StudentQueryPointPort,
    private val studentQueryPhrasePort: StudentQueryPhrasePort
) {

    fun execute(): StudentMyPageResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
        val school = querySchoolPort.querySchoolById(student.schoolId) ?: throw SchoolNotFoundException

        val (bonusPoint, minusPoint) =
            queryPointPort.queryBonusAndMinusTotalPointByGcnAndStudentName(student.gcn, student.name)

        val phrase = randomPhrase(bonusPoint, minusPoint)

        return StudentMyPageResponse(
            schoolName = school.name,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl!!,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = phrase
        )
    }

    private fun randomPhrase(bonusPoint: Int, minusPoint: Int): String {
        val bonusPhrase = studentQueryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(
            type = PointType.BONUS, point = bonusPoint
        )
        val minusPhrase = studentQueryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(
            type = PointType.MINUS, point = minusPoint
        )

        val phrases = listOf<Phrase>()
            .plus(bonusPhrase)
            .plus(minusPhrase)

        val phrase = if(phrases.isNotEmpty()) {
            val randomIndex = SecureRandom().nextInt(phrases.size)
            phrases[randomIndex].content
        } else Phrase.NO_PHRASE

        return phrase
    }
}