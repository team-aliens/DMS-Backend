package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort

interface UserPort :
    QueryUserPort,
    CommandUserPort,
    StudentQueryUserPort,
    StudentCommandUserPort,
    ManagerQueryUserPort,
    ManagerCommandUserPort,
    AuthQueryUserPort,
    NoticeQueryUserPort,
    SchoolQueryUserPort,
    PointQueryUserPort,
    StudyRoomQueryUserPort {
}