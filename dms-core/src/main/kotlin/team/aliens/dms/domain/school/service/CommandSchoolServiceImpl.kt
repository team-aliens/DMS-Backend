package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.school.model.ApplicationAvailableTime
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.CommandSchoolPort

@Service
class CommandSchoolServiceImpl(
    private val commandSchoolPort: CommandSchoolPort
) : CommandSchoolService {

    override fun saveSchool(school: School) =
            commandSchoolPort.saveSchool(school)

    override fun saveApplicationAvailableTime(applicationAvailableTime: ApplicationAvailableTime) =
            commandSchoolPort.saveApplicationAvailableTime(applicationAvailableTime)
}
