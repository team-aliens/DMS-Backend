package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.file.spi.FileQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort

interface UserPort :
    QueryUserPort,
    CommandUserPort,
    StudentQueryUserPort,
    StudentCommandUserPort,
    ManagerQueryUserPort,
    ManagerCommandUserPort,
    AuthQueryUserPort,
    NoticeQueryUserPort,
    TagQueryUserPort,
    SchoolQueryUserPort,
    PointQueryUserPort,
    StudyRoomQueryUserPort,
    RemainQueryUserPort,
    FileQueryUserPort
