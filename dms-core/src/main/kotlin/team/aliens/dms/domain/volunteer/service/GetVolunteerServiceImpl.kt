package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerApplicationPort
import java.util.UUID

@Service
class GetVolunteerServiceImpl(
    private val queryVolunteerApplicationPort: QueryVolunteerApplicationPort
) : GetVolunteerService {

    override fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication =
        queryVolunteerApplicationPort.queryVolunteerApplicationById(volunteerApplicationId)
            ?: throw VolunteerApplicationNotFoundException
}
