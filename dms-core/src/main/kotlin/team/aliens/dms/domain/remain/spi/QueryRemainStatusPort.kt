package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import team.aliens.dms.domain.remain.model.RemainStatus
import java.util.UUID

interface QueryRemainStatusPort {

    fun queryAllByStudentId(studentIds: List<UUID>): List<RemainStatusInfo>

    fun queryRemainStatusById(studentId: UUID): RemainStatus?
}
