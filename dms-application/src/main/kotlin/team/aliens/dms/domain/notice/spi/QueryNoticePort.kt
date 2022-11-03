package team.aliens.dms.domain.notice.spi

import team.aliens.dms.domain.notice.model.Notice
import java.time.LocalDate
import java.util.UUID

interface QueryNoticePort {

    fun existsNoticeByDateBetween(to: LocalDate, from: LocalDate): Boolean

    fun queryNoticeById(noticeId: UUID): Notice?

}
