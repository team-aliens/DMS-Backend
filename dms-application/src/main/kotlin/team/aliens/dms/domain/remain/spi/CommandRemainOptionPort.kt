package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainOption

interface CommandRemainOptionPort {

    fun saveRemainOption(remainOption: RemainOption): RemainOption

    fun deleteRemainOption(remainOption: RemainOption)
}