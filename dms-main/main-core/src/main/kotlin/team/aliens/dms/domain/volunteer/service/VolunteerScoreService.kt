package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service

@Service
class VolunteerScoreService(
    commandVolunteerScoreService: CommandVolunteerScoreService
) : CommandVolunteerScoreService by commandVolunteerScoreService
