package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.auth.spi.AuthQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.meal.spi.MealQuerySchoolPort
import team.aliens.dms.domain.point.spi.PointQuerySchoolPort
import team.aliens.dms.domain.remain.spi.RemainQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort

interface SchoolPort :
    QuerySchoolPort,
    CommandSchoolPort,
    ManagerQuerySchoolPort,
    StudentQuerySchoolPort,
    AuthQuerySchoolPort,
    StudyRoomQuerySchoolPort,
    PointQuerySchoolPort,
    RemainQuerySchoolPort,
    MealQuerySchoolPort
