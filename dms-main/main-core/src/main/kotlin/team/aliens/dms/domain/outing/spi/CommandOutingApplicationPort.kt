package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingApplication

interface CommandOutingApplicationPort {

    fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication

    fun deleteOutingApplication(outingApplication: OutingApplication)
}
