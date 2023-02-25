package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.manager.spi.ManagerCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort

interface RemainStatusPort :
    CommandRemainStatusPort,
    QueryRemainStatusPort,
    StudentCommandRemainStatusPort,
    ManagerCommandRemainStatusPort {
}