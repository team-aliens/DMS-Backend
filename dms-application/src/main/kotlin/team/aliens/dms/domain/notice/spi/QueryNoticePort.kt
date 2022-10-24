package team.aliens.dms.domain.notice.spi

import java.time.LocalDate

interface QueryNoticePort {

    fun existsByDateBetween(to: LocalDate, from: LocalDate): Boolean

}
