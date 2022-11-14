package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.manager.spi.ManagerQueryPointPort
import team.aliens.dms.domain.student.spi.StudentQueryPointPort

interface PointPort :
    QueryPointPort,
    ManagerQueryPointPort,
    StudentQueryPointPort {
}