package team.aliens.dms.common.spi

import team.aliens.dms.domain.auth.spi.AuthSecurityPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.SeatTypeSecurityPort
import team.aliens.dms.domain.user.spi.UserSecurityPort

interface SecurityPort :
    ManagerSecurityPort,
    StudentSecurityPort,
    UserSecurityPort,
    MealSecurityPort,
    AuthSecurityPort,
    NoticeSecurityPort,
    SchoolSecurityPort,
    PointSecurityPort,
    StudyRoomSecurityPort,
    SeatTypeSecurityPort {
}