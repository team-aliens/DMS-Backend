package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.student.spi.StudentQueryPointHistoryPort

// TODO PointHistoryPort -> PointPort
// TODO ManagerQueryPointHistoryPort -> ManagerQueryPointPort
// TODO StudentQueryPointHistoryPort -> StudentQueryPointPort
interface PointHistoryPort :
    QueryPointPort,
    ManagerQueryPointHistoryPort,
    StudentQueryPointHistoryPort {
}