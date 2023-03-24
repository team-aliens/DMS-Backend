package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.remain.spi.RemainQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.tag.spi.TagQueryStudentPort
import team.aliens.dms.domain.user.spi.UserQueryStudentPort

interface StudentPort :
    QueryStudentPort,
    CommandStudentPort,
    AuthQueryStudentPort,
    MealQueryStudentPort,
    UserQueryStudentPort,
    ManagerQueryStudentPort,
    StudyRoomQueryStudentPort,
    PointQueryStudentPort,
    RemainQueryStudentPort,
    TagQueryStudentPort
