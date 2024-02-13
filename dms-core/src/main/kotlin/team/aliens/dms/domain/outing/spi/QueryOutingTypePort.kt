package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingType

interface QueryOutingTypePort {

    fun existsOutingType(outingType: OutingType): Boolean

    fun queryOutingType(outingType: OutingType): OutingType?
}
