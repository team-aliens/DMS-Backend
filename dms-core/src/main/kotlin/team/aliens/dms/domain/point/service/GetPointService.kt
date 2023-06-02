package team.aliens.dms.domain.point.service

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO

interface GetPointService {

    fun queryAllPhraseByPointTypeAndStandardPoint(type: PointType, standardPoint: Int): List<Phrase>

    fun getPointHistoryById(pointHistoryId: UUID): PointHistory

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
    ): List<PointHistoryVO>

    fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<StudentPointHistoryVO>

    fun queryRecentPointHistoryBySchoolId
        schoolId: UUID,
        studentName: String? = null,
        studentGcn: String? = null
    ): StudentPointHistoryVO?

    fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory>

    fun getPointOptionById(pointOptionId: UUID, schoolId: UUID): PointOption

    fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<PointOption>

    fun getPointHistoriesByStudentsAndPointOptionAndSchoolId(
        students: List<StudentWithPointVO>,
        pointOption: PointOption,
        schoolId: UUID
    ): List<PointHistory>
}
