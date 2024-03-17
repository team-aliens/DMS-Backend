package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.QueryPhrasePort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.point.spi.vo.StudentPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import java.time.LocalDateTime
import java.util.UUID

@Service
class GetPointServiceImpl(
    private val queryPhrasePort: QueryPhrasePort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val queryPointOptionPort: QueryPointOptionPort
) : GetPointService {

    override fun queryAllPhraseByPointTypeAndStandardPoint(type: PointType, standardPoint: Int) =
        queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type, standardPoint)

    override fun getPointHistoryById(pointHistoryId: UUID) =
        queryPointHistoryPort.queryPointHistoryById(pointHistoryId) ?: throw PointHistoryNotFoundException

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

    override fun queryRecentPointHistoryBySchoolId(
        schoolId: UUID,
        studentName: String?,
        studentGcn: String?
    ): StudentPointHistoryVO? =
        queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
            schoolId = schoolId,
            studentName = studentName,
            studentGcn = studentGcn,
            pageData = PageData.LIMIT1
        ).getOrNull(0)

    override fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ) = queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId = schoolId,
        startAt = startAt,
        endAt = endAt
    )

    override fun getPointHistoryByGcnIn(
        gcns: List<String>
    ): List<PointHistory> = queryPointHistoryPort.queryPointHistoryByGcnIn(
        gcns = gcns
    )

    override fun getPointOptionById(pointOptionId: UUID, schoolId: UUID) =
        queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException

    override fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?) =
        queryPointOptionPort.queryPointOptionsBySchoolIdAndKeyword(schoolId, keyword)

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
