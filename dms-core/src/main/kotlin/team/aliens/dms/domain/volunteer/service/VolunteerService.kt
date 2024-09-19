package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service

@Service
class VolunteerService(
    commandVolunteerService: CommandVolunteerService,
    getVolunteerService: GetVolunteerService
) : CommandVolunteerService by commandVolunteerService,
    GetVolunteerService by getVolunteerService
