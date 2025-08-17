package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingType

interface CommandOutingTypePort {

    fun saveOutingType(outingType: OutingType): OutingType

    fun deleteOutingType(outingType: OutingType)
}
