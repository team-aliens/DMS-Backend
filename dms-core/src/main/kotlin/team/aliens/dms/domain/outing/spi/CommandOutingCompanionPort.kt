package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingCompanion

interface CommandOutingCompanionPort {

    fun saveAllOutingCompanions(outingCompanions: List<OutingCompanion>)
}
