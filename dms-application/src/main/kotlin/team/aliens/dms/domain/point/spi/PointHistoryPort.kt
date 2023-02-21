package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.student.spi.StudentQueryPointHistoryPort

interface PointHistoryPort :
    CommandPointHistoryPort,
    QueryPointHistoryPort,
    ManagerQueryPointHistoryPort,
    StudentQueryPointHistoryPort {
}