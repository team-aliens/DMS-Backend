package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort

interface SchoolPort :
    QuerySchoolPort,
    CommandSchoolPort,
    ManagerQuerySchoolPort,
    StudentQuerySchoolPort {
}