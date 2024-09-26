package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service

@Service
class VolunteerService(
    checkVolunteerService: CheckVolunteerService,
    commandVolunteerService: CommandVolunteerService,
    getVolunteerService: GetVolunteerService
) : CheckVolunteerService by checkVolunteerService,
    CommandVolunteerService by commandVolunteerService,
    GetVolunteerService by getVolunteerService
