package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service

@Service
class VolunteerScoreService(
    commandVolunteerScoreService: CommandVolunteerScoreService,
    getVolunteerScoreService: GetVolunteerScoreService
) : CommandVolunteerScoreService by commandVolunteerScoreService,
    GetVolunteerScoreService by getVolunteerScoreService
