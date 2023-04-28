package team.aliens.dms.domain.remain.service

import java.util.UUID
import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.model.RemainStatus

interface GetRemainService {

    fun getRemainAvailableTimeBySchoolId(schoolId: UUID): RemainAvailableTime

    fun getRemainOptionById(remainOptionId: UUID): RemainOption

    fun getAllRemainOptionsBySchoolId(schoolId: UUID): List<RemainOption>

    fun getAllRemainStatusInfoByStudentId(studentIds: List<UUID>): List<RemainStatusInfo>

    fun getRemainStatusById(userId: UUID): RemainStatus?

    fun getAppliedRemainOptionByUserId(userId: UUID): RemainOption
}
