package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingApplication

interface CommandOutingService {

    fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication
}
