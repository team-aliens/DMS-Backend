package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.QueryPhrasePort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.school.validateSameSchool
import java.time.LocalDateTime
import java.util.UUID

@Service
class GetPointServiceImpl(
    private val queryPhrasePort: QueryPhrasePort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val queryPointOptionPort: QueryPointOptionPort
)  : GetPointService {

    override fun queryPhraseAllByPointTypeAndStandardPoint(type: PointType, point: Int): List<Phrase> {
        return queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type, point)
    }

    override fun getPointHistoryById(pointHistoryId: UUID, schoolId: UUID): PointHistory {
        return (queryPointHistoryPort.queryPointHistoryById(pointHistoryId) ?: throw PointHistoryNotFoundException)
            .apply { validateSameSchool(this.schoolId, schoolId) }
    }

    override fun queryBonusAndMinusTotalPointByStudentGcnAndName(gcn: String, studentName: String) =
        queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, studentName)

    override fun queryPointHistoryByStudentGcnAndNameAndType(
        gcn: String,
        studentName: String,
        type: PointType?,
        isCancel: Boolean?,
        pageData: PageData
    ) = queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
        gcn = gcn,
        studentName = studentName,
        type = type,
        isCancel = isCancel,
        pageData = pageData
    )

    override fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean?,
        pageData: PageData
    ) = queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
        schoolId = schoolId,
        type = type,
        isCancel = isCancel,
        pageData = pageData
    )

    override fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ) = queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId = schoolId,
        startAt = startAt,
        endAt
    )

    override fun getPointOptionById(pointOptionId: UUID, schoolId: UUID): PointOption {
        return (queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException)
            .apply { validateSameSchool(this.schoolId, schoolId) }

    }

    override fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?) =
        queryPointOptionPort.queryPointOptionsBySchoolIdAndKeyword(schoolId, keyword)

    override fun getTotalPoint(type: PointRequestType, bonusTotal: Int, minusTotal: Int): Int {
        val totalPoint = when (type) {
            PointRequestType.BONUS -> bonusTotal
            PointRequestType.MINUS -> minusTotal
            PointRequestType.ALL -> bonusTotal - minusTotal
        }
        return totalPoint
    }

    override fun getPointHistoriesByStudentsAndPointOptionAndSchoolId(
        students: List<StudentWithPointVO>,
        pointOption: PointOption,
        schoolId: UUID
    ): List<PointHistory> {
        return students.map {
            val (updatedBonusTotal, updatedMinusTotal) = it.calculateUpdatedPointTotal(
                pointOption.type, pointOption.score
            )

            PointHistory(
                studentName = it.name,
                studentGcn = it.gcn,
                bonusTotal = updatedBonusTotal,
                minusTotal = updatedMinusTotal,
                isCancel = false,
                pointName = pointOption.name,
                pointScore = pointOption.score,
                pointType = pointOption.type,
                createdAt = LocalDateTime.now(),
                schoolId = schoolId
            )
        }
    }
}