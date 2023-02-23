package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import java.util.UUID

interface QueryRemainStatusPort {
    fun queryByStudentIdIn(studentIds: List<UUID>): List<RemainStatusInfo>
}