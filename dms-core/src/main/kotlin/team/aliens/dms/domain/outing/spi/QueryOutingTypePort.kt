package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingType

interface QueryOutingTypePort {

    fun exists(outingType: OutingType): Boolean
}
