package team.aliens.dms.domain.point.stub

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.GetPointService
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentTotalVO
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import java.time.LocalDateTime
import java.util.UUID

abstract class GetPointServiceStub : GetPointService {
    override fun queryAllPhraseByPointTypeAndStandardPoint(
        type: PointType,
        standardPoint: Int
    ): List<Phrase> = throw UnsupportedOperationException()

    override fun getPointHistoryById(pointHistoryId: UUID): PointHistory = throw UnsupportedOperationException()
    override fun queryBonusAndMinusTotalPointByStudentGcnAndName(
        gcn: String,
        studentName: String
    ): Pair<Int, Int> = throw UnsupportedOperationException()

    override fun queryPointHistoryByStudentGcnAndNameAndType(
        gcn: String,
        studentName: String,
        type: PointType?,
        isCancel: Boolean?,
        pageData: PageData
    ): List<PointHistoryVO> = throw UnsupportedOperationException()

    override fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean?,
        pageData: PageData
    ): List<StudentPointHistoryVO> = throw UnsupportedOperationException()

    override fun queryRecentPointHistoryBySchoolId(
        schoolId: UUID,
        studentName: String?,
        studentGcn: String?
    ): StudentPointHistoryVO? = throw UnsupportedOperationException()

    override fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory> = throw UnsupportedOperationException()

    override fun getPointHistoryByGcnIn(gcns: List<String>): List<PointHistory> = throw UnsupportedOperationException()

    override fun getPointOptionById(
        pointOptionId: UUID,
        schoolId: UUID
    ): PointOption = throw UnsupportedOperationException()

    override fun queryPointOptionsBySchoolIdAndKeyword(
        schoolId: UUID,
        keyword: String?
    ): List<PointOption> = throw UnsupportedOperationException()

    override fun getPointHistoriesByStudentsAndPointOptionAndSchoolId(
        students: List<StudentWithPointVO>,
        pointOption: PointOption,
        schoolId: UUID
    ): List<PointHistory> = throw UnsupportedOperationException()

    override fun getPointTotalsGroupByStudent(): List<StudentTotalVO> = throw UnsupportedOperationException()
}