package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.ApplicationAvailableTime
import team.aliens.dms.domain.school.model.School

interface CommandSchoolPort {

    fun saveSchool(school: School): School

    fun saveApplicationAvailableTime(applicationAvailableTime: ApplicationAvailableTime): ApplicationAvailableTime
}
