package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort

interface UserPort :
    StudentQueryUserPort,
    StudentCommandUserPort,
    ManagerQueryUserPort,
    ManagerCommandUserPort,
    AuthQueryUserPort,
    NoticeQueryUserPort {
}