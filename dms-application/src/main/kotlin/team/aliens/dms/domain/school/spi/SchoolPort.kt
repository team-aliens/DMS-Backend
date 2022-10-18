package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort

interface SchoolPort : QuerySchoolPort, ManagerQuerySchoolPort {
}