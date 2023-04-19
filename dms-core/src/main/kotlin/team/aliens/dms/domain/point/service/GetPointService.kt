package team.aliens.dms.domain.point.service

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.PointHistoryDto
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import java.time.LocalDateTime
import java.util.UUID

interface GetPointService {

    fun queryPhraseAllByPointTypeAndStandardPoint(type: PointType, point: Int): List<Phrase>

    fun getPointHistoryById(pointHistoryId: UUID, schoolId: UUID): PointHistory

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(
        gcn: String,
        studentName: String
    ): Pair<Int, Int>

    fun queryPointHistoryByStudentGcnAndNameAndType(
        gcn: String,
        studentName: String,
        type: PointType? = null,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<PointHistoryDto>

    fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<QueryAllPointHistoryResponse.PointHistory>

    fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory>

    fun getPointOptionById(pointOptionId: UUID, schoolId: UUID): PointOption

    fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<PointOption>

    fun getTotalPoint(
        type: PointRequestType,
        bonusTotal: Int,
        minusTotal: Int
    ): Int

    fun getPointHistoriesByStudentsAndPointOptionAndSchoolId(
        students: List<StudentWithPointVO>,
        pointOption: PointOption,
        schoolId: UUID
    ): List<PointHistory>
}
