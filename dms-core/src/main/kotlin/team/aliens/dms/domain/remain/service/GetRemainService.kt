package team.aliens.dms.domain.remain.service

import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus
import java.util.UUID

interface GetRemainService {

    fun getRemainAvailableTimeBySchoolId(schoolId: UUID): RemainAvailableTime

    fun getRemainOptionById(remainOptionId: UUID, schoolId: UUID): RemainOption

    fun queryAllRemainOptionsBySchoolId(schoolId: UUID): List<RemainOption>

    fun queryAllByStudentId(studentIds: List<UUID>): List<RemainStatusInfo>

    fun queryRemainStatusById(userId: UUID): RemainStatus?

    fun getRemainStatusById(userId: UUID): RemainStatus
}
