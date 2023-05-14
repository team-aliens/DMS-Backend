package team.aliens.dms.domain.point.spi

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.AllPointHistoryResponse
import team.aliens.dms.domain.point.dto.PointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime
import java.util.UUID

interface QueryPointHistoryPort {

    fun queryPointHistoryById(pointHistoryId: UUID): PointHistory?

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
    ): List<PointHistoryResponse.PointHistoryDto>

    fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<AllPointHistoryResponse.PointHistory>

    fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory>
}
